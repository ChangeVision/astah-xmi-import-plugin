package com.change_vision.astah.xmi.convert.relationship;

import static java.lang.String.format;

import java.util.Map;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.Relationship;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.INamedElement;

public class PackageImportConverter implements RelationshipConverter {

    private Map<Element, IElement> converteds;
    private AstahAPIUtil util;

    public PackageImportConverter(Map<Element, IElement> converteds, AstahAPIUtil util) {
        this.converteds = converteds;
        this.util = util;
    }

    @Override
    public boolean accepts(Relationship relationship) {
        return relationship instanceof PackageImport;
    }
    
    @Override
    public IElement convert(Relationship relationship) throws InvalidEditingException {
        if ((relationship instanceof PackageImport) == false) {
            throw new IllegalArgumentException(format("target relathionship isn't PackageImport",relationship));
        }
        PackageImport packageImport = (PackageImport) relationship;
        INamedElement source = getSource(packageImport);
        if (source == null) return null;
        INamedElement target = getTarget(packageImport);
        if (target == null) return null;
        return getBasicModelEditor().createDependency(source, target, "");
    }

    private BasicModelEditor getBasicModelEditor() {
        return util.getBasicModelEditor();
    }

    private INamedElement getSource(PackageImport dependency) {
        NamedElement sourceElement = dependency.getImportedPackage();
        IElement source = converteds.get(sourceElement);
        if (source instanceof INamedElement) {
            return (INamedElement) source;
        }
        return null;
    }

    private INamedElement getTarget(PackageImport dependency) {
        NamedElement targetElement = dependency.getImportingNamespace();
        IElement target = converteds.get(targetElement);
        if (target instanceof INamedElement) {
            return (INamedElement) target;
        }
        return null;
    }

    
}
