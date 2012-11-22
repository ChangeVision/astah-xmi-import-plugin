package com.change_vision.astah.xmi.internal.convert.relationship;

import static java.lang.String.format;

import java.util.Map;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.Type;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.relationship.RelationshipConverter;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IAssociation;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;

public class AssociationConverter implements RelationshipConverter {

    private Map<Element, IElement> converteds;
    private AstahAPIUtil util;

    public AssociationConverter(Map<Element, IElement> converteds, AstahAPIUtil util) {
        this.converteds = converteds;
        this.util = util;
    }

    @Override
    public boolean accepts(Relationship relationship) {
        if ((relationship instanceof Association) == false) return false;
        Association association = (Association) relationship;
        return association.getMemberEnds().isEmpty() == false;
    }
    
    @Override
    public IElement convert(Relationship relationship) throws InvalidEditingException {
        if (accepts(relationship) == false) {
            throw new IllegalArgumentException(format("target relathionship isn't accepted.",relationship));
        }
        Association association = (Association) relationship;
        Property sourceProperty = getSourceProperty(association);
        IClass source = getSource(sourceProperty);
        if (source == null) return null;
        Property targetProperty = getTargetProperty(association);
        IClass target = getTarget(targetProperty);
        if (target == null) return null;
        IAssociation result = getBasicModelEditor().createAssociation(source, target, getName(association),getName(sourceProperty),getName(targetProperty));
        IAttribute[] memberEnds = result.getMemberEnds();
        converteds.put(sourceProperty, memberEnds[0]);
        converteds.put(targetProperty, memberEnds[1]);
        return result;
    }
    
    private String getName(NamedElement element) {
        String name = element.getName();
        if (name != null) return name;
        return "";
    }

    private BasicModelEditor getBasicModelEditor() {
        return util.getBasicModelEditor();
    }

    private IClass getSource(Property property) {
        Type sourceElement = property.getType();
        IElement source = converteds.get(sourceElement);
        if (source instanceof IClass) {
            return (IClass)source;
        }
        return null;
    }

    private Property getSourceProperty(Association association) {
        Property sourceProperty = association.getMemberEnds().get(0);
        return sourceProperty;
    }

    private IClass getTarget(Property targetProperty) {
        Type targetElement = targetProperty.getType();
        IElement target = converteds.get(targetElement);
        if (target instanceof IClass) {
            return (IClass) target;
        }
        return null;
    }

    private Property getTargetProperty(Association association) {
        return association.getMemberEnds().get(1);
    }
    
}
