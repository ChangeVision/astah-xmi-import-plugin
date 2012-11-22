package com.change_vision.astah.xmi.internal.convert.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static util.UML2TestUtil.*;

import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.Element;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.internal.convert.ConvertHelper;
import com.change_vision.astah.xmi.internal.convert.model.ClassConverter;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IPackage;

public class ClassConverterTest {

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

    private ClassConverter converter;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(util.getBasicModelEditor()).thenReturn(basicModelEditor);
        converter = new ClassConverter(util, helper);
    }

    @Test
    public void rejectWithNull() throws Exception {
        boolean result = converter.accepts(null);
        assertThat(result, is(false));
    }

    @Test
    public void acceptClass() {
        Element target = createClass("dummy");
        boolean result = converter.accepts(target);
        assertThat(result, is(true));
    }

    @Test
    public void rejectInterface() throws Exception {
        Element target = createInterface("dummy");
        boolean result = converter.accepts(target);
        assertThat(result, is(false));
    }

    @Test
    public void rejectDataType() throws Exception {
        Element target = createDataType("dummy");
        boolean result = converter.accepts(target);
        assertThat(result, is(false));
    }

    @Test
    public void rejectBehavior() throws Exception {
        Element target = mock(Behavior.class);
        boolean result = converter.accepts(target);
        assertThat(result, is(false));
    }

    @Test
    public void rejectComponent() throws Exception {
        Element target = createComponent("dummy");
        boolean result = converter.accepts(target);
        assertThat(result, is(false));
    }

    @Test
    public void rejectNode() throws Exception {
        Element target = createNode("dummy");
        boolean result = converter.accepts(target);
        assertThat(result, is(false));
    }

    @Test
    public void rejectStereotype() throws Exception {
        Element target = createStereotype("dummy");
        boolean result = converter.accepts(target);
        assertThat(result, is(false));
    }

    @Test
    public void convertInPackage() throws Exception {
        Element target = createClass("dummy");
        IClass result = mock(IClass.class);
        when(basicModelEditor.createClass(parentPackage, "dummy")).thenReturn(result);
        IElement converted = converter.convert(parentPackage, target);
        assertThat(converted, is(notNullValue()));
    }

    @Test
    public void convertSameNameClass() throws Exception {
        Element target = createClass("dummy");
        IClass result = mock(IClass.class);
        INamedElement existed = mock(IClass.class);
        when(existed.getName()).thenReturn("dummy");
        when(parentPackage.getOwnedElements()).thenReturn(new INamedElement[] { existed });
        when(basicModelEditor.createClass(parentPackage, "dummy0")).thenReturn(result);
        IElement converted = converter.convert(parentPackage, target);
        assertThat(converted, is(notNullValue()));
    }

    @Test
    public void convertInClass() throws Exception {
        Element target = createClass("dummy");
        IClass result = mock(IClass.class);
        when(basicModelEditor.createClass(parentClass, "dummy")).thenReturn(result);
        IElement converted = converter.convert(parentClass, target);
        assertThat(converted, is(notNullValue()));
    }

}
