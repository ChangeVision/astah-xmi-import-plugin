package com.change_vision.astah.xmi.internal.convert.relationship;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static util.UML2TestUtil.createTemplateBinding;

import java.util.HashMap;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.TemplateBinding;
import org.eclipse.uml2.uml.TemplateSignature;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.internal.convert.ConvertHelper;
import com.change_vision.astah.xmi.internal.convert.relationship.TemplateBindingConverter;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.model.ITemplateBinding;

public class TemplateBindingConverterTest {

    @Mock
    private AstahAPIUtil util;

    @Mock
    private BasicModelEditor basicModelEditor;

    @Mock
    private Classifier sourceClassifier;
    
    @Mock
    private IClass sourceClassifierConvertedElement;

    @Mock
    private Classifier targetClassifier;
    
    @Mock
    private IClass targetClassifierConvertedElement;
    
    @Mock
    private IPackage parentPackage;
    
    @Mock
    private IClass parentClass;
    
    @Mock
    private ConvertHelper helper;

    private TemplateBindingConverter converter;
    
    private HashMap<Element, IElement> converteds;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(util.getBasicModelEditor()).thenReturn(basicModelEditor);
        converteds = new HashMap<Element, IElement>();
        converter = new TemplateBindingConverter(converteds, util);
    }
    
    @Test
    public void rejectWithNull() {
        boolean result = converter.accepts(null);
        assertThat(result,is(false));
    }
    
    @Test
    public void acceptTemplateBinding() throws Exception {
        Relationship relationship = createTemplateBinding();
        boolean result = converter.accepts(relationship);
        assertThat(result,is(true));
    }
    
    
    @Test
    public void convertTemplateBinding() throws Exception {
        converteds.put(sourceClassifier, sourceClassifierConvertedElement);
        converteds.put(targetClassifier, targetClassifierConvertedElement);
        
        TemplateBinding binding = mock(TemplateBinding.class);
        when(binding.getBoundElement()).thenReturn(sourceClassifier);
        TemplateSignature signature = mock(TemplateSignature.class);
        when(signature.getTemplate()).thenReturn(targetClassifier);
        when(binding.getSignature()).thenReturn(signature );
        
        ITemplateBinding created = mock(ITemplateBinding.class);
        when(basicModelEditor.createTemplateBinding(sourceClassifierConvertedElement, targetClassifierConvertedElement)).thenReturn(created);
        
        IElement element = converter.convert(binding);
        assertThat(element,is(notNullValue()));
    }


}
