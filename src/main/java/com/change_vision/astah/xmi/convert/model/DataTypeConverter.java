package com.change_vision.astah.xmi.convert.model;

import static java.lang.String.format;

import java.util.Arrays;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interface;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.AstahUtil;
import com.change_vision.astah.xmi.convert.ConvertHelper;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IPackage;

public class DataTypeConverter implements ClassifierConverter {

    private UniqueNameCreator uniqueNameCreator = new UniqueNameCreator();
    private AstahAPIUtil apiUtil;
    private ConvertHelper helper;

    public DataTypeConverter(AstahAPIUtil util, ConvertHelper helper) {
        this.apiUtil = util;
        this.helper = helper;
    }

    @Override
    public boolean accepts(Element element) {
        return element instanceof DataType;
    }

    @Override
    public IClass convert(IElement parent, Element element) throws InvalidEditingException {
        if (element instanceof DataType == false) {
            throw new IllegalArgumentException(format("target element isn't DataType '%s'",element)); 
        }
        Classifier target = (Classifier) element;
        String name = target.getName();
        if(Arrays.asList(AstahUtil.ASTAH_PRIMITIVES).contains(name)) return null;
        IClass results = null;
        if (parent instanceof IPackage) {
            IPackage parentPackage = (IPackage) parent;
            name = uniqueNameCreator.getUniqueName(parentPackage.getOwnedElements(), name);
            results = apiUtil.getBasicModelEditor().createClass(parentPackage, name);
        }
        if (parent instanceof IClass) {
            IClass parentClass = (IClass) parent;
            name = target.getName();
            if(element instanceof Class || element instanceof Interface){
                name = uniqueNameCreator.getUniqueName(parentClass.getNestedClasses(), name);
            }else if(element instanceof DataType){
                name = uniqueNameCreator.getUniqueName(parentClass.getAttributes(), name);                
            }
            results = apiUtil.getBasicModelEditor().createClass(parentClass, name);            
        }
        helper.setStereotype(element, results);
        return results;
    }

}
