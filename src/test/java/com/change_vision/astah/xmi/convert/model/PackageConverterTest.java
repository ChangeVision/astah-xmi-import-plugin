package com.change_vision.astah.xmi.convert.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static util.UML2TestUtil.createPackage;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.ConvertHelper;
import com.change_vision.astah.xmi.convert.exception.NotForUseException;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IPackage;

public class PackageConverterTest {

    @Mock
    private AstahAPIUtil util;

    @Mock
    private BasicModelEditor basicModelEditor;

    @Mock
    private IPackage parentPackage;
    
    @Mock
    private IModel parentModel;

    @Mock
    private IClass parentClass;

    @Mock
    private ConvertHelper helper;

    private PackageConverter converter;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(util.getBasicModelEditor()).thenReturn(basicModelEditor);
        converter = new PackageConverter(util, helper);
    }

    @Test
    public void rejectWithNull() {
        boolean result = converter.accepts(null);
        assertThat(result,is(false));
    }
    
    @Test
    public void acceptPackage() throws Exception {
        Package pack = createPackage("pack");
        boolean result = converter.accepts(pack);
        assertThat(result,is(true));
    }
    
    @Test(expected = NotForUseException.class)
    public void singleArgIsNotForUse() throws Exception {
        converter.convert(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void notConvertWithClassParent() throws Exception {
        Element target = createPackage("dummy");
        converter.convert(parentClass,target);
    }
    
    @Test
    public void convertInPackage() throws Exception {
        Element target = createPackage("dummy");
        IPackage result = mock(IPackage.class);
        when(basicModelEditor.createPackage(parentPackage, "dummy")).thenReturn(result);
        INamedElement converted = converter.convert(parentPackage,target);
        assertThat(converted,is(notNullValue()));
    }
    
}
