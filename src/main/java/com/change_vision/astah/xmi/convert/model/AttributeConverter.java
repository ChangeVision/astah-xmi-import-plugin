package com.change_vision.astah.xmi.convert.model;

import static java.lang.String.format;

import java.util.Arrays;
import java.util.Map;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.TypedElement;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.AstahUtil;
import com.change_vision.astah.xmi.convert.XMILoader;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.INamedElement;

public class AttributeConverter implements FeatureConverter {

    private static final String DEFAULT_ATTRIUBTE_TYPE = "int";
    
    private Map<Element, IElement> converteds;
    private AstahAPIUtil util;
    private UniqueNameCreator nameCreator = new UniqueNameCreator();

    public AttributeConverter(Map<Element, IElement> converteds, AstahAPIUtil util) {
        this.converteds = converteds;
        this.util = util;
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
        try {
            String expression = getQualifiedTypeExpression(prop);
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
    
    private String getQualifiedTypeExpression(Element element) throws InvalidEditingException {
        StringBuilder sb = new StringBuilder();
        if (element instanceof TypedElement) {
            sb.append(getQualifiedType((TypedElement) element));
        }
        if (element instanceof MultiplicityElement) {
            sb.append(getMultiplicityString((MultiplicityElement) element));
        }
        return sb.toString();
    }

    private Object getMultiplicityString(MultiplicityElement me) {
        if (me.getLowerValue() != null && me.getUpperValue() != null
                && !(me.getLower() == 1 && me.getUpper() == 1)) {
            return String.format("[%s..%s]",
                    AstahUtil.toMultiplicityRange(me.getLower()),
                    AstahUtil.toMultiplicityRange(me.getUpper()));
        }
        return "";
    }

    private Object getQualifiedType(TypedElement element) throws InvalidEditingException {
        Type type = element.getType();
        if (type != null && converteds.get(type) instanceof IClass) {
            IClass astahClass = (IClass) converteds.get(type);
            return astahClass.getFullName("::");
        } else if (type instanceof PrimitiveType || type instanceof DataType) {
            String name = type.getName();
            if (name == null || name.equals("")) {
                name = XMILoader.getId(type);
            }
            if (Arrays.asList(AstahUtil.ASTAH_PRIMITIVES).contains(name)) {
                return name;
            }
            IClass astahPrimitive = AstahUtil.getOrCreatePrimitiveClass(name);
            if (astahPrimitive != null) {
                converteds.put(type, astahPrimitive);
                return astahPrimitive.getFullName("::");
            }
            return name;
        }
        return DEFAULT_ATTRIUBTE_TYPE;
    }

    
    private BasicModelEditor getBasicModelEditor(){
        return util.getBasicModelEditor();
    }

}
