package com.change_vision.astah.xmi.convert;

import java.util.Map;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Relationship;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.relationship.RelationshipConverter;
import com.change_vision.astah.xmi.convert.relationship.RelationshipConverters;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.INamedElement;

public class RelationConverter {

    private Map<Element, IElement> converteds;
    private RelationshipConverters converters;

    public RelationConverter(Map<Element, IElement> converteds, AstahAPIUtil util) {
        this.converteds = converteds;
        this.converters = new RelationshipConverters(converteds,util);
    }
    
    public INamedElement convert(Relationship rel) throws InvalidEditingException{
        for (RelationshipConverter converter : converters.getConverters()) {
            if (converter.accepts(rel)) {
                IElement converted = converter.convert(rel);
                if(converted != null) converteds.put(rel, converted);
                return (INamedElement)converted;                
            }
        }
        return null;
    }
    
}
