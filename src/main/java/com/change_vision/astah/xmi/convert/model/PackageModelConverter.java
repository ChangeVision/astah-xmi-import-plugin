package com.change_vision.astah.xmi.convert.model;

import static java.lang.String.format;

import java.util.Map;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.PackageMerge;
import org.eclipse.uml2.uml.Relationship;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.ConvertHelper;
import com.change_vision.astah.xmi.convert.XMILoader;
import com.change_vision.astah.xmi.convert.exception.NotForUseException;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IPackage;

public class PackageModelConverter implements ModelConverter{

    private UniqueNameCreator uniqueNameCreator = new UniqueNameCreator();
    private Map<String, Relationship> relationships;
    private AstahAPIUtil util;
    private ConvertHelper helper;

    public PackageModelConverter(Map<String, Relationship> relationships, AstahAPIUtil util,
            ConvertHelper helper) {
        this.relationships = relationships;
        this.util = util;
        this.helper = helper;
    }

    @Override
    public boolean accepts(Element element) {
        return element instanceof org.eclipse.uml2.uml.Package;
    }

    @Override
    public IClass convert(Element element) {
        throw new NotForUseException();
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
        for (PackageImport pi : packageElement.getPackageImports()) {
            relationships.put(XMILoader.getId(pi), pi);
        }
        for (PackageMerge pm : packageElement.getPackageMerges()) {
            relationships.put(XMILoader.getId(pm), pm);
        }
        helper.setStereotype(element, result);
        return result;
    }

}
