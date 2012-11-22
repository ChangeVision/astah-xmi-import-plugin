package com.change_vision.astah.xmi.convert.relationship;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static util.UML2TestUtil.createUsage;

import java.util.HashMap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.Usage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.RelationConverter;
import com.change_vision.astah.xmi.convert.UMLUtil;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.model.IUsage;

public class UsageConverterTest {

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

    private UsageConverter converter;

    private HashMap<Element, IElement> converteds;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(util.getBasicModelEditor()).thenReturn(basicModelEditor);
        converteds = new HashMap<Element, IElement>();
        converter = new UsageConverter(converteds,util);
    }
    
    @Test
    public void rejectWithNull() throws Exception {
        boolean result = converter.accepts(null);
        assertThat(result,is(false));        
    }
    
    @Test
    public void acceptUsage() {
        Relationship element = createUsage("usage");
        boolean result = converter.accepts(element);
        assertThat(result,is(true));
    }
    
    @Test
    public void notConvertNotHaveTarget() throws Exception {
        converteds.put(sourceClassifier, sourceClassifierConvertedElement);
        Usage usege = mock(Usage.class);
        
        EList<NamedElement> suppliers = mock(EList.class);
        when(suppliers.get(0)).thenReturn(sourceClassifier);
        when(usege.getSuppliers()).thenReturn(suppliers);
        
        EList<NamedElement> clients = mock(EList.class);
        when(usege.getClients()).thenReturn(clients);
        
        assertThat(UMLUtil.getSource(usege),is(notNullValue()));
        assertThat(UMLUtil.getTarget(usege),is(nullValue()));
        
        IElement element = converter.convert(usege);
        assertThat(element,is(nullValue()));
    }
    
    @Test
    public void notConvertNotHaveSource() throws Exception {
        converteds.put(targetClassifier, targetClassifierConvertedElement);
        Usage usege = mock(Usage.class);
        
        EList<NamedElement> suppliers = mock(EList.class);
        when(usege.getSuppliers()).thenReturn(suppliers);
        
        EList<NamedElement> clients = mock(EList.class);
        when(clients.get(0)).thenReturn(targetClassifier);
        when(usege.getClients()).thenReturn(clients);
        
        assertThat(UMLUtil.getSource(usege),is(nullValue()));
        assertThat(UMLUtil.getTarget(usege),is(notNullValue()));
        
        IElement element = converter.convert(usege);
        assertThat(element,is(nullValue()));
    }

    @Test
    public void convertUsage() throws Exception {
        converteds.put(sourceClassifier, sourceClassifierConvertedElement);
        converteds.put(targetClassifier, targetClassifierConvertedElement);

        Usage usage = mock(Usage.class);
        EList<NamedElement> clients = mock(EList.class);
        when(clients.get(0)).thenReturn(sourceClassifier);
        when(usage.getClients()).thenReturn(clients);
        EList<NamedElement> suppliers = mock(EList.class);
        when(suppliers.get(0)).thenReturn(targetClassifier);
        when(usage.getSuppliers()).thenReturn(suppliers);
        
        IUsage created = mock(IUsage.class);
        when(basicModelEditor.createUsage(eq(sourceClassifierConvertedElement), eq(targetClassifierConvertedElement), anyString())).thenReturn(created);
        
        RelationConverter converter = new RelationConverter(converteds, util);
        INamedElement element = converter.convert(usage);
        assertThat(element,is(notNullValue()));
    }

}
