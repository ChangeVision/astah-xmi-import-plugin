package com.change_vision.astah.xmi.convert.model;

import org.eclipse.uml2.uml.Element;

import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IElement;

/**
 * convert UML2 to Astah model interface
 * @param <TARGET> target model type
 */
public interface ModelConverter<TARGET extends IElement> {
    
    /**
     * accepts UML2 element 
     * @param element UML2 element
     * @return true: accepted by the converter
     */
    public boolean accepts(Element element);
    
    /**
     * convert the element to Astah model
     * @param element
     * @return
     * @throws InvalidEditingException
     */
    public TARGET convert(Element element) throws InvalidEditingException;
    
    public TARGET convert(IElement parent, Element element) throws InvalidEditingException;

}
