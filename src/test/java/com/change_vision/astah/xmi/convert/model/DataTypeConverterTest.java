package com.change_vision.astah.xmi.convert.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static util.UML2TestUtil.createDataType;

import org.eclipse.uml2.uml.Element;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.ConvertHelper;
import com.change_vision.astah.xmi.convert.exception.NotForUseException;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IPackage;

public class DataTypeConverterTest {

    @Mock
    private AstahAPIUtil util;

    @Mock
    private BasicModelEditor basicModelEditor;

    @Mock
    private IPackage parentPackage;
    
    @Mock
    private IClass parentClass;
    
    @Mock
    private ConvertHelper helper;

    private DataTypeConverter converter;
        
    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(util.getBasicModelEditor()).thenReturn(basicModelEditor);
        converter = new DataTypeConverter(util, helper);
    }

    @Test
    public void rejectWithNull() throws Exception {
        boolean result = converter.accepts(null);
        assertThat(result,is(false));        
    }

    @Test
    public void acceptDataType() {
        Element target = createDataType("dummy");
        boolean result = converter.accepts(target);
        assertThat(result,is(true));
    }
        
    @Test(expected=NotForUseException.class)
    public void singleArgIsNotForUse() throws Exception {
        converter.convert(null);
    }

    @Test
    public void convertInPackage() throws Exception {
        Element target = createDataType("dummy");
        IClass result = mock(IClass.class);
        when(basicModelEditor.createClass(parentPackage, "dummy")).thenReturn(result);
        IElement converted = converter.convert(parentPackage, target);
        assertThat(converted,is(notNullValue()));
    }

    @Test
    public void convertInClass() throws Exception {
        Element target = createDataType("dummy");
        IClass result = mock(IClass.class);
        when(basicModelEditor.createClass(parentClass, "dummy")).thenReturn(result);
        IElement converted = converter.convert(parentClass, target);
        assertThat(converted, is(notNullValue()));
    }
    
    @Test
    public void notConvertInAstahPrimitives() throws Exception {
        Element target = createDataType("int");
        IClass result = mock(IClass.class);
        when(basicModelEditor.createClass(parentClass, "int")).thenReturn(result);
        IElement converted = converter.convert(parentClass, target);
        assertThat(converted, is(nullValue()));
    }

}
