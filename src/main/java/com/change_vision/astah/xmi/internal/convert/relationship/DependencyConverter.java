package com.change_vision.astah.xmi.internal.convert.relationship;

import static java.lang.String.format;

import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Deployment;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Relationship;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.relationship.RelationshipConverter;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.INamedElement;

public class DependencyConverter implements RelationshipConverter {

    private AstahAPIUtil util;

    public DependencyConverter(AstahAPIUtil util) {
        this.util = util;
    }

    @Override
    public boolean accepts(Relationship relationship) {
        if ((relationship instanceof Dependency) == false )return false;
        if (relationship instanceof Deployment) return false;
        return true;
    }
    
    @Override
    public IElement convert(Map<Element, IElement> converteds, Relationship relationship) throws InvalidEditingException {
        if ((relationship instanceof Dependency) == false) {
            throw new IllegalArgumentException(format("target relathionship isn't Dependency",relationship));
        }
        Dependency dependency = (Dependency) relationship;
        INamedElement supplier = getSupplier(converteds, dependency);
        if (supplier == null) return null;
        INamedElement client = getClient(converteds, dependency);
        if (client == null) return null;
        return getBasicModelEditor().createDependency(supplier, client, getName(dependency));
    }

    private String getName(Dependency depdendency) {
        String name = depdendency.getName();
        if (name != null) return name;
        return "";
    }

    private BasicModelEditor getBasicModelEditor() {
        return util.getBasicModelEditor();
    }

    private INamedElement getSupplier(Map<Element, IElement> converteds, Dependency dependency) {
        EList<NamedElement> suppliers = dependency.getSuppliers();
        if( suppliers == null || suppliers.size() == 0) return null;
        NamedElement sourceElement = suppliers.get(0);
        IElement source = converteds.get(sourceElement);
        if (source instanceof INamedElement) {
            return (INamedElement) source;
        }
        return null;
    }

    private INamedElement getClient(Map<Element, IElement> converteds, Dependency dependency) {
        EList<NamedElement> clients = dependency.getClients();
        if( clients == null || clients.size() == 0) return null;
        NamedElement targetElement = clients.get(0);
        IElement target = converteds.get(targetElement);
        if (target instanceof INamedElement) {
            return (INamedElement) target;
        }
        return null;
    }
    
}
