package com.change_vision.astah.xmi.internal.convert.model;

import static java.lang.String.format;

import java.util.Arrays;
import java.util.Map;

import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.model.FeatureConverter;
import com.change_vision.astah.xmi.convert.model.TypeExpressionNameCreator;
import com.change_vision.astah.xmi.internal.convert.UMLUtil;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IOperation;
import com.change_vision.jude.api.inf.model.IParameter;

public class OperationConverter implements FeatureConverter {

    private static final String[] DIRECTIONS = new String[]{"in","out","inout"};

    private static final String DEFAULT_OPERATION_RETURN_TYPE = "void";

    private Map<Element, IElement> converteds;
    private AstahAPIUtil util;
    private TypeExpressionNameCreator typeNameCreator;

    public OperationConverter(Map<Element, IElement> converteds, AstahAPIUtil util) {
        this.converteds = converteds;
        this.util = util;
        this.typeNameCreator = new TypeExpressionNameCreator(converteds);
    }

    @Override
    public boolean accepts(Element element) {
        return (element instanceof Operation);
    }

    @Override
    public INamedElement convert(Element element,IClass astahClass) throws InvalidEditingException {
        if ((element instanceof Operation) == false) {
            throw new IllegalArgumentException(format("", element));
        }
        Operation op = (Operation) element;
        IOperation astahOperation = getBasicModelEditor().createOperation(astahClass,
                op.getName(), DEFAULT_OPERATION_RETURN_TYPE);
        astahOperation.setStatic(op.isStatic());
        astahOperation.setAbstract(op.isAbstract());
        for (Constraint c : op.getPreconditions()) {
            String condition = c.getName();
            if (condition != null && !condition.isEmpty()) {
                astahOperation.addPreCondition(condition);
            }
        }
        for (Constraint c : op.getPostconditions()) {
            String condition = c.getName();
            if (condition != null && !condition.isEmpty()) {
                astahOperation.addPostCondition(condition);
            }
        }
        for (Parameter param : op.getOwnedParameters()) {
                String expression = typeNameCreator.getName(param);
                if (isReturn(op,param)) {
                    astahOperation.setQualifiedReturnTypeExpression(expression);
                } else {
                    createParam(astahOperation, param, expression);
                }
        }
        return astahOperation;
    }

    private void createParam(IOperation astahOperation, Parameter param, String expression)
            throws InvalidEditingException {
        IParameter astahParam = getBasicModelEditor().createParameter(
                astahOperation, param.getName(), DEFAULT_OPERATION_RETURN_TYPE);
        String direction = param.getDirection().getName();
        if(Arrays.asList(DIRECTIONS).contains(direction)){
            astahParam.setDirection(direction);
        }
        try {
            astahParam.setQualifiedTypeExpression(expression);
        } catch (InvalidEditingException ex) {
            //do nothing, just use default type "void" when failed.
        }
        converteds.put(param, astahParam);
    }
    
    private boolean isReturn(Operation op,Parameter param) {
        Parameter retParam = UMLUtil.getReturnParameter(op);
        return retParam == param;
    }

    private BasicModelEditor getBasicModelEditor(){
        return util.getBasicModelEditor();
    }

}
