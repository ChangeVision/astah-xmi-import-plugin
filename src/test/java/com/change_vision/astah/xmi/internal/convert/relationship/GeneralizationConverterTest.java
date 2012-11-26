package com.change_vision.astah.xmi.internal.convert.relationship;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static util.UML2TestUtil.createGeneralization;

import java.util.HashMap;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Relationship;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.internal.convert.UMLUtil;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IGeneralization;
import com.change_vision.jude.api.inf.model.IPackage;

public class GeneralizationConverterTest {

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
    
    private GeneralizationConverter converter;
    
    private HashMap<Element, IElement> converteds;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(util.getBasicModelEditor()).thenReturn(basicModelEditor);
        converteds = new HashMap<Element, IElement>();
        converter = new GeneralizationConverter(util);
    }
    
    @Test
    public void rejectWithNull() throws Exception {
        boolean result = converter.accepts(null);
        assertThat(result,is(false));        
    }
    
    @Test
    public void acceptGeneralization() {
        Relationship element = createGeneralization();
        boolean result = converter.accepts(element);
        assertThat(result,is(true));
    }
    
    @Test
    public void notConvertNotHaveTarget() throws Exception {
        converteds.put(sourceClassifier, sourceClassifierConvertedElement);
        Generalization generalization = mock(Generalization.class);
        when(generalization.getSpecific()).thenReturn(sourceClassifier);
        
        assertThat(UMLUtil.getSource(generalization),is(notNullValue()));
        assertThat(UMLUtil.getTarget(generalization),is(nullValue()));
        
        IElement element = converter.convert(converteds, generalization);
        assertThat(element,is(nullValue()));
    }
    
    @Test
    public void notConvertNotHaveSource() throws Exception {
        converteds.put(targetClassifier, targetClassifierConvertedElement);
        Generalization generalization = mock(Generalization.class);
        when(generalization.getGeneral()).thenReturn(targetClassifier);
        
        assertThat(UMLUtil.getSource(generalization),is(nullValue()));
        assertThat(UMLUtil.getTarget(generalization),is(notNullValue()));
        
        IElement element = converter.convert(converteds, generalization);
        assertThat(element,is(nullValue()));
    }

    
    @Test
    public void convertGeneralization() throws Exception {
        converteds.put(sourceClassifier, sourceClassifierConvertedElement);
        converteds.put(targetClassifier, targetClassifierConvertedElement);
        Generalization generalization = mock(Generalization.class);
        when(generalization.getSpecific()).thenReturn(sourceClassifier);
        when(generalization.getGeneral()).thenReturn(targetClassifier);
        
        IGeneralization created = mock(IGeneralization.class);
        when(basicModelEditor.createGeneralization(eq(sourceClassifierConvertedElement), eq(targetClassifierConvertedElement), anyString())).thenReturn(created);
        
        IElement element = converter.convert(converteds, generalization);
        assertThat(element,is(notNullValue()));
    }

}
