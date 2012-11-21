package com.change_vision.astah.xmi.convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.PackageMerge;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.model.ClassModelConverter;
import com.change_vision.astah.xmi.convert.model.DataTypeModelConverter;
import com.change_vision.astah.xmi.convert.model.InterfaceModelConverter;
import com.change_vision.astah.xmi.convert.model.ModelConverter;
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
    private AstahAPIUtil util;
    private final List<ModelConverter<IClass>> converters = new ArrayList<ModelConverter<IClass>>();

    public CommonModelConverter(ConvertHelper helper, Map<Element, IElement> converteds, Map<String, Relationship> relationships,AstahAPIUtil util) {
        this.helper = helper;
        this.converteds = converteds;
        this.relationships = relationships;
        this.util = util;
        this.converters.add(new ClassModelConverter(relationships, util, helper));
        this.converters.add(new InterfaceModelConverter(relationships, util, helper));
        this.converters.add(new DataTypeModelConverter(relationships, util, helper));
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
                        if (uml2Element instanceof Association) {
                            Association association = (Association) uml2Element;
                            if (!association.getMemberEnds().isEmpty()) {
                                rememberRelationship(uml2Element);
                            }
                            helper.setStereotype(uml2Element, newUMLModel);
                        } else{
                            boolean converted = false;
                            for (ModelConverter<IClass> converter : converters) {
                                if(converter.accepts(uml2Element)){
                                    newUMLModel = converter.convert(astahElement, uml2Element);
                                    if(newUMLModel != null) converted = true;
                                }
                            }
                            if(converted) {
                                java.lang.Class<? extends Element> baseClass = uml2Element.getClass();
                                String className = baseClass.getSimpleName();
                                logger.trace("doesn't target of CommonModelConverter:{}",className);
                            }
                        }
                    } else if ( uml2Element instanceof Dependency) {
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
    
}
