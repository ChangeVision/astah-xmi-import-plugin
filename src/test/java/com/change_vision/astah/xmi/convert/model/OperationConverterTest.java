package com.change_vision.astah.xmi.convert.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static util.UML2TestUtil.createClass;
import static util.UML2TestUtil.createOperation;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Type;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.ConvertHelper;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IOperation;
import com.change_vision.jude.api.inf.model.IParameter;

public class OperationConverterTest {
    @Mock
    private AstahAPIUtil util;

    @Mock
    private BasicModelEditor basicModelEditor;
    
    @Mock
    private ConvertHelper helper;

    private Map<Element, IElement> converteds;

    private OperationConverter converter;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(util.getBasicModelEditor()).thenReturn(basicModelEditor);
        this.converteds = new HashMap<Element, IElement>();
        converter = new OperationConverter(converteds, util);
    }

    @Test
    public void rejectWithNull() {
        boolean result = converter.accepts(null);
        assertThat(result,is(false));
    }
    
    @Test
    public void acceptOperation() throws Exception {
        Operation operation = createOperation("dummy");
        boolean result = converter.accepts(operation);
        assertThat(result,is(true));        
    }
    
    @Test
    public void convertOperation() throws Exception {
        Class classifier = createClass("classifier");
        EList<String> params = new BasicEList<String>();
        EList<Type> paramTypes = new BasicEList<Type>();
        Operation operation = classifier.createOwnedOperation("operation", params , paramTypes );

        IClass clazz = mock(IClass.class);
        converteds.put(classifier,clazz);

        IOperation created = mock(IOperation.class);
        when(basicModelEditor.createOperation(eq(clazz), anyString(), eq("void"))).thenReturn(created);

        converter.convert(operation,clazz);
        
        verify(basicModelEditor).createOperation(eq(clazz), anyString(), eq("void"));
    }
    
    @Test
    public void convertOperationReturnType() throws Exception {
        Class classifier = createClass("classifier");
        EList<String> params = new BasicEList<String>();
        EList<Type> paramTypes = new BasicEList<Type>();
        Class returnType = createClass("returnType");
        Operation operation = classifier.createOwnedOperation("operation", params , paramTypes, returnType);

        IClass clazz = mock(IClass.class);
        converteds.put(classifier,clazz);
        IClass returnClazz = mock(IClass.class);
        when(returnClazz.getFullName("::")).thenReturn("returnType");
        converteds.put(returnType,returnClazz);

        IOperation created = mock(IOperation.class);
        when(basicModelEditor.createOperation(eq(clazz), anyString(), eq("void"))).thenReturn(created);

        INamedElement converted = converter.convert(operation,clazz);
        assertThat(converted,is(notNullValue()));
        verify(created).setQualifiedReturnTypeExpression("returnType");
    }
    
    @Test
    public void convertOperationWithParameter() throws Exception {
        Class classifier = createClass("classifier");

        EList<String> params = new BasicEList<String>();
        params.add("param");

        EList<Type> paramTypes = new BasicEList<Type>();
        paramTypes.add(classifier);
        Operation operation = classifier.createOwnedOperation("operation", params , paramTypes);

        IClass clazz = mock(IClass.class);
        converteds.put(classifier,clazz);

        IOperation created = mock(IOperation.class);
        when(basicModelEditor.createOperation(eq(clazz), anyString(), eq("void"))).thenReturn(created);
        
        IParameter parameter = mock(IParameter.class);
        when(basicModelEditor.createParameter(created, "param", "void")).thenReturn(parameter);

        INamedElement converted = converter.convert(operation,clazz);
        assertThat(converted,is(notNullValue()));
    }

}
