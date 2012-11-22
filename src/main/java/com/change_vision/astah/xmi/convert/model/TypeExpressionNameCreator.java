package com.change_vision.astah.xmi.convert.model;

import java.util.Arrays;
import java.util.Map;

import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.TypedElement;

import com.change_vision.astah.xmi.internal.convert.AstahUtil;
import com.change_vision.astah.xmi.internal.convert.XMILoader;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;

public class TypeExpressionNameCreator {
    
    private static final String DEFAULT_ATTRIUBTE_TYPE = "int";

    private Map<Element, IElement> converteds;
    
    public TypeExpressionNameCreator(Map<Element, IElement> converteds) {
        this.converteds = converteds;
    }

    public String getName(Element element) throws InvalidEditingException{
        return getQualifiedTypeExpression(element);
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
    
}
