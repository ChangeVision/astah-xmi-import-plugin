package com.change_vision.astah.xmi.internal.convert.relationship;

import static java.lang.String.format;

import java.util.Map;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.PackageMerge;
import org.eclipse.uml2.uml.Relationship;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.relationship.RelationshipConverter;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.INamedElement;

public class PackageMergeConverter implements RelationshipConverter {

    private AstahAPIUtil util;

    public PackageMergeConverter(AstahAPIUtil util) {
        this.util = util;
    }

    @Override
    public boolean accepts(Relationship relationship) {
        return relationship instanceof PackageMerge;
    }
    
    @Override
    public IElement convert(Map<Element, IElement> converteds, Relationship relationship) throws InvalidEditingException {
        if ((relationship instanceof PackageMerge) == false) {
            throw new IllegalArgumentException(format("target relathionship isn't PackageImport",relationship));
        }
        PackageMerge packageImport = (PackageMerge) relationship;
        INamedElement source = getSource(converteds, packageImport);
        if (source == null) return null;
        INamedElement target = getTarget(converteds, packageImport);
        if (target == null) return null;
        return getBasicModelEditor().createDependency(source, target, "");
    }

    private BasicModelEditor getBasicModelEditor() {
        return util.getBasicModelEditor();
    }

    private INamedElement getSource(Map<Element, IElement> converteds, PackageMerge dependency) {
        NamedElement sourceElement = dependency.getMergedPackage();
        IElement source = converteds.get(sourceElement);
        if (source instanceof INamedElement) {
            return (INamedElement) source;
        }
        return null;
    }

    private INamedElement getTarget(Map<Element, IElement> converteds, PackageMerge dependency) {
        NamedElement targetElement = dependency.getReceivingPackage();
        IElement target = converteds.get(targetElement);
        if (target instanceof INamedElement) {
            return (INamedElement) target;
        }
        return null;
    }
    
}
