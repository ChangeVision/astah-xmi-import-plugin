package com.change_vision.astah.xmi.convert.model;

import static java.lang.String.format;

import java.util.Map;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Property;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.INamedElement;

public class AttributeConverter implements FeatureConverter {

    private static final String DEFAULT_ATTRIUBTE_TYPE = "int";
    
    private AstahAPIUtil util;
    private UniqueNameCreator nameCreator = new UniqueNameCreator();
    private TypeExpressionNameCreator typeNameCreator;

    public AttributeConverter(Map<Element, IElement> converteds, AstahAPIUtil util) {
        this.util = util;
        this.typeNameCreator = new TypeExpressionNameCreator(converteds);
    }

    @Override
    public boolean accepts(Element element) {
        return (element instanceof Property);
    }

    @Override
    public INamedElement convert(Element element,IClass astahClass) throws InvalidEditingException {
        if ((element instanceof Property) == false) {
            throw new IllegalArgumentException(format("", element));
        }
        Property prop = (Property) element;
        if (prop.getAssociation() != null) return null;
        String name = prop.getName();
        name = nameCreator.getUniqueName(astahClass.getAttributes(), name);
        IAttribute astahAttribute = getBasicModelEditor().createAttribute(
                astahClass, name, DEFAULT_ATTRIUBTE_TYPE);
        String expression = typeNameCreator.getName(prop);
        try {
            astahAttribute.setQualifiedTypeExpression(expression);
        } catch (InvalidEditingException ex) {
            //do nothing, just use default type "int" when failed.
        }
        astahAttribute.setStatic(prop.isStatic());
        astahAttribute.setChangeable(prop.isReadOnly());
        if (prop.isComposite()) {
            astahAttribute.setComposite();
        } else if (prop.getAggregation().equals(
                AggregationKind.SHARED)) {
            astahAttribute.setAggregation();
        }
        astahAttribute.setDerived(prop.isDerived());
        astahAttribute.setInitialValue(prop.getDefault());
        return astahAttribute;
    }
        
    private BasicModelEditor getBasicModelEditor(){
        return util.getBasicModelEditor();
    }

}
