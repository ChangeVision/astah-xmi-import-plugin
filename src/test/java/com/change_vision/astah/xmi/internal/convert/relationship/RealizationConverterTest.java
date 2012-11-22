package com.change_vision.astah.xmi.internal.convert.relationship;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static util.UML2TestUtil.createRealization;

import java.util.HashMap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Realization;
import org.eclipse.uml2.uml.Relationship;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.internal.convert.UMLUtil;
import com.change_vision.astah.xmi.internal.convert.relationship.RealizationConverter;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.model.IRealization;

public class RealizationConverterTest {

    @Mock
    private AstahAPIUtil util;

    @Mock
    private BasicModelEditor basicModelEditor;

    @Mock
    private IPackage parentPackage;
    
    @Mock
    private IClass parentClass;
    
    @Mock
    private Classifier sourceClassifier;
    
    @Mock
    private IClass sourceClassifierConvertedElement;

    @Mock
    private Classifier targetClassifier;
    
    @Mock
    private IClass targetClassifierConvertedElement;

    private RealizationConverter converter;

    private HashMap<Element, IElement> converteds;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(util.getBasicModelEditor()).thenReturn(basicModelEditor);
        converteds = new HashMap<Element, IElement>();
        converter = new RealizationConverter(converteds,util);
    }
    
    @Test
    public void rejectWithNull() throws Exception {
        boolean result = converter.accepts(null);
        assertThat(result,is(false));        
    }
    
    @Test
    public void acceptRealization() {
        Relationship element = createRealization("hoge");
        boolean result = converter.accepts(element);
        assertThat(result,is(true));
    }
    
    @Test
    public void notConvertNotHaveTarget() throws Exception {
        converteds.put(sourceClassifier, sourceClassifierConvertedElement);
        Realization realization = mock(Realization.class);
        
        EList<NamedElement> suppliers = mock(EList.class);
        when(suppliers.get(0)).thenReturn(sourceClassifier);
        when(realization.getSuppliers()).thenReturn(suppliers);
        
        EList<NamedElement> clients = mock(EList.class);
        when(realization.getClients()).thenReturn(clients);
        
        assertThat(UMLUtil.getSource(realization),is(notNullValue()));
        assertThat(UMLUtil.getTarget(realization),is(nullValue()));
        
        IElement element = converter.convert(realization);
        assertThat(element,is(nullValue()));
    }
    
    @Test
    public void notConvertNotHaveSource() throws Exception {
        converteds.put(targetClassifier, targetClassifierConvertedElement);
        Realization realization = mock(Realization.class);
        
        EList<NamedElement> suppliers = mock(EList.class);
        when(realization.getSuppliers()).thenReturn(suppliers);
        
        EList<NamedElement> clients = mock(EList.class);
        when(clients.get(0)).thenReturn(targetClassifier);
        when(realization.getClients()).thenReturn(clients);
        
        assertThat(UMLUtil.getSource(realization),is(nullValue()));
        assertThat(UMLUtil.getTarget(realization),is(notNullValue()));
        
        IElement element = converter.convert(realization);
        assertThat(element,is(nullValue()));
    }


    @Test
    public void convertRealization() throws Exception {
        converteds.put(sourceClassifier, sourceClassifierConvertedElement);
        converteds.put(targetClassifier, targetClassifierConvertedElement);
        Realization realization = mock(Realization.class);
        EList<NamedElement> clients = mock(EList.class);
        when(clients.get(0)).thenReturn(sourceClassifier);
        when(realization.getClients()).thenReturn(clients);
        EList<NamedElement> suppliers = mock(EList.class);
        when(suppliers.get(0)).thenReturn(targetClassifier);
        when(realization.getSuppliers()).thenReturn(suppliers);
        
        IRealization created = mock(IRealization.class);
        when(basicModelEditor.createRealization(eq(sourceClassifierConvertedElement), eq(targetClassifierConvertedElement), anyString())).thenReturn(created);
        
        IElement element = converter.convert(realization);
        assertThat(element,is(notNullValue()));
    }

}
