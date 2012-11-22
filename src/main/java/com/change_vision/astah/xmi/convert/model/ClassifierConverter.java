package com.change_vision.astah.xmi.convert.model;

import org.eclipse.uml2.uml.Element;

import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.INamedElement;

/**
 * convert UML2 Classifier element to Astah model interface
 */
public interface ClassifierConverter {
    
    /**
     * accepts UML2 element 
     * @param element UML2 element
     * @return true: accepted by the converter
     */
    public boolean accepts(Element element);
        
    /**
     * convert the element to Astah model
     * @param parent the model of parent in Astah
     * @param element the element of converted UML2 model
     * @return converted Astah model
     * @throws InvalidEditingException
     */
    public INamedElement convert(IElement parent, Element element) throws InvalidEditingException;

}
