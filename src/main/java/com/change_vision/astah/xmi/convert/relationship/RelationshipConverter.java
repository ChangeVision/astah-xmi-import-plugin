package com.change_vision.astah.xmi.convert.relationship;

import java.util.Map;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Relationship;

import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IElement;

public interface RelationshipConverter {
    
    public boolean accepts(Relationship relationship);
    
    public IElement convert(Map<Element, IElement> converteds, Relationship relationship) throws InvalidEditingException;

}
