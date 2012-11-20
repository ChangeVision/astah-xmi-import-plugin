package com.change_vision.astah.xmi.convert;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static util.UML2TestUtil.createClass;
import static util.UML2TestUtil.createEnumeration;

import java.util.HashMap;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Type;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IOperation;

public class FeatureConverterTest {
    
    @Mock
    private AstahAPIUtil util;

    @Mock
    private BasicModelEditor basicModelEditor;

    private HashMap<Element, IElement> converteds;
    
    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(util.getBasicModelEditor()).thenReturn(basicModelEditor);

        converteds = new HashMap<Element, IElement>();   
    }

    @Test(expected=IllegalArgumentException.class)
    public void convertWithNull() throws InvalidEditingException {
        FeatureConverter converter = new FeatureConverter(converteds, util);
        converter.convert(null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void convertCantRegisteredClassifier() throws Exception {
        Classifier classifier = mock(Classifier.class);
        
        FeatureConverter converter = new FeatureConverter(converteds, util);
        converter.convert(classifier);
    }
    
    @Test
    public void convertEmptyClass() throws Exception {
        Classifier classifier = createClass("classifier");
        IClass clazz = mock(IClass.class);
        converteds.put(classifier,clazz);
        
        FeatureConverter converter = new FeatureConverter(converteds, util);
        converter.convert(classifier);
        
        assertThat(converteds.size(),is(1));
    }
    
    @Test
    public void convertPropertyToAttribute() throws Exception {
        Class classifier = createClass("classifier");
        Class dummy = createClass("dummy");
        classifier.createOwnedAttribute("attr", dummy);
        
        IClass clazz = mock(IClass.class);
        converteds.put(classifier,clazz);
        IClass dummyClazz = mock(IClass.class);
        when(dummyClazz.getName()).thenReturn("dummy");
        when(dummyClazz.getFullName("::")).thenReturn("dummy");
        converteds.put(dummy,dummyClazz);
        
        IAttribute attribute = mock(IAttribute.class);
        when(basicModelEditor.createAttribute(clazz, "attr", "int")).thenReturn(attribute);
        
        FeatureConverter converter = new FeatureConverter(converteds, util);
        converter.convert(classifier);
        
        assertThat(converteds.size(),is(3));        
        verify(attribute).setQualifiedTypeExpression("dummy");
    }
    
    @Test
    @Ignore(value="対応必要")
    public void convertSameNamePropertyToAttribute() throws Exception {
        Class classifier = createClass("classifier");
        Class dummy = createClass("dummy");
        classifier.createOwnedAttribute("attr", dummy);
        classifier.createOwnedAttribute("attr", dummy);
        
        IClass clazz = mock(IClass.class);
        converteds.put(classifier,clazz);
        IClass dummyClazz = mock(IClass.class);
        when(dummyClazz.getName()).thenReturn("dummy");
        when(dummyClazz.getFullName("::")).thenReturn("dummy");
        converteds.put(dummy,dummyClazz);
        
        IAttribute attribute = mock(IAttribute.class);
        when(basicModelEditor.createAttribute(clazz, "attr", "int")).thenReturn(attribute);
        
        FeatureConverter converter = new FeatureConverter(converteds, util);
        converter.convert(classifier);
        
        assertThat(converteds.size(),is(4));
        verify(basicModelEditor).createAttribute(clazz, "attr", "int");
        verify(basicModelEditor).createAttribute(clazz, "attr0", "int");
        verify(attribute,times(2)).setQualifiedTypeExpression("dummy");        
    }
    
    @Test
    public void convertEnumerationToAttribute() throws Exception {
        Enumeration enumeration = createEnumeration("enumeration");
        enumeration.createOwnedLiteral("hoge");
        enumeration.createOwnedLiteral("fuga");

        IClass clazz = mock(IClass.class);
        converteds.put(enumeration,clazz);
        
        IAttribute attribute = mock(IAttribute.class);
        when(basicModelEditor.createAttribute(eq(clazz), anyString(), eq(clazz))).thenReturn(attribute);
        
        FeatureConverter converter = new FeatureConverter(converteds, util);
        converter.convert(enumeration);
        
        assertThat(converteds.size(),is(3));        
    }
    
    @Test
    public void convertOperation() throws Exception {
        Class classifier = createClass("classifier");
        EList<String> params = new BasicEList<String>();
        EList<Type> paramTypes = new BasicEList<Type>();
        classifier.createOwnedOperation("operation", params , paramTypes );

        IClass clazz = mock(IClass.class);
        converteds.put(classifier,clazz);

        IOperation operation = mock(IOperation.class);
        when(basicModelEditor.createOperation(eq(clazz), anyString(), eq("void"))).thenReturn(operation);

        FeatureConverter converter = new FeatureConverter(converteds, util);
        converter.convert(classifier);
        
        assertThat(converteds.size(),is(2));
    }

}
