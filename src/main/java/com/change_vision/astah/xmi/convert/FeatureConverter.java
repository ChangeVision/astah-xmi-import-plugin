package com.change_vision.astah.xmi.convert;

import java.util.Map;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.model.AttributeConverter;
import com.change_vision.astah.xmi.convert.model.OperationConverter;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.INamedElement;

public class FeatureConverter {

    private Map<Element, IElement> converteds;
    private AstahAPIUtil apiUtil;

    public FeatureConverter(Map<Element, IElement> converteds, AstahAPIUtil apiUtil) {
        this.converteds = converteds;
        this.apiUtil = apiUtil;
    }

    public void convert(Classifier cls) throws InvalidEditingException {
        if(cls == null) throw new IllegalArgumentException("Target Classifier is null.");
        IClass astahClass = (IClass) converteds.get(cls);
        if(astahClass == null) throw new IllegalArgumentException("converted class is not found.");
        
        if (cls instanceof Enumeration) {
            for (EnumerationLiteral literal : ((Enumeration) cls)
                    .getOwnedLiterals()) {
                IAttribute astahAttribute = getBasicModelEditor().createAttribute(
                        astahClass, literal.getName(), astahClass);
                converteds.put(literal, astahAttribute);
            }
        }
        for (Property prop : cls.getAttributes()) {
            AttributeConverter converter = new AttributeConverter(converteds, apiUtil);
            INamedElement convert = converter.convert(prop, astahClass);
            if (convert != null) {
                converteds.put(prop, convert);                
            }
        }
        for (Operation op : cls.getOperations()) {
            OperationConverter converter = new OperationConverter(converteds, apiUtil);
            INamedElement convert = converter.convert(op, astahClass);
            if (convert != null) {
                converteds.put(op, convert);                
            }
        }
    }
    
    private BasicModelEditor getBasicModelEditor(){
        return apiUtil.getBasicModelEditor();
    }

}
