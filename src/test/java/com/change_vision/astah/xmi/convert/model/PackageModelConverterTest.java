package com.change_vision.astah.xmi.convert.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static util.UML2TestUtil.createPackage;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Relationship;
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

public class PackageModelConverterTest {

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

    private PackageModelConverter converter;

    private Map<String, Relationship> relationships;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(util.getBasicModelEditor()).thenReturn(basicModelEditor);
        relationships = new HashMap<String, Relationship>();
        converter = new PackageModelConverter(relationships, util, helper);
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
    
    @Test
    public void convertWithPackageImport() throws Exception {
        Package target = createPackage("dummy");
        Package imported = createPackage("imported");
        target.createPackageImport(imported);
        IPackage result = mock(IPackage.class);
        when(basicModelEditor.createPackage(parentPackage, "dummy")).thenReturn(result);
        converter.convert(parentPackage,target);
        assertThat(relationships.size(),is(1));
    }

    @Test
    public void convertWithPackageMerge() throws Exception {
        Package target = createPackage("dummy");
        Package merged = createPackage("merged");
        target.createPackageMerge(merged);
        IPackage result = mock(IPackage.class);
        when(basicModelEditor.createPackage(parentPackage, "dummy")).thenReturn(result);
        converter.convert(parentPackage,target);
        assertThat(relationships.size(),is(1));
    }
}
