package com.change_vision.astah.xmi.convert.relationship;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static util.UML2TestUtil.createAssociation;
import static util.UML2TestUtil.createClass;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.Type;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.ConvertHelper;
import com.change_vision.astah.xmi.convert.RelationConverter;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.model.IAssociation;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IPackage;

public class AssociationConverterTest {

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
    private Classifier sourceClassifier;
    
    @Mock
    private IClass sourceClassifierConvertedElement;

    @Mock
    private Classifier targetClassifier;
    
    @Mock
    private IClass targetClassifierConvertedElement;

    private AssociationConverter converter;

    private Map<Element, IElement> converteds;
    

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(util.getBasicModelEditor()).thenReturn(basicModelEditor);
        converteds = new HashMap<Element, IElement>();
        converter = new AssociationConverter(converteds , util);
    }
    
    @Test
    public void rejectWithNull() throws Exception {
        boolean result = converter.accepts(null);
        assertThat(result,is(false));        
    }
    
    @Test
    public void rejectAssociationWithEmptyMemberEnds() {
        Relationship element = createAssociation("hoge");
        boolean result = converter.accepts(element);
        assertThat(result,is(false));
    }
    
    @Test
    public void acceptAssociationWithSomeMemberEnds() {
        Association element = createAssociation("hoge");
        Type type1 = createClass("type");
        Type type2 = createClass("type");
        element.createOwnedEnd("", type1);
        element.createOwnedEnd("", type2);
        boolean result = converter.accepts(element);
        assertThat(result,is(true));
    }
    
    @Test
    public void convertAssociation() throws Exception {
        converteds.put(sourceClassifier, sourceClassifierConvertedElement);
        converteds.put(targetClassifier, targetClassifierConvertedElement);

        Association association = mock(Association.class);
        when(association.isSetName()).thenReturn(true);
        when(association.getName()).thenReturn("association");

        Property sourceProperty = mock(Property.class);
        when(sourceProperty.getName()).thenReturn("dummySource");
        when(sourceProperty.isSetName()).thenReturn(true);
        when(sourceProperty.getType()).thenReturn(sourceClassifier);

        Property targetProperty = mock(Property.class);
        when(targetProperty.getName()).thenReturn("dummyTarget");
        when(targetProperty.isSetName()).thenReturn(true);
        when(targetProperty.getType()).thenReturn(targetClassifier);

        EList<Property> members = mock(EList.class);
        when(members.get(0)).thenReturn(sourceProperty);
        when(members.get(1)).thenReturn(targetProperty);
        
        when(association.getMemberEnds()).thenReturn(members );
        
        IAssociation created = mock(IAssociation.class);
        IAttribute sourceAttribute = mock(IAttribute.class);
        IAttribute targetAttribute = mock(IAttribute.class);
        IAttribute[] attributes = new IAttribute[]{
                sourceAttribute ,
                targetAttribute
        };
        
        when(created.getMemberEnds()).thenReturn(attributes );
        when(basicModelEditor.createAssociation(eq(sourceClassifierConvertedElement), eq(targetClassifierConvertedElement), eq("association"), eq("dummySource"), eq("dummyTarget"))).thenReturn(created);
        
        RelationConverter converter = new RelationConverter(converteds, util);
        assertThat(converteds.containsValue(sourceAttribute),is(false));
        assertThat(converteds.containsValue(targetAttribute),is(false));
        INamedElement element = converter.convert(association);
        assertThat(element,is(notNullValue()));
        assertThat(converteds.containsValue(sourceAttribute),is(true));
        assertThat(converteds.containsValue(targetAttribute),is(true));
    }


}
