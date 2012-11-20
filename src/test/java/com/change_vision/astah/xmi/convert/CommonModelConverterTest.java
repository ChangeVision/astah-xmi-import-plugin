package com.change_vision.astah.xmi.convert;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static util.UML2TestUtil.*;

import java.util.HashMap;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.InformationFlow;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.PackageMerge;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.TemplateBinding;
import org.eclipse.uml2.uml.TemplateSignature;
import org.eclipse.uml2.uml.Type;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IOperation;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.model.IParameter;

public class CommonModelConverterTest {

    @Mock
    private ConvertHelper helper;

    @Mock
    private Element element;

    @Mock
    private IModel model;

    @Mock
    private AstahAPIUtil util;

    @Mock
    private BasicModelEditor basicModelEditor;

    private HashMap<String, Relationship> relationships;

    private HashMap<Element, IElement> converteds;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(util.getBasicModelEditor()).thenReturn(basicModelEditor);

        relationships = new HashMap<String, Relationship>();
        converteds = new HashMap<Element, IElement>();
    }

    @Test
    public void convertWithNullModelIsOK() throws InvalidEditingException, ClassNotFoundException {
        CommonModelConverter converter = new CommonModelConverter(helper, converteds, relationships);
        converter.convert(null, element);
    }

    @Test(expected = IllegalArgumentException.class)
    public void convertWithNullElement() throws Exception {
        CommonModelConverter converter = new CommonModelConverter(helper, converteds, relationships);
        converter.convert(model, null);
    }
    
    @Test
    public void convertWithNoResponseByInvalidEditingException() throws Exception {
        when(model.getOwnedElements()).thenReturn(new INamedElement[] {});

        Class target = createClass("test");
        EList<Element> elements = new BasicEList<Element>();
        elements.add(target);
        when(element.getOwnedElements()).thenReturn(elements);
        when(basicModelEditor.createClass(model, "test")).thenThrow(new InvalidEditingException("hoge", "huga"));

        CommonModelConverter converter = new CommonModelConverter(helper, converteds, relationships);
        converter.setAstahAPIUtil(util);
        converter.convert(model, element);
    }

    @Test
    public void convertEmptyElement() throws Exception {
        CommonModelConverter converter = new CommonModelConverter(helper, converteds, relationships);
        EList<Element> elements = new BasicEList<Element>();
        when(element.getOwnedElements()).thenReturn(elements);
        converter.convert(model, element);
    }

    @Test
    public void convertClass() throws Exception {
        convertClassPattern(createClass("test"));
    }

    @Test
    public void convertNestedClass() throws Exception {
        IClass owned = mock(IClass.class);
        when(owned.getName()).thenReturn("owned");
        when(owned.getNestedClasses()).thenReturn(new IClass[] {});

        Class clazz = createClass("test");
        EList<Element> elements = new BasicEList<Element>();
        elements.add(clazz);
        when(element.getOwnedElements()).thenReturn(elements);

        CommonModelConverter converter = new CommonModelConverter(helper, converteds, relationships);
        converter.setAstahAPIUtil(util);
        converter.convert(owned, element);
        verify(basicModelEditor).createClass(eq(owned), eq("test"));
    }
    
    @Test
    public void notConvertPropertyToAttribute() throws Exception {
        IClass owned = mock(IClass.class);
        when(owned.getName()).thenReturn("owned");
        when(owned.getAttributes()).thenReturn(new IAttribute[] {});

        Property prop = createProperty("test");
        Type type = createClass("Hoge");
        prop.setType(type);
        EList<Element> elements = new BasicEList<Element>();
        elements.add(prop);
        when(element.getOwnedElements()).thenReturn(elements);

        CommonModelConverter converter = new CommonModelConverter(helper, converteds, relationships);
        converter.setAstahAPIUtil(util);
        converter.convert(owned, element);
        
        verify(basicModelEditor,never()).createAttribute(eq(owned), anyString(), org.mockito.Matchers.any(IClass.class));
        verify(basicModelEditor,never()).createClass(eq(owned), eq("test"));
    }
    
    @Test
    public void notConvertEnumerationLiteralToAttribute() throws Exception {
        IClass owned = mock(IClass.class);
        when(owned.getName()).thenReturn("owned");
        when(owned.getAttributes()).thenReturn(new IAttribute[] {});

        EnumerationLiteral enumerationLiteral = createEnumerationLiteral("test");
        EList<Element> elements = new BasicEList<Element>();
        elements.add(enumerationLiteral);
        when(element.getOwnedElements()).thenReturn(elements);

        CommonModelConverter converter = new CommonModelConverter(helper, converteds, relationships);
        converter.setAstahAPIUtil(util);
        converter.convert(owned, element);
        
        verify(basicModelEditor,never()).createAttribute(eq(owned), anyString(), org.mockito.Matchers.any(IClass.class));
        verify(basicModelEditor,never()).createClass(eq(owned), eq("test"));        
    }

    @Test
    public void notConvertOperation() throws Exception {
        IClass owned = mock(IClass.class);
        when(owned.getName()).thenReturn("owned");
        when(owned.getAttributes()).thenReturn(new IAttribute[] {});

        Operation operation = createOperation("test");
        EList<Element> elements = new BasicEList<Element>();
        elements.add(operation);
        when(element.getOwnedElements()).thenReturn(elements);

        CommonModelConverter converter = new CommonModelConverter(helper, converteds, relationships);
        converter.setAstahAPIUtil(util);
        converter.convert(owned, element);
        
        verify(basicModelEditor,never()).createOperation(eq(owned), anyString(), org.mockito.Matchers.any(IClass.class));
        verify(basicModelEditor,never()).createOperation(eq(owned), anyString(), org.mockito.Matchers.any(String.class));
        verify(basicModelEditor,never()).createClass(eq(owned), eq("test"));
    }
    
    @Test
    public void notConvertParameter() throws Exception {
        IOperation owned = mock(IOperation.class);
        when(owned.getName()).thenReturn("owned");
        when(owned.getParameters()).thenReturn(new IParameter[] {});

        Parameter parameter = createParameter("test");
        EList<Element> elements = new BasicEList<Element>();
        elements.add(parameter);
        when(element.getOwnedElements()).thenReturn(elements);

        CommonModelConverter converter = new CommonModelConverter(helper, converteds, relationships);
        converter.setAstahAPIUtil(util);
        converter.convert(owned, element);
        
        verify(basicModelEditor,never()).createParameter(eq(owned), anyString(), org.mockito.Matchers.any(IClass.class));
        verify(basicModelEditor,never()).createParameter(eq(owned), anyString(), org.mockito.Matchers.any(String.class));
    }

    @Test
    public void convertGeneralization() throws Exception {
        IClass owned = mock(IClass.class);
        when(owned.getName()).thenReturn("owned");
        when(owned.getNestedClasses()).thenReturn(new IClass[] {});

        EList<Element> elements = new BasicEList<Element>();
        Generalization generalization = createGeneralization();
        elements.add(generalization);
        when(element.getOwnedElements()).thenReturn(elements);

        CommonModelConverter converter = new CommonModelConverter(helper, converteds, relationships);
        converter.setAstahAPIUtil(util);
        converter.convert(owned, element);

        Relationship relationship = relationships.get(EcoreUtil.getURI(generalization).fragment());
        assertThat(relationship, is(notNullValue()));
    }

    @Test
    public void convertDependency() throws Exception {
        IClass owned = mock(IClass.class);
        when(owned.getName()).thenReturn("owned");
        when(owned.getNestedClasses()).thenReturn(new IClass[] {});

        EList<Element> elements = new BasicEList<Element>();
        Dependency dependency = createDependency("test");
        elements.add(dependency);
        when(element.getOwnedElements()).thenReturn(elements);

        CommonModelConverter converter = new CommonModelConverter(helper, converteds, relationships);
        converter.setAstahAPIUtil(util);
        converter.convert(owned, element);

        Relationship relationship = relationships.get(EcoreUtil.getURI(dependency).fragment());
        assertThat(relationship, is(notNullValue()));
    }
    
    @Test
    public void convertRealization() throws Exception {
        IClass owned = mock(IClass.class);
        when(owned.getName()).thenReturn("owned");
        when(owned.getNestedClasses()).thenReturn(new IClass[] {});

        EList<Element> elements = new BasicEList<Element>();
        Dependency realization = createRealization("test");
        elements.add(realization);
        when(element.getOwnedElements()).thenReturn(elements);

        CommonModelConverter converter = new CommonModelConverter(helper, converteds, relationships);
        converter.setAstahAPIUtil(util);
        converter.convert(owned, element);

        Relationship relationship = relationships.get(EcoreUtil.getURI(realization).fragment());
        assertThat(relationship, is(notNullValue()));        
    }

    @Test
    public void convertInterface() throws Exception {
        convertClassPattern(createInterface("test"));
    }

    @Test
    public void convertPackage() throws Exception {
        when(model.getOwnedElements()).thenReturn(new INamedElement[] {});

        EList<Element> elements = new BasicEList<Element>();
        Package pack = createPackage("test");
        elements.add(pack);
        when(element.getOwnedElements()).thenReturn(elements);

        CommonModelConverter converter = new CommonModelConverter(helper, converteds, relationships);
        converter.setAstahAPIUtil(util);
        converter.convert(model, element);

        verify(basicModelEditor).createPackage(eq(model), eq("test"));
    }

    @Test
    public void convertPackageImport() throws Exception {
        INamedElement owned = mock(IPackage.class);
        when(owned.getName()).thenReturn("imported");
        when(model.getOwnedElements()).thenReturn(new INamedElement[] { owned });

        EList<Element> elements = new BasicEList<Element>();
        Package pack = createPackage("test");
        elements.add(pack);

        when(element.getOwnedElements()).thenReturn(elements);

        Package imported = createPackage("imported");
        PackageImport packageImport = pack.createPackageImport(imported);

        CommonModelConverter converter = new CommonModelConverter(helper, converteds, relationships);
        converter.setAstahAPIUtil(util);
        converter.convert(model, element);

        verify(basicModelEditor).createPackage(eq(model), eq("test"));
        Relationship relationship = relationships.get(EcoreUtil.getURI(packageImport).fragment());
        assertThat(relationship, is(notNullValue()));
    }

    @Test
    public void convertPackageMerge() throws Exception {
        INamedElement owned = mock(IPackage.class);
        when(owned.getName()).thenReturn("merged");
        when(model.getOwnedElements()).thenReturn(new INamedElement[] { owned });

        EList<Element> elements = new BasicEList<Element>();
        Package pack = createPackage("test");
        elements.add(pack);

        when(element.getOwnedElements()).thenReturn(elements);

        Package merged = createPackage("merged");
        PackageMerge packageMerge = pack.createPackageMerge(merged);

        CommonModelConverter converter = new CommonModelConverter(helper, converteds, relationships);
        converter.setAstahAPIUtil(util);
        converter.convert(model, element);

        verify(basicModelEditor).createPackage(eq(model), eq("test"));
        Relationship relationship = relationships.get(EcoreUtil.getURI(packageMerge).fragment());
        assertThat(relationship, is(notNullValue()));
    }

    @Test
    public void convertAssociation() throws Exception {
        when(model.getOwnedElements()).thenReturn(new INamedElement[] {});

        EList<Element> elements = new BasicEList<Element>();
        Association association = createAssociation("test");
        elements.add(association);
        Class clazz = createClass("test");
        association.createOwnedEnd("hoge", clazz);
        association.createOwnedEnd("fuga", clazz);
        when(element.getOwnedElements()).thenReturn(elements);

        CommonModelConverter converter = new CommonModelConverter(helper, converteds, relationships);
        converter.setAstahAPIUtil(util);
        converter.convert(model, element);

        Relationship relationship = relationships.get(EcoreUtil.getURI(association).fragment());
        assertThat(relationship, is(notNullValue()));
    }

    @Test
    public void convertTemplateBinding() throws Exception {
        when(model.getOwnedElements()).thenReturn(new INamedElement[] {});

        EList<Element> elements = new BasicEList<Element>();
        Class clazz = createClass("test");
        TemplateSignature signature = createTemplateSignature();
        TemplateBinding binding = clazz.createTemplateBinding(signature);
        elements.add(clazz);
        when(element.getOwnedElements()).thenReturn(elements);

        CommonModelConverter converter = new CommonModelConverter(helper, converteds, relationships);
        converter.setAstahAPIUtil(util);
        converter.convert(model, element);

        Relationship relationship = relationships.get(EcoreUtil.getURI(binding).fragment());
        assertThat(relationship, is(notNullValue()));
    }
    
    @Test
    public void notConvertInformationFlow() throws Exception {
        when(model.getOwnedElements()).thenReturn(new INamedElement[] {});

        EList<Element> elements = new BasicEList<Element>();
        InformationFlow flow = createInformationFlow("test");
        elements.add(flow);
        when(element.getOwnedElements()).thenReturn(elements);

        CommonModelConverter converter = new CommonModelConverter(helper, converteds, relationships);
        converter.setAstahAPIUtil(util);
        converter.convert(model, element);

        Relationship relationship = relationships.get(EcoreUtil.getURI(flow).fragment());
        assertThat(relationship, is(nullValue()));
        
    }
    
    @Test
    public void notConvertExtend() throws Exception {
        notConvertRelationPattern(createExtend("test"));
    }
    
    @Test
    public void notConvertInclude() throws Exception {
        notConvertRelationPattern(createInclude("name"));
    }

    private void notConvertRelationPattern(Relationship target) throws InvalidEditingException,
            ClassNotFoundException {
        when(model.getOwnedElements()).thenReturn(new INamedElement[] {});

        EList<Element> elements = new BasicEList<Element>();
        elements.add(target);
        when(element.getOwnedElements()).thenReturn(elements);

        CommonModelConverter converter = new CommonModelConverter(helper, converteds, relationships);
        converter.setAstahAPIUtil(util);
        converter.convert(model, element);

        Relationship relationship = relationships.get(EcoreUtil.getURI(target).fragment());
        assertThat(relationship, is(nullValue()));
    }


    @Test
    public void convertPrimitive() throws Exception {
        convertClassPattern(createPrimitiveType("test"));
    }

    @Test
    public void notConvertSamePrimitive() throws Exception {
        when(model.getOwnedElements()).thenReturn(new INamedElement[] {});

        EList<Element> elements = new BasicEList<Element>();
        PrimitiveType primitiveType = createPrimitiveType("boolean");
        elements.add(primitiveType);
        when(element.getOwnedElements()).thenReturn(elements);

        CommonModelConverter converter = new CommonModelConverter(helper, converteds, relationships);
        converter.setAstahAPIUtil(util);
        converter.convert(model, element);

        verify(basicModelEditor, never()).createClass(eq(model), eq("boolean"));
    }

    @Test
    public void convertDataType() throws Exception {
        convertClassPattern(createDataType("test"));
    }

    @Test
    public void notConvertSameDataType() throws Exception {
        when(model.getOwnedElements()).thenReturn(new INamedElement[] {});

        EList<Element> elements = new BasicEList<Element>();
        DataType dataType = createDataType("boolean");
        elements.add(dataType);
        when(element.getOwnedElements()).thenReturn(elements);

        CommonModelConverter converter = new CommonModelConverter(helper, converteds, relationships);
        converter.setAstahAPIUtil(util);
        converter.convert(model, element);

        verify(basicModelEditor, never()).createClass(eq(model), eq("boolean"));
    }

    @Test
    public void convertSameNameClass() throws Exception {
        INamedElement owned = mock(IClass.class);
        when(owned.getName()).thenReturn("test");
        when(model.getOwnedElements()).thenReturn(new INamedElement[] { owned });

        EList<Element> elements = new BasicEList<Element>();
        Class clazz = createClass("test");
        elements.add(clazz);
        when(element.getOwnedElements()).thenReturn(elements);

        CommonModelConverter converter = new CommonModelConverter(helper, converteds, relationships);
        converter.setAstahAPIUtil(util);
        converter.convert(model, element);
        verify(basicModelEditor).createClass(eq(model), eq("test0"));
    }

    @Test
    public void norConvertUseCase() throws Exception {
        notConvertPattern(createUseCase("test"));
    }

    @Test
    public void notConvertActor() throws Exception {
        notConvertPattern(createActor("test"));
    }

    @Test
    public void notConvertState() throws Exception {
        notConvertPattern(createState("test"));
    }

    @Test
    public void notConvertNode() throws Exception {
        notConvertPattern(createNode("test"));
    }

    @Test
    public void notConvertComponent() throws Exception {
        notConvertPattern(createComponent("test"));
    }

    @Test
    public void notConvertCollabration() throws Exception {
        notConvertPattern(createCollaboration("test"));
    }

    @Test
    public void notConvertArtifact() throws Exception {
        notConvertPattern(createArtifact("test"));
    }

    @Test
    public void notConvertInformationItem() throws Exception {
        notConvertPattern(createInformationItem());
    }

    @Test
    public void notConvertDeploymentSpecification() throws Exception {
        notConvertPattern(createDeploymentSpecification("name"));
    }

    @Test
    public void notConvertActivity() throws Exception {
        notConvertPattern(createActivity("name"));
    }

    @Test
    public void notConvertInteraction() throws Exception {
        notConvertPattern(createInteraction("name"));
    }
    
    @Test
    public void notConvertOpaqueBehavior() throws Exception {
        notConvertPattern(createOpaqueBehavior("name"));
    }
    
    @Test
    public void notConvertStateMachine() throws Exception {
        notConvertPattern(createStateMachine("name"));
    }

    @Test
    public void notConvertSignal() throws Exception {
        notConvertPattern(createSignal("name"));
    }

    private void convertClassPattern(Element target) throws InvalidEditingException,
            ClassNotFoundException {
        when(model.getOwnedElements()).thenReturn(new INamedElement[] {});

        EList<Element> elements = new BasicEList<Element>();
        elements.add(target);
        when(element.getOwnedElements()).thenReturn(elements);
        IClass clazz = mock(IClass.class);
        when(basicModelEditor.createClass(model, "test")).thenReturn(clazz);

        CommonModelConverter converter = new CommonModelConverter(helper, converteds, relationships);
        converter.setAstahAPIUtil(util);
        converter.convert(model, element);

        verify(basicModelEditor).createClass(model, "test");
        verify(helper).setStereotype(eq(target), org.mockito.Matchers.any(IClass.class));
        assertThat(converteds.size(), is(1));
    }

    private void notConvertPattern(Element target) throws InvalidEditingException,
            ClassNotFoundException {
        when(model.getOwnedElements()).thenReturn(new INamedElement[] {});

        EList<Element> elements = new BasicEList<Element>();
        elements.add(target);
        when(element.getOwnedElements()).thenReturn(elements);

        CommonModelConverter converter = new CommonModelConverter(helper, converteds, relationships);
        converter.setAstahAPIUtil(util);
        converter.convert(model, element);

        verify(basicModelEditor, never()).createClass(model, "test");
        verify(helper, never()).setStereotype((Element) any(), (IElement) any());
    }

}
