package com.change_vision.astah.xmi.convert;

import java.util.Arrays;
import java.util.Map;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.TypedElement;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IOperation;
import com.change_vision.jude.api.inf.model.IParameter;

public class FeatureConverter {

    private static final String DEFAULT_OPERATION_RETURN_TYPE = "void";
    private static final String DEFAULT_ATTRIUBTE_TYPE = "int";
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
            if (prop.getAssociation() == null) {
                IAttribute astahAttribute = getBasicModelEditor().createAttribute(
                        astahClass, prop.getName(), DEFAULT_ATTRIUBTE_TYPE);
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
                converteds.put(prop, astahAttribute);
            }
        }
        for (Operation op : cls.getOperations()) {
            IOperation astahOperation = getBasicModelEditor().createOperation(astahClass,
                    op.getName(), DEFAULT_OPERATION_RETURN_TYPE);
            astahOperation.setStatic(op.isStatic());
            astahOperation.setAbstract(op.isAbstract());
            for (Constraint c : op.getPreconditions()) {
                astahOperation.addPreCondition(c.getName());
            }
            for (Constraint c : op.getPostconditions()) {
                astahOperation.addPostCondition(c.getName());
            }
            Parameter retParam = UMLUtil.getReturnParameter(op);
            for (Parameter param : op.getOwnedParameters()) {
                try {
                    if (param == retParam) {
                        astahOperation
                                .setQualifiedReturnTypeExpression(getQualifiedTypeExpression(param));
                    } else {
                        IParameter astahParam = getBasicModelEditor().createParameter(
                                astahOperation, param.getName(), DEFAULT_OPERATION_RETURN_TYPE);
                        if (!param.getDirection().equals(
                                ParameterDirectionKind.RETURN_LITERAL)) {
                            astahParam.setDirection(param.getDirection()
                                    .getName());
                        }
                        astahParam
                                .setQualifiedTypeExpression(getQualifiedTypeExpression(param));
                        converteds.put(param, astahParam);
                    }
                } catch (InvalidEditingException ex) {
                    //do nothing, just use default type "void" when failed.
                }
            }
            converteds.put(op, astahOperation);
        }
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
        return apiUtil.getBasicModelEditor();
    }

}
