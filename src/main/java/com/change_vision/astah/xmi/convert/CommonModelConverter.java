package com.change_vision.astah.xmi.convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.model.ClassConverter;
import com.change_vision.astah.xmi.convert.model.DataTypeConverter;
import com.change_vision.astah.xmi.convert.model.InterfaceConverter;
import com.change_vision.astah.xmi.convert.model.ModelConverter;
import com.change_vision.astah.xmi.convert.model.PackageConverter;
import com.change_vision.astah.xmi.convert.relationship.AssociationClassConverter;
import com.change_vision.astah.xmi.convert.relationship.AssociationConverter;
import com.change_vision.astah.xmi.convert.relationship.DependencyConverter;
import com.change_vision.astah.xmi.convert.relationship.GeneralizationConverter;
import com.change_vision.astah.xmi.convert.relationship.RealizationConverter;
import com.change_vision.astah.xmi.convert.relationship.RelationshipConverter;
import com.change_vision.astah.xmi.convert.relationship.UsageConverter;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.INamedElement;

public class CommonModelConverter {
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(CommonModelConverter.class);
    
    private Map<Element, IElement> converteds;
    private Map<String, Relationship> relationships;
    private final List<RelationshipConverter> relationshipConverters = new ArrayList<RelationshipConverter>();
    private final List<ModelConverter> modelConverters = new ArrayList<ModelConverter>();

    public CommonModelConverter(ConvertHelper helper, Map<Element, IElement> converteds, Map<String, Relationship> relationships,AstahAPIUtil util) {
        this.converteds = converteds;
        this.relationships = relationships;
        
        this.relationshipConverters.add(new GeneralizationConverter(util, helper));
        this.relationshipConverters.add(new RealizationConverter(util, helper));
        this.relationshipConverters.add(new UsageConverter(util, helper));
        this.relationshipConverters.add(new DependencyConverter(util, helper));
        this.relationshipConverters.add(new AssociationConverter(util, helper));
        this.relationshipConverters.add(new AssociationClassConverter(util, helper));
        
        this.modelConverters.add(new PackageConverter(relationships, util, helper));
        this.modelConverters.add(new ClassConverter(relationships, util, helper));
        this.modelConverters.add(new InterfaceConverter(relationships, util, helper));
        this.modelConverters.add(new DataTypeConverter(relationships, util, helper));
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
                if (uml2Element instanceof Relationship){
//                    boolean remember = true;
//                    if (uml2Element instanceof Association) {
//                        Association association = (Association) uml2Element;
//                        if (association.getMemberEnds().isEmpty()) {
//                            remember = false;
//                        }
//                    }else if (uml2Element instanceof Extend ||
//                            uml2Element instanceof Include ||
//                            uml2Element instanceof InformationFlow){
//                        remember = false;
//                    }
//                    if(remember){
//                        rememberRelationship(uml2Element);
//                    }
                    Relationship relationship = (Relationship) uml2Element;
                    boolean converted = false;
                    for (RelationshipConverter converter : relationshipConverters){
                        if (converter.accepts(relationship)) {
                            rememberRelationship(relationship);
                            converted = true;
                            break;
                        }
                    }
                    if(converted == false) {
                        java.lang.Class<? extends Element> baseClass = uml2Element.getClass();
                        String className = baseClass.getSimpleName();
                        logger.trace("doesn't target of CommonModelConverter:{}",className);
                    }
                } else {
                    boolean converted = false;
                    for (ModelConverter converter : modelConverters) {
                        if(converter.accepts(uml2Element)){
                            newUMLModel = converter.convert(astahElement, uml2Element);
                            if(newUMLModel != null){
                                converted = true;
                                break;
                            }
                        }
                    }
                    if(converted == false) {
                        java.lang.Class<? extends Element> baseClass = uml2Element.getClass();
                        String className = baseClass.getSimpleName();
                        logger.trace("doesn't target of CommonModelConverter:{}",className);
                    }
                    
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
    
}
