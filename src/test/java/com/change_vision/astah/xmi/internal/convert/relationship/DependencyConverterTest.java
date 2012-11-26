package com.change_vision.astah.xmi.internal.convert.relationship;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static util.UML2TestUtil.createDependency;

import java.util.HashMap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Deployment;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Relationship;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.internal.convert.ConvertHelper;
import com.change_vision.astah.xmi.internal.convert.UMLUtil;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IDependency;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IPackage;

public class DependencyConverterTest {

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
    
    @Mock
    private Package sourcePackage;
    
    @Mock
    private IPackage sourcePackageConvertedElement;

    @Mock
    private Package targetPackage;
    
    @Mock
    private IPackage targetPackageConvertedElement;

    private DependencyConverter converter;
    
    private HashMap<Element, IElement> converteds;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(util.getBasicModelEditor()).thenReturn(basicModelEditor);
        converteds = new HashMap<Element, IElement>();
        converter = new DependencyConverter(util);
    }
    
    @Test
    public void rejectWithNull() throws Exception {
        boolean result = converter.accepts(null);
        assertThat(result,is(false));        
    }
    
    @Test
    public void rejectDeployment() throws Exception {
        Relationship element = mock(Deployment.class);
        boolean result = converter.accepts(element);
        assertThat(result,is(false));        
    }
    
    @Test
    public void acceptDependency() {
        Relationship element = createDependency("dupends");
        boolean result = converter.accepts(element);
        assertThat(result,is(true));
    }
    
    @Test
    public void notConvertNotHaveTarget() throws Exception {
        converteds.put(sourcePackage, sourcePackageConvertedElement);
        Dependency dependency = mock(Dependency.class);
        
        EList<NamedElement> suppliers = mock(EList.class);
        when(suppliers.get(0)).thenReturn(sourcePackage);
        when(dependency.getSuppliers()).thenReturn(suppliers);
        
        EList<NamedElement> clients = mock(EList.class);
        when(dependency.getClients()).thenReturn(clients);
        
        assertThat(UMLUtil.getSource(dependency),is(notNullValue()));
        assertThat(UMLUtil.getTarget(dependency),is(nullValue()));
        
        IElement element = converter.convert(converteds,dependency);
        assertThat(element,is(nullValue()));
    }
    
    @Test
    public void notConvertNotHaveSource() throws Exception {
        converteds.put(targetPackage, targetPackageConvertedElement);
        Dependency dependency = mock(Dependency.class);
        
        EList<NamedElement> suppliers = mock(EList.class);
        when(dependency.getSuppliers()).thenReturn(suppliers);
        
        EList<NamedElement> clients = mock(EList.class);
        when(clients.get(0)).thenReturn(targetPackage);
        when(dependency.getClients()).thenReturn(clients);
        
        assertThat(UMLUtil.getSource(dependency),is(nullValue()));
        assertThat(UMLUtil.getTarget(dependency),is(notNullValue()));
        
        IElement element = converter.convert(converteds,dependency);
        assertThat(element,is(nullValue()));
    }

    @Test
    public void convertDependency() throws Exception {
        converteds.put(sourcePackage, sourcePackageConvertedElement);
        converteds.put(targetPackage, targetPackageConvertedElement);

        Dependency dependency = mock(Dependency.class);
        when(dependency.getName()).thenReturn("dependency");
        when(dependency.isSetName()).thenReturn(true);

        EList<NamedElement> clients = mock(EList.class);
        when(clients.size()).thenReturn(1);
        when(clients.get(0)).thenReturn(targetPackage);
        when(dependency.getClients()).thenReturn(clients);
        
        EList<NamedElement> suppliers = mock(EList.class);
        when(suppliers.get(0)).thenReturn(sourcePackage);
        when(suppliers.size()).thenReturn(1);
        when(dependency.getSuppliers()).thenReturn(suppliers);

        IDependency created = mock(IDependency.class);
        when(basicModelEditor.createDependency(eq(sourcePackageConvertedElement), eq(targetPackageConvertedElement), eq("dependency"))).thenReturn(created);

        IElement element = converter.convert(converteds, dependency);
        assertThat(element,is(notNullValue()));        
    }


}
