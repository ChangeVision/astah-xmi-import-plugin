package com.change_vision.astah.xmi.internal.convert.relationship;

import static java.lang.String.format;

import java.util.Map;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.Usage;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.relationship.RelationshipConverter;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.INamedElement;

public class UsageConverter implements RelationshipConverter {

    private AstahAPIUtil util;

    public UsageConverter(AstahAPIUtil util) {
        this.util = util;
    }

    @Override
    public boolean accepts(Relationship relationship) {
        return relationship instanceof Usage;
    }

    @Override
    public IElement convert(Map<Element, IElement> converteds, Relationship relationship) throws InvalidEditingException {
        if ((relationship instanceof Usage) == false) {
            throw new IllegalArgumentException(format("target relathionship isn't Usage",relationship));
        }
        Usage usage = (Usage) relationship;
        INamedElement supplier = getSupplier(converteds, usage);
        if (supplier == null) return null;
        INamedElement client = getClient(converteds, usage);
        if (client == null) return null;
        if ((supplier instanceof IClass) && (client instanceof IClass)) {
            IClass classSupplier = (IClass) supplier;
            IClass classClient = (IClass) client;
            return getBasicModelEditor().createUsage(classClient, classSupplier, getName(usage));
        }
        return getBasicModelEditor().createDependency(supplier, client, getName(usage));
    }

    private String getName(Usage usage) {
        String name = usage.getName();
        if (name != null) return name;
        return "";
    }

    private BasicModelEditor getBasicModelEditor() {
        return util.getBasicModelEditor();
    }

    private INamedElement getSupplier(Map<Element, IElement> converteds, Usage usage) {
        NamedElement sourceElement = usage.getSuppliers().get(0);
        IElement source = converteds.get(sourceElement);
        if (source instanceof INamedElement) {
            return (INamedElement) source;
        }
        return null;
    }

    private INamedElement getClient(Map<Element, IElement> converteds, Usage usage) {
        NamedElement targetElement = usage.getClients().get(0);
        IElement target = converteds.get(targetElement);
        if (target instanceof INamedElement) {
            return (INamedElement) target;
        }
        return null;
    }

}
