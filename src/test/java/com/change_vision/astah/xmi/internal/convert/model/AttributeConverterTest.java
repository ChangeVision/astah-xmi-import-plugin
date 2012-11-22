package com.change_vision.astah.xmi.internal.convert.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static util.UML2TestUtil.createClass;
import static util.UML2TestUtil.createProperty;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Property;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.internal.convert.ConvertHelper;
import com.change_vision.astah.xmi.internal.convert.model.AttributeConverter;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.INamedElement;

public class AttributeConverterTest {
    
    @Mock
    private AstahAPIUtil util;

    @Mock
    private BasicModelEditor basicModelEditor;
    
    @Mock
    private ConvertHelper helper;

    private Map<Element, IElement> converteds;

    private AttributeConverter converter;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(util.getBasicModelEditor()).thenReturn(basicModelEditor);
        this.converteds = new HashMap<Element, IElement>();
        converter = new AttributeConverter(converteds, util);
    }

    @Test
    public void rejectWithNull() {
        boolean result = converter.accepts(null);
        assertThat(result,is(false));
    }
    
    @Test
    public void acceptProperty() throws Exception {
        Property property = createProperty("dummy");
        boolean result = converter.accepts(property);
        assertThat(result,is(true));        
    }
    
    @Test
    public void convertPropertyToAttribute() throws Exception {
        Class classifier = createClass("classifier");
        Class dummy = createClass("dummy");
        Property property = classifier.createOwnedAttribute("attr", dummy);
        
        IClass clazz = mock(IClass.class);
        converteds.put(classifier,clazz);
        IClass dummyClazz = mock(IClass.class);
        when(dummyClazz.getName()).thenReturn("dummy");
        when(dummyClazz.getFullName("::")).thenReturn("dummy");
        converteds.put(dummy,dummyClazz);
        
        IAttribute attribute = mock(IAttribute.class);
        when(basicModelEditor.createAttribute(clazz, "attr", "int")).thenReturn(attribute);
        
        INamedElement attributes = converter.convert(property,clazz);
        assertThat(attributes,is(notNullValue()));
        verify(attribute).setQualifiedTypeExpression("dummy");
    }

    @Test
    public void convertSameNamePropertyToAttribute() throws Exception {
        Class classifier = createClass("classifier");
        Class dummy = createClass("dummy");
        classifier.createOwnedAttribute("attr", dummy);
        Property property = classifier.createOwnedAttribute("attr", dummy);
        
        IClass clazz = mock(IClass.class);
        IAttribute attr = mock(IAttribute.class);
        when(attr.getName()).thenReturn("attr");
        IAttribute[] attributes = new IAttribute[]{
                attr
        };
        when(clazz.getAttributes()).thenReturn(attributes);
        converteds.put(classifier,clazz);
        IClass dummyClazz = mock(IClass.class);
        when(dummyClazz.getName()).thenReturn("dummy");
        when(dummyClazz.getFullName("::")).thenReturn("dummy");
        converteds.put(dummy,dummyClazz);
        
        IAttribute attribute = mock(IAttribute.class);
        when(basicModelEditor.createAttribute(clazz, "attr0", "int")).thenReturn(attribute);
        
        converter.convert(property,clazz);
        
        verify(basicModelEditor).createAttribute(clazz, "attr0", "int");
        verify(attribute).setQualifiedTypeExpression("dummy");        
    }

}
