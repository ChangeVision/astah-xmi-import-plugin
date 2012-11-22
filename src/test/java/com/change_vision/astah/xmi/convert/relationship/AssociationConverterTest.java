package com.change_vision.astah.xmi.convert.relationship;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static util.UML2TestUtil.createAssociation;
import static util.UML2TestUtil.createClass;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.Type;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.ConvertHelper;
import com.change_vision.astah.xmi.convert.relationship.AssociationConverter;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.model.IClass;
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

    private AssociationConverter converter;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(util.getBasicModelEditor()).thenReturn(basicModelEditor);
        converter = new AssociationConverter(util, helper);
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

}
