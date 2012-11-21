package com.change_vision.astah.xmi.convert.model;

import static java.lang.String.format;

import java.util.Map;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.TemplateBinding;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.ConvertHelper;
import com.change_vision.astah.xmi.convert.XMILoader;
import com.change_vision.astah.xmi.convert.exception.NotForUseException;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IPackage;

public class InterfaceConverter implements ModelConverter {
    
    private UniqueNameCreator uniqueNameCreator = new UniqueNameCreator();
    private AstahAPIUtil apiUtil;
    private Map<String, Relationship> relationships;
    private ConvertHelper helper;

    public InterfaceConverter(Map<String, Relationship> relationships, AstahAPIUtil util, ConvertHelper helper) {
        this.apiUtil = util;
        this.relationships = relationships;
        this.helper = helper;
    }

    @Override
    public boolean accepts(Element element) {
        return element instanceof Interface;
    }

    @Override
    public IClass convert(Element element) {
        throw new NotForUseException();
    }
    
    @Override
    public IClass convert(IElement parent, Element element) throws InvalidEditingException {
        if ((element instanceof Interface) == false){
            throw new IllegalArgumentException(format("target element isn't Interface '%s'",element)); 
        }
        Interface target = (Interface) element;
        IClass result = null;
        if (parent instanceof IPackage) {
            IPackage parentPackage = (IPackage) parent;
            String name = uniqueNameCreator.getUniqueName(parentPackage.getOwnedElements(), target.getName());
            result = apiUtil.getBasicModelEditor().createClass(parentPackage, name);
        }
        if (parent instanceof IClass) {
            IClass parentClass = (IClass) parent;
            String name = uniqueNameCreator.getUniqueName(parentClass.getNestedClasses(), target.getName());
            result = apiUtil.getBasicModelEditor().createClass(parentClass, name);            
        }
        for (TemplateBinding binding : target.getTemplateBindings()) {
            relationships.put(XMILoader.getId(binding), binding);
        }
        helper.setStereotype(element, result);
        return result;
    }
    
}
