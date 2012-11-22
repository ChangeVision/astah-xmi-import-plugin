package com.change_vision.astah.xmi.convert.relationship;

import static java.lang.String.format;

import java.util.Map;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.Usage;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;

public class UsageConverter implements RelationshipConverter {

    private AstahAPIUtil util;
    private Map<Element, IElement> converteds;

    public UsageConverter(Map<Element, IElement> converteds, AstahAPIUtil util) {
        this.converteds = converteds;
        this.util = util;
    }

    @Override
    public boolean accepts(Relationship relationship) {
        return relationship instanceof Usage;
    }

    @Override
    public IElement convert(Relationship relationship) throws InvalidEditingException {
        if ((relationship instanceof Usage) == false) {
            throw new IllegalArgumentException(format("target relathionship isn't Usage",relationship));
        }
        Usage usage = (Usage) relationship;
        IClass supplier = getSupplier(usage);
        if (supplier == null) return null;
        IClass client = getClient(usage);
        if (client == null) return null;
        return getBasicModelEditor().createUsage(client, supplier, getName(usage));
    }

    private String getName(Usage usage) {
        String name = usage.getName();
        if (name != null) return name;
        return "";
    }

    private BasicModelEditor getBasicModelEditor() {
        return util.getBasicModelEditor();
    }

    private IClass getSupplier(Usage usage) {
        NamedElement sourceElement = usage.getSuppliers().get(0);
        IElement source = converteds.get(sourceElement);
        if (source instanceof IClass) {
            return (IClass) source;
        }
        return null;
    }

    private IClass getClient(Usage usage) {
        NamedElement targetElement = usage.getClients().get(0);
        IElement target = converteds.get(targetElement);
        if (target instanceof IClass) {
            return (IClass) target;
        }
        return null;
    }

}
