package com.change_vision.astah.xmi.internal.convert.relationship;

import static java.lang.String.format;

import java.util.Map;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.TemplateBinding;
import org.eclipse.uml2.uml.TemplateSignature;
import org.eclipse.uml2.uml.TemplateableElement;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.relationship.RelationshipConverter;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;

public class TemplateBindingConverter implements RelationshipConverter {

    private AstahAPIUtil util;
    private Map<Element, IElement> converteds;

    public TemplateBindingConverter(Map<Element, IElement> converteds, AstahAPIUtil util) {
        this.converteds = converteds;
        this.util = util;
    }

    @Override
    public boolean accepts(Relationship relationship) {
        return relationship instanceof TemplateBinding;
    }
    
    @Override
    public IElement convert(Relationship relationship) throws InvalidEditingException {
        if (!(relationship instanceof TemplateBinding)) {
            throw new IllegalArgumentException(format("target relathionship isn't TemplateBinding '%s'",relationship));
        }
        TemplateBinding binding = (TemplateBinding) relationship;
        IClass boundElement = getSource(binding);
        if(boundElement == null) return null;
        IClass template = getTarget(binding);
        if(template == null) return null;
        return getBasicModelEditor().createTemplateBinding(boundElement, template);
    }

    private IClass getSource(TemplateBinding binding) {
        TemplateableElement boundElemnt = binding.getBoundElement();
        if (boundElemnt instanceof Classifier) {
            Classifier clazz = (Classifier) boundElemnt;
            IElement sourceCandidate = converteds.get(clazz);
            if (sourceCandidate instanceof IClass) {
                return (IClass) sourceCandidate;
            }
        }
        return null;
    }
    
    private IClass getTarget(TemplateBinding binding) {
        TemplateSignature signature = binding.getSignature();
        if(signature == null) return null;
        TemplateableElement template = signature.getTemplate();
        if (template instanceof Classifier) {
            Classifier clazz = (Classifier) template;
            IElement sourceCandidate = converteds.get(clazz);
            if (sourceCandidate instanceof IClass) {
                return (IClass) sourceCandidate;
            }
        }
        return null;
    }

    
    private BasicModelEditor getBasicModelEditor() {
        return util.getBasicModelEditor();
    }

    
}
