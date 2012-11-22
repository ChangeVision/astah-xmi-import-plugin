package com.change_vision.astah.xmi.convert.model;

import java.util.Map;

import org.eclipse.uml2.uml.Element;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.jude.api.inf.model.IElement;

public interface FeatureConverterFactory {

    public FeatureConverter createConverter(Map<Element, IElement> converteds, AstahAPIUtil util);
    
}
