package com.change_vision.astah.xmi.convert;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.InformationFlow;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.PackageMerge;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Realization;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.TemplateBinding;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.TypedElement;
import org.eclipse.uml2.uml.Usage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.editor.ModelEditorFactory;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IAssociation;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IOperation;
import com.change_vision.jude.api.inf.model.IParameter;
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
			InvalidUsingException {
		convert(null);
	}

	public void convert(String toPath) throws LicenseNotFoundException,
			ProjectNotFoundException, IOException, ProjectLockedException,
			ClassNotFoundException, InvalidEditingException,
			InvalidUsingException {
		Package root = loader.getRoot();
		for (Profile p : root.getAppliedProfiles()) {
			URI uri = EcoreUtil.getURI(p);
			new XMILoader(uri.toString());
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
		BasicModelEditor bme = ModelEditorFactory.getBasicModelEditor();
		// create attributes and operations
		for (Element e : converteds.keySet().toArray(new Element[0])) {
			if (e instanceof Classifier && converteds.get(e) instanceof IClass) {
				Classifier cls = (Classifier) e;
				IClass astahClass = (IClass) converteds.get(cls);
				
				if (cls instanceof Enumeration) {
					for (EnumerationLiteral literal : ((Enumeration) cls)
							.getOwnedLiterals()) {
						IAttribute astahAttribute = bme.createAttribute(
								astahClass, literal.getName(), astahClass);
						converteds.put(literal, astahAttribute);
					}
				}
				for (Property prop : cls.getAttributes()) {
					if (prop.getAssociation() == null) {
						IAttribute astahAttribute = bme.createAttribute(
								astahClass, prop.getName(), "int");
						try {
							astahAttribute
									.setQualifiedTypeExpression(getQualifiedTypeExpression(prop));
						} catch (InvalidEditingException ex) {
							//do nothing, just use default type "int" when failed.
						}
						astahAttribute.setStatic(prop.isStatic());
						astahAttribute.setChangeable(prop.isReadOnly());
						if (prop.isComposite()) {
							astahAttribute.setComposite();
						} else if (prop.getAggregation().equals(
								AggregationKind.SHARED)) {
							astahAttribute.setAggregation();
						}
						astahAttribute.setDerived(prop.isDerived());
						astahAttribute.setInitialValue(prop.getDefault());
						converteds.put(prop, astahAttribute);
					}
				}
				for (Operation op : cls.getOperations()) {
					IOperation astahOperation = bme.createOperation(astahClass,
							op.getName(), "void");
					astahOperation.setStatic(op.isStatic());
					astahOperation.setAbstract(op.isAbstract());
					for (Constraint c : op.getPreconditions()) {
						astahOperation.addPreCondition(c.getName());
					}
					for (Constraint c : op.getPostconditions()) {
						astahOperation.addPostCondition(c.getName());
					}
					Parameter retParam = UMLUtil.getReturnParameter(op);
					for (Parameter param : op.getOwnedParameters()) {
						try {
							if (param == retParam) {
								astahOperation
										.setQualifiedReturnTypeExpression(getQualifiedTypeExpression(param));
							} else {
								IParameter astahParam = bme.createParameter(
										astahOperation, param.getName(), "void");
								if (!param.getDirection().equals(
										ParameterDirectionKind.RETURN_LITERAL)) {
									astahParam.setDirection(param.getDirection()
											.getName());
								}
								astahParam
										.setQualifiedTypeExpression(getQualifiedTypeExpression(param));
								converteds.put(param, astahParam);
							}
						} catch (InvalidEditingException ex) {
							//do nothing, just use default type "void" when failed.
						}
					}
					converteds.put(op, astahOperation);
				}
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

	private String getQualifiedTypeExpression(Element element)
			throws ClassNotFoundException, InvalidEditingException,
			ProjectNotFoundException {
		StringBuilder sb = new StringBuilder();
		if (element instanceof TypedElement) {
			sb.append(getQualifiedType((TypedElement) element));
		}
		if (element instanceof MultiplicityElement) {
			sb.append(getMultiplicityString((MultiplicityElement) element));
		}
		return sb.toString();
	}

	private Object getMultiplicityString(MultiplicityElement me) {
		if (me.getLowerValue() != null && me.getUpperValue() != null
				&& !(me.getLower() == 1 && me.getUpper() == 1)) {
			return String.format("[%s..%s]",
					AstahUtil.toMultiplicityRange(me.getLower()),
					AstahUtil.toMultiplicityRange(me.getUpper()));
		}
		return "";
	}

	private Object getQualifiedType(TypedElement element) throws ClassNotFoundException, InvalidEditingException, ProjectNotFoundException {
		Type type = element.getType();
		if (type != null && converteds.get(type) instanceof IClass) {
			IClass astahClass = (IClass) converteds.get(type);
			return astahClass.getFullName("::");
		} else if (type instanceof PrimitiveType || type instanceof DataType) {
			String name = type.getName();
			if (name == null || name.equals("")) {
				name = XMILoader.getId(type);
			}
			if (Arrays.asList(AstahUtil.ASTAH_PRIMITIVES).contains(name)) {
				return name;
			}
			IClass astahPrimitive = AstahUtil.getOrCreatePrimitiveClass(name);
			if (astahPrimitive != null) {
				converteds.put(type, astahPrimitive);
				return astahPrimitive.getFullName("::");
			}
			return name;
		}
		return "int";
	}

	private INamedElement convertRelationship(Relationship rel)
			throws InvalidEditingException, ClassNotFoundException {

		INamedElement astahSource = (INamedElement) converteds.get(UMLUtil
				.getSource(rel));
		INamedElement astahTarget = (INamedElement) converteds.get(UMLUtil
				.getTarget(rel));
		if (astahSource == null || astahTarget == null) {
			return null;
		}
		BasicModelEditor bme = ModelEditorFactory.getBasicModelEditor();
		INamedElement astahRel = null;
		String name = UMLUtil.getName(rel);
		if (astahSource instanceof IClass && astahTarget instanceof IClass) {
			IClass astahSourceClass = (IClass) astahSource;
			IClass astahTargetClass = (IClass) astahTarget;
			if (rel instanceof Generalization) {
				astahRel = bme.createGeneralization(astahSourceClass, astahTargetClass, name);
			} else if (rel instanceof Realization) {
				astahRel = bme.createRealization(astahTargetClass, astahSourceClass, name);
			} else if (rel instanceof Usage) {
				astahRel = bme.createUsage(astahTargetClass, astahSourceClass, name);
			} else if (rel instanceof Association) {
				Property end0 = ((Association) rel).getMemberEnds().get(0);
				Property end1 = ((Association) rel).getMemberEnds().get(1);
				if (rel instanceof AssociationClass) {
					astahRel = bme.createAssociationClass(astahSourceClass, astahTargetClass,
							name, UMLUtil.getName(end0), UMLUtil.getName(end1));
				} else {
					astahRel = bme.createAssociation(astahSourceClass, astahTargetClass,
							name, UMLUtil.getName(end0), UMLUtil.getName(end1));
				}
				if (astahRel instanceof IAssociation) {
					IAttribute[] memberEnds = ((IAssociation) astahRel)
							.getMemberEnds();
					converteds.put(end0, memberEnds[0]);
					converteds.put(end1, memberEnds[1]);
				}
			}
		} else if (rel instanceof PackageImport || rel instanceof PackageMerge
				|| rel instanceof Dependency || rel instanceof InformationFlow) {
			astahRel = bme.createDependency(astahSource, astahTarget, name);
		}
		if (astahRel != null) {
			converteds.put(rel, astahRel);
		}
		return astahRel;
	}

	private void convertNormalModel(INamedElement model, Element parent)
			throws InvalidEditingException, ClassNotFoundException {
		if (model == null || parent == null) {
			return;
		}
		CommonModelConverter converter = new CommonModelConverter(helper, converteds, relationships);
		converter.convert(model,parent);
	}

}
