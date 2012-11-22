package com.change_vision.astah.xmi.internal.convert.relationship;

import static java.lang.String.format;

import java.util.Map;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Realization;
import org.eclipse.uml2.uml.Relationship;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.relationship.RelationshipConverter;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;

public class RealizationConverter implements RelationshipConverter {

    private AstahAPIUtil util;
    private Map<Element, IElement> converteds;

    public RealizationConverter(Map<Element, IElement> converteds, AstahAPIUtil util) {
        this.converteds = converteds;
        this.util = util;
    }

    @Override
    public boolean accepts(Relationship relationship) {
        return relationship instanceof Realization;
    }
    
    @Override
    public IElement convert(Relationship relationship) throws InvalidEditingException {
        if ((relationship instanceof Realization) == false) {
            throw new IllegalArgumentException(format("target relathionship isn't Realization",relationship));
        }
        Realization realization = (Realization) relationship;
        IClass supplier = getSupplier(realization);
        if (supplier == null) return null;
        IClass client = getClient(realization);
        if (client == null) return null;
        return getBasicModelEditor().createRealization(client, supplier, getName(realization));
    }

    private String getName(Realization realization) {
        String name = realization.getName();
        if(name != null) return name;
        return "";
    }

    private BasicModelEditor getBasicModelEditor() {
        return util.getBasicModelEditor();
    }

    private IClass getSupplier(Realization realization) {
        NamedElement sourceElement = realization.getSuppliers().get(0);
        IElement source = converteds.get(sourceElement);
        if (source instanceof IClass) {
            return (IClass)source;
        }
        return null;
    }

    private IClass getClient(Realization realization) {
        NamedElement targetElement = realization.getClients().get(0);
        IElement target = converteds.get(targetElement);
        if (target instanceof IClass) {
            return (IClass) target;
        }
        return null;
    }

}
