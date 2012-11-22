package com.change_vision.astah.xmi.convert.model;

import org.eclipse.uml2.uml.Element;

import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.INamedElement;

/**
 * convert UML2 to Astah model interface
 */
public interface FeatureConverter {
    
    /**
     * accepts UML2 element 
     * @param element UML2 element
     * @return true: accepted by the converter
     */
    public boolean accepts(Element element);
    
    /**
     * convert the element to Astah model
     * @param element
     * @param astahClass owner astahClass
     * @return
     * @throws InvalidEditingException
     */
    public INamedElement convert(Element element,IClass astahClass) throws InvalidEditingException;
    
}
