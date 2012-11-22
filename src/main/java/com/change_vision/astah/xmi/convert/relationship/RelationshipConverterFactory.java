package com.change_vision.astah.xmi.convert.relationship;

import java.util.Map;

import org.eclipse.uml2.uml.Element;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.jude.api.inf.model.IElement;

public interface RelationshipConverterFactory {
    
    public RelationshipConverter craeteConverter(Map<Element, IElement> converteds, AstahAPIUtil util);

}
