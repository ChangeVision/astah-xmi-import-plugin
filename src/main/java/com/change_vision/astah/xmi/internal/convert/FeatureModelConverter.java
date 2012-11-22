package com.change_vision.astah.xmi.internal.convert;

import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.model.FeatureConverter;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.INamedElement;

public class FeatureModelConverter {

    private Map<Element, IElement> converteds;
    private AstahAPIUtil apiUtil;
    private FeatureConverters converters;

    public FeatureModelConverter(Map<Element, IElement> converteds, AstahAPIUtil apiUtil) {
        this.converteds = converteds;
        this.apiUtil = apiUtil;
        this.converters =  new FeatureConverters(converteds, apiUtil);
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
        EList<Element> ownedElements = cls.getOwnedElements();
        for (Element element : ownedElements) {
            for (FeatureConverter converter : converters.getConverters()) {
                if(converter.accepts(element)){
                    INamedElement convert = converter.convert(element, astahClass);
                    if (convert != null) {
                        converteds.put(element, convert);
                        break;
                    }
                }
            }
        }
    }
    
    private BasicModelEditor getBasicModelEditor(){
        return apiUtil.getBasicModelEditor();
    }

}
