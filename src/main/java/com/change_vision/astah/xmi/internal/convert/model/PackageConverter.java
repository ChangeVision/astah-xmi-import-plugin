package com.change_vision.astah.xmi.internal.convert.model;

import static java.lang.String.format;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.model.ClassifierConverter;
import com.change_vision.astah.xmi.convert.model.UniqueNameCreator;
import com.change_vision.astah.xmi.internal.convert.ConvertHelper;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IPackage;

public class PackageConverter implements ClassifierConverter {

    private UniqueNameCreator uniqueNameCreator = new UniqueNameCreator();
    private AstahAPIUtil util;
    private ConvertHelper helper;

    public PackageConverter(AstahAPIUtil util, ConvertHelper helper) {
        this.util = util;
        this.helper = helper;
    }

    @Override
    public boolean accepts(Element element) {
        return element instanceof Package;
    }

    @Override
    public IPackage convert(IElement parent, Element element) throws InvalidEditingException {
        if((parent instanceof IPackage) == false){
            throw new IllegalArgumentException(format("The parent must be instance of IPackage",parent));
        }
        IPackage parentPackage = (IPackage) parent;
        Package packageElement = (Package) element;
        String uniqueName = uniqueNameCreator.getUniqueName(parentPackage.getOwnedElements(), packageElement.getName());
        IPackage result = util.getBasicModelEditor().createPackage(parentPackage, uniqueName);
        helper.setStereotype(element, result);
        return result;
    }

}
