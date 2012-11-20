package com.change_vision.astah.xmi.convert;

import java.util.Arrays;
import java.util.Map;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.InformationFlow;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Node;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.PackageMerge;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.TemplateBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IOperation;
import com.change_vision.jude.api.inf.model.IPackage;

public class CommonModelConverter {
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(CommonModelConverter.class);
    
    private ConvertHelper helper;
    private Map<Element, IElement> converteds;
    private Map<String, Relationship> relationships;
    private AstahAPIUtil util = new AstahAPIUtil();

    public CommonModelConverter(ConvertHelper helper, Map<Element, IElement> converteds, Map<String, Relationship> relationships) {
        this.helper = helper;
        this.converteds = converteds;
        this.relationships = relationships;
    }
    
    public void convert(INamedElement astahElement, Element parent) throws InvalidEditingException, ClassNotFoundException {
        if(parent == null){
            throw new IllegalArgumentException("parent is null.");            
        }
        if(astahElement == null){
            return;
        }
        for (Element uml2Element : parent.getOwnedElements()) {
            INamedElement newUMLModel = null;
            try {
                if (uml2Element instanceof NamedElement) {
                    INamedElement[] children = getElementsShouldBeUniqueName(
                            astahElement, uml2Element);
                    String name = AstahUtil.getUniqueName(children,
                            UMLUtil.getName(uml2Element));

                    if (uml2Element instanceof Package) {
                        IPackage packageElement = (IPackage) astahElement;
                        if (astahElement instanceof IPackage) {
                            newUMLModel = getBasicModelEditor().createPackage(packageElement,
                                    name);
                            for (PackageImport pi : ((Package) uml2Element)
                                    .getPackageImports()) {
                                rememberRelationship(pi);
                            }
                            for (PackageMerge pm : ((Package) uml2Element)
                                    .getPackageMerges()) {
                                rememberRelationship(pm);
                            }
                        }
                    } else if (uml2Element instanceof Classifier) {
                        Classifier classifier = (Classifier) uml2Element;
                        if (uml2Element instanceof Association) {
                            Association association = (Association) uml2Element;
                            if (!association.getMemberEnds().isEmpty()) {
                                rememberRelationship(uml2Element);
                            }
                            helper.setStereotype(uml2Element, newUMLModel);
                        } else if (uml2Element instanceof Class || uml2Element instanceof DataType
                                || uml2Element instanceof Interface) {
                            if ( uml2Element instanceof Behavior ) continue;
                            if ( uml2Element instanceof Node ) continue;
                            if ( uml2Element instanceof Component ) continue;
                            if ((uml2Element instanceof PrimitiveType || uml2Element instanceof DataType)
                                    && Arrays
                                            .asList(AstahUtil.ASTAH_PRIMITIVES)
                                            .contains(name)) {
                                continue;
                            }
                            if (astahElement instanceof IPackage) {
                                IPackage packageElement = (IPackage) astahElement;
                                newUMLModel = getBasicModelEditor().createClass(packageElement, name);
                            } else if (astahElement instanceof IClass) {
                                IClass classElement = (IClass) astahElement;
                                newUMLModel = getBasicModelEditor().createClass(classElement, name);
                            }
                            for (TemplateBinding binding : classifier.getTemplateBindings()) {
                                rememberRelationship(binding);
                            }
                            helper.setStereotype(uml2Element, newUMLModel);
                        } else {
                            java.lang.Class<? extends Element> baseClass = uml2Element.getClass();
                            String className = baseClass.getSimpleName();
                            logger.trace("doesn't target of CommonModelConverter:{}",className);
                        }
                    } else if ( uml2Element instanceof Dependency ||
                        uml2Element instanceof InformationFlow) {
                        rememberRelationship(uml2Element);
                    }
                } else if (uml2Element instanceof Generalization) {
                    rememberRelationship(uml2Element);
                }
            } catch (InvalidEditingException ex) {
                logger.error("Exception by InvalidEditing", ex);
                continue;
            }
            if (newUMLModel != null) {
                converteds.put(uml2Element, newUMLModel);
            }
            convert(newUMLModel, uml2Element);
        }

    }
    
    private void rememberRelationship(Element e) {
        if (e instanceof Relationship) {
            relationships.put(XMILoader.getId(e), (Relationship) e);
        }
    }

    private INamedElement[] getElementsShouldBeUniqueName(INamedElement owner,
            Element e) {
        INamedElement[] children = new INamedElement[0];
        if (owner instanceof IPackage) {
            children = ((IPackage) owner).getOwnedElements();
        } else if (owner instanceof IClass) {
            if (e instanceof Classifier) {
                children = ((IClass) owner).getNestedClasses();
            } else if (e instanceof Property || e instanceof EnumerationLiteral) {
                children = ((IClass) owner).getAttributes();
            }
        } else if (owner instanceof IOperation) {
            children = ((IOperation) owner).getParameters();
        }
        return children;
    }

    private BasicModelEditor getBasicModelEditor(){
        return util.getBasicModelEditor();
    }
    
    void setAstahAPIUtil(AstahAPIUtil util){
        this.util = util;
    }
}
