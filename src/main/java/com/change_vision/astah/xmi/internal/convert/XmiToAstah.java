package com.change_vision.astah.xmi.internal.convert;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.TemplateBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		URI loadRoot = URI.createURI(this.fromPath);
		URI rootPath = loadRoot.trimSegments(1);
		Map<URI, URI> relativeResourceURI = putRelativeResourceURI(rootPath);
		Map<URI, URI> globalUriMap = URIConverter.URI_MAP;
		try {
			TransactionManager.beginTransaction();
			globalUriMap.putAll(relativeResourceURI);
			
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
		} finally {
			Set<Entry<URI, URI>> entrySet = relativeResourceURI.entrySet();
			for (Entry<URI, URI> entry : entrySet) {
				URI key = entry.getKey();
				globalUriMap.remove(key);
			}
		}

		logger.debug("Convert XMI file {} to astah file {} done.", fromPath, toPath);
	}

	private Map<URI,URI> putRelativeResourceURI(URI rootPath) {
		FileSystem fileSystem = FileSystems.getDefault();
		String basePath = rootPath.path();
		final Path path = fileSystem.getPath(basePath);
		final Map<URI, URI> relativeResources = new HashMap<URI, URI>();
		try {
			Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				  Path relativePath = path.relativize(file);
				  String relativePathValue = relativePath.toString();
				  URI name = URI.createURI(relativePathValue);
				  URI toURI = URI.createFileURI(file.toString());
				  logger.trace("path:{},absolute:{}",relativePath,toURI.toString());
				  relativeResources.put(name, toURI);
				  return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			logger.error("Loading error.",e);
		}
		return relativeResources;
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
