package com.change_vision.astah.xmi.convert.relationship;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static util.UML2TestUtil.createPackage;

import java.util.HashMap;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageMerge;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.model.IDependency;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IPackage;


public class PackageMergeConverterTest {


    @Mock
    private AstahAPIUtil util;

    @Mock
    private BasicModelEditor basicModelEditor;

    @Mock
    private Package sourcePackage;
    
    @Mock
    private IPackage sourcePackageConvertedElement;

    @Mock
    private Package targetPackage;
    
    @Mock
    private IPackage targetPackageConvertedElement;
    
    private PackageMergeConverter converter;

    private HashMap<Element, IElement> converteds;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(util.getBasicModelEditor()).thenReturn(basicModelEditor);
        converteds = new HashMap<Element, IElement>();
        converter = new PackageMergeConverter(converteds, util);
    }
    
    @Test
    public void rejectWithNull() {
        boolean result = converter.accepts(null);
        assertThat(result,is(false));
    }
    
    @Test
    public void acceptPackageMerge() throws Exception {
        Package pack = createPackage("pack");
        Package merged = createPackage("merged");
        PackageMerge relationship = pack.createPackageMerge(merged);
        boolean result = converter.accepts(relationship);
        assertThat(result,is(true));
    }
    
    @Test
    public void convertPackageMerge() throws Exception {
        converteds.put(sourcePackage, sourcePackageConvertedElement);
        converteds.put(targetPackage, targetPackageConvertedElement);

        IDependency created = mock(IDependency.class);
        when(basicModelEditor.createDependency(eq(sourcePackageConvertedElement), eq(targetPackageConvertedElement), eq(""))).thenReturn(created );

        PackageMerge rel = mock(PackageMerge.class);
        when(rel.getMergedPackage()).thenReturn(sourcePackage);
        when(rel.getReceivingPackage()).thenReturn(targetPackage);
        IElement element = converter.convert(rel);
        assertThat(element,is(notNullValue()));        
    }


}
