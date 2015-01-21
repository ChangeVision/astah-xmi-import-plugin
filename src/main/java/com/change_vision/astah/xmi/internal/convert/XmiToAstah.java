package com.change_vision.astah.xmi.internal.convert;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.internal.convert.exception.XMIReadFailedExcetion;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;

import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.TemplateBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Convert XMI(2.1) to Astah Project.
 * 
 * Current Version only supports to convert UML models in Class Diagram (not including diagram).
 */
public class XmiToAstah {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(XmiToAstah.class);

	private ConvertHelper helper;
	private XMILoader loader;
	private String fromPath;
	private Map<Element, IElement> converteds = new HashMap<Element, IElement>();
	private Map<String, Relationship> relationships = new HashMap<String, Relationship>();
	private AstahAPIUtil apiUtil = new AstahAPIUtil();

	public XmiToAstah(String xmiPath) throws IOException {
		this.fromPath = xmiPath;
		this.loader = new XMILoader(xmiPath);
		this.helper = new ConvertHelper();
	}

	public void convert() throws LicenseNotFoundException,
			ProjectNotFoundException, IOException, ProjectLockedException,
			ClassNotFoundException, InvalidEditingException,
			InvalidUsingException, XMIReadFailedExcetion {
		convert(null);
	}

	public void convert(String toPath) throws LicenseNotFoundException,
			ProjectNotFoundException, IOException, ProjectLockedException,
			ClassNotFoundException, InvalidEditingException,
			InvalidUsingException, XMIReadFailedExcetion {
		Package root = loader.getRoot();
		if (root == null) {
			throw new XMIReadFailedExcetion();
		}
		ProjectAccessor pa = ProjectAccessorFactory.getProjectAccessor();
		if (toPath != null) {
			pa.create(toPath);
		}
		try {
			TransactionManager.beginTransaction();
			
			IModel astahModel = pa.getProject();
			astahModel.setName(root.getName());
			convertNormalModel(astahModel, root);
			convertAssociationClasses();
			convertRelationships();
			convertFeatures();
			convertTemplateBindings();
			setElementInformation();
			TransactionManager.endTransaction();
			if (toPath != null) {
				pa.save();
			}
		} catch (Exception e) {
			TransactionManager.abortTransaction();
			throw new RuntimeException(e);
		}

		logger.debug("Convert XMI file {} to astah file {} done.", fromPath, toPath);
	}

	private void setElementInformation() throws InvalidEditingException,
			ClassNotFoundException {
		for (Element e : converteds.keySet()) {
			IElement element = converteds.get(e);
			helper.setElementInfo(e, element);
		}
	}

	private void convertAssociationClasses() throws ClassNotFoundException,
			InvalidEditingException {
		for (Relationship r : relationships.values()) {
			if (r instanceof AssociationClass) {
				convertRelationship(r);
			}
		}
	}

	private void convertFeatures() throws ClassNotFoundException,
			InvalidEditingException, ProjectNotFoundException {
		FeatureModelConverter converter = new FeatureModelConverter(converteds,apiUtil);
		// create attributes and operations
		for (Element e : converteds.keySet().toArray(new Element[0])) {
			if (e instanceof Classifier && converteds.get(e) instanceof IClass) {
				Classifier cls = (Classifier) e;
				converter.convert(cls);
			}
		}
	}

	private void convertRelationships() throws ClassNotFoundException,
			InvalidEditingException {
		for (Relationship r : relationships.values()) {
			if (!(r instanceof AssociationClass)
					&& !(r instanceof TemplateBinding)) {
				convertRelationship(r);
			}
		}
	}

	private void convertTemplateBindings() throws ClassNotFoundException,
			InvalidEditingException {
		for (Relationship r : relationships.values()) {
			if (r instanceof TemplateBinding) {
				convertRelationship(r);
			}
		}
	}

	private INamedElement convertRelationship(Relationship rel)
			throws InvalidEditingException, ClassNotFoundException {
		RelationConverter converter = new RelationConverter(apiUtil);
		return converter.convert(converteds, rel);
	}

	private void convertNormalModel(INamedElement model, Element parent)
			throws InvalidEditingException, ClassNotFoundException {
		if (model == null || parent == null) {
			return;
		}
		CommonModelConverter converter = new CommonModelConverter(helper, converteds, relationships, apiUtil);
		converter.convert(model,parent);
	}

}
