package com.change_vision.astah.xmi.convert.relationship;

import static java.lang.String.format;

import java.util.Map;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Relationship;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;

public class GeneralizationConverter implements RelationshipConverter {

    private AstahAPIUtil util;
    private Map<Element, IElement> converteds;

    public GeneralizationConverter(Map<Element, IElement> converteds, AstahAPIUtil util) {
        this.converteds = converteds;
        this.util = util;
    }

    @Override
    public boolean accepts(Relationship relationship) {
        return relationship instanceof Generalization;
    }
    
    @Override
    public IElement convert(Relationship relationship) throws InvalidEditingException {
        if ((relationship instanceof Generalization) == false) {
            throw new IllegalArgumentException(format("target relathionship isn't Generalization",relationship));
        }
        Generalization generalization = (Generalization) relationship;
        IClass source = getSource(generalization);
        if (source == null) return null;
        IClass target = getTarget(generalization);
        if (target == null) return null;
        return getBasicModelEditor().createGeneralization(source, target, "");
    }

    private BasicModelEditor getBasicModelEditor() {
        return util.getBasicModelEditor();
    }

    private IClass getSource(Generalization generalization) {
        Classifier sourceElement = generalization.getSpecific();
        IElement source = converteds.get(sourceElement);
        if (source instanceof IClass) {
            return (IClass)source;
        }
        return null;
    }

    private IClass getTarget(Generalization generalization) {
        Classifier targetElement = generalization.getGeneral();
        IElement target = converteds.get(targetElement);
        if (target instanceof IClass) {
            return (IClass) target;
        }
        return null;
    }
    
}
