package com.change_vision.astah.xmi.convert.relationship;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static util.UML2TestUtil.createPackage;

import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.ConvertHelper;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IPackage;


public class PackageImportConverterTest {


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

    private PackageImportConverter converter;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(util.getBasicModelEditor()).thenReturn(basicModelEditor);
        converter = new PackageImportConverter(util, helper);
    }
    
    @Test
    public void rejectWithNull() {
        boolean result = converter.accepts(null);
        assertThat(result,is(false));
    }
    
    @Test
    public void acceptPackageImport() throws Exception {
        Package pack = createPackage("pack");
        Package imported = createPackage("imported");
        PackageImport relationship = pack.createPackageImport(imported);
        boolean result = converter.accepts(relationship);
        assertThat(result,is(true));
    }

}
