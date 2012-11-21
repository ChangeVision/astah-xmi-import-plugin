package com.change_vision.astah.xmi.convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Extend;
import org.eclipse.uml2.uml.Include;
import org.eclipse.uml2.uml.InformationFlow;
import org.eclipse.uml2.uml.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.model.ClassConverter;
import com.change_vision.astah.xmi.convert.model.DataTypeConverter;
import com.change_vision.astah.xmi.convert.model.InterfaceConverter;
import com.change_vision.astah.xmi.convert.model.ModelConverter;
import com.change_vision.astah.xmi.convert.model.PackageConverter;
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
    private final List<ModelConverter> converters = new ArrayList<ModelConverter>();

    public CommonModelConverter(ConvertHelper helper, Map<Element, IElement> converteds, Map<String, Relationship> relationships,AstahAPIUtil util) {
        this.converteds = converteds;
        this.relationships = relationships;
        this.converters.add(new PackageConverter(relationships, util, helper));
        this.converters.add(new ClassConverter(relationships, util, helper));
        this.converters.add(new InterfaceConverter(relationships, util, helper));
        this.converters.add(new DataTypeConverter(relationships, util, helper));
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
                    boolean remember = true;
                    if (uml2Element instanceof Association) {
                        Association association = (Association) uml2Element;
                        if (association.getMemberEnds().isEmpty()) {
                            remember = false;
                        }
                    }else if (uml2Element instanceof Extend ||
                            uml2Element instanceof Include ||
                            uml2Element instanceof InformationFlow){
                        remember = false;
                    }
                    if(remember){
                        rememberRelationship(uml2Element);
                    }
                } else {
                    
                    boolean converted = false;
                    for (ModelConverter converter : converters) {
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
