package com.change_vision.astah.xmi.internal.convert;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.InformationFlow;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.PackageMerge;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Realization;
import org.eclipse.uml2.uml.TemplateBinding;
import org.eclipse.uml2.uml.TemplateSignature;
import org.eclipse.uml2.uml.Usage;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IAssociation;
import com.change_vision.jude.api.inf.model.IAssociationClass;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IDependency;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IGeneralization;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.model.IRealization;
import com.change_vision.jude.api.inf.model.ITemplateBinding;
import com.change_vision.jude.api.inf.model.IUsage;

public class RelationConverterTest {

    @Mock
    private Element element;

    @Mock
    private IModel model;

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
    private Package sourcePackage;
    
    @Mock
    private IPackage sourcePackageConvertedElement;

    @Mock
    private Package targetPackage;
    
    @Mock
    private IPackage targetPackageConvertedElement;
    
    private HashMap<Element, IElement> converteds;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(util.getBasicModelEditor()).thenReturn(basicModelEditor);

        converteds = new HashMap<Element, IElement>();
    }

    @Test
    public void notConvertWithNull() throws InvalidEditingException {
        RelationConverter converter = new RelationConverter(converteds, util);
        INamedElement element = converter.convert(null);
        assertThat(element,is(nullValue()));
    }
    
    @Test
    public void notConvertNotHaveTarget() throws Exception {
        converteds.put(sourceClassifier, sourceClassifierConvertedElement);
        Generalization generalization = mock(Generalization.class);
        when(generalization.getSpecific()).thenReturn(sourceClassifier);
        
        assertThat(UMLUtil.getSource(generalization),is(notNullValue()));
        assertThat(UMLUtil.getTarget(generalization),is(nullValue()));
        
        RelationConverter converter = new RelationConverter(converteds, util);
        INamedElement element = converter.convert(generalization);
        assertThat(element,is(nullValue()));
    }
    
    @Test
    public void notConvertNotHaveSource() throws Exception {
        converteds.put(targetClassifier, targetClassifierConvertedElement);
        Generalization generalization = mock(Generalization.class);
        when(generalization.getGeneral()).thenReturn(targetClassifier);
        
        assertThat(UMLUtil.getSource(generalization),is(nullValue()));
        assertThat(UMLUtil.getTarget(generalization),is(notNullValue()));
        
        RelationConverter converter = new RelationConverter(converteds, util);
        INamedElement element = converter.convert(generalization);
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
        
        RelationConverter converter = new RelationConverter(converteds, util);
        INamedElement element = converter.convert(generalization);
        assertThat(element,is(notNullValue()));
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
        
        RelationConverter converter = new RelationConverter(converteds, util);
        INamedElement element = converter.convert(realization);
        assertThat(element,is(notNullValue()));
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

    @Test
    public void convertAssociationClass() throws Exception {
        converteds.put(sourceClassifier, sourceClassifierConvertedElement);
        converteds.put(targetClassifier, targetClassifierConvertedElement);

        AssociationClass association = mock(AssociationClass.class);
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
        
        when(association.getMemberEnds()).thenReturn(members);
        
        IAssociationClass created = mock(IAssociationClass.class);
        IAttribute sourceAttribute = mock(IAttribute.class);
        IAttribute targetAttribute = mock(IAttribute.class);
        IAttribute[] attributes = new IAttribute[]{
                sourceAttribute ,
                targetAttribute
        };
        
        when(created.getMemberEnds()).thenReturn(attributes );
        when(basicModelEditor.createAssociationClass(eq(sourceClassifierConvertedElement), eq(targetClassifierConvertedElement), eq("association"), eq("dummySource"), eq("dummyTarget"))).thenReturn(created);
        
        RelationConverter converter = new RelationConverter(converteds, util);

        assertThat(converteds.containsValue(sourceAttribute),is(false));
        assertThat(converteds.containsValue(targetAttribute),is(false));
        
        INamedElement element = converter.convert(association);
        
        assertThat(element,is(notNullValue()));
        assertThat(converteds.containsValue(sourceAttribute),is(true));
        assertThat(converteds.containsValue(targetAttribute),is(true));
    }
    
    @Test
    public void convertPackageImport() throws Exception {
        converteds.put(sourcePackage, sourcePackageConvertedElement);
        converteds.put(targetPackage, targetPackageConvertedElement);

        IDependency created = mock(IDependency.class);
        when(basicModelEditor.createDependency(eq(sourcePackageConvertedElement), eq(targetPackageConvertedElement), eq(""))).thenReturn(created );

        RelationConverter converter = new RelationConverter(converteds, util);
        PackageImport rel = mock(PackageImport.class);
        when(rel.getImportedPackage()).thenReturn(sourcePackage);
        when(rel.getImportingNamespace()).thenReturn(targetPackage);
        INamedElement element = converter.convert(rel);
        assertThat(element,is(notNullValue()));        
    }
    
    @Test
    public void convertPackageMerge() throws Exception {
        converteds.put(sourcePackage, sourcePackageConvertedElement);
        converteds.put(targetPackage, targetPackageConvertedElement);

        IDependency created = mock(IDependency.class);
        when(basicModelEditor.createDependency(eq(sourcePackageConvertedElement), eq(targetPackageConvertedElement), eq(""))).thenReturn(created );

        RelationConverter converter = new RelationConverter(converteds, util);
        PackageMerge rel = mock(PackageMerge.class);
        when(rel.getMergedPackage()).thenReturn(sourcePackage);
        when(rel.getReceivingPackage()).thenReturn(targetPackage);
        INamedElement element = converter.convert(rel);
        assertThat(element,is(notNullValue()));        
    }
    
    @Test
    public void convertDependency() throws Exception {
        converteds.put(sourcePackage, sourcePackageConvertedElement);
        converteds.put(targetPackage, targetPackageConvertedElement);

        Dependency dependency = mock(Dependency.class);
        when(dependency.getName()).thenReturn("dependency");
        when(dependency.isSetName()).thenReturn(true);

        EList<NamedElement> clients = mock(EList.class);
        when(clients.get(0)).thenReturn(targetPackage);
        when(dependency.getClients()).thenReturn(clients);
        
        EList<NamedElement> suppliers = mock(EList.class);
        when(suppliers.get(0)).thenReturn(sourcePackage);
        when(dependency.getSuppliers()).thenReturn(suppliers);

        IDependency created = mock(IDependency.class);
        when(basicModelEditor.createDependency(eq(sourcePackageConvertedElement), eq(targetPackageConvertedElement), eq("dependency"))).thenReturn(created );

        RelationConverter converter = new RelationConverter(converteds, util);
        INamedElement element = converter.convert(dependency);
        assertThat(element,is(notNullValue()));        
    }
    
    @Test
    @Ignore
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
        
        RelationConverter converter = new RelationConverter(converteds, util);
        INamedElement element = converter.convert(binding);
        assertThat(element,is(notNullValue()));
    }

    
    @Test
    public void notConvertInformationFlow() throws Exception {
        converteds.put(sourcePackage, sourcePackageConvertedElement);
        converteds.put(targetPackage, targetPackageConvertedElement);

        InformationFlow flow = mock(InformationFlow.class);

        EList<NamedElement> sources = mock(EList.class);
        when(sources.isEmpty()).thenReturn(false);
        when(sources.get(0)).thenReturn(sourcePackage);
        when(flow.getInformationSources()).thenReturn(sources);

        EList<NamedElement> targets = mock(EList.class);
        when(targets.isEmpty()).thenReturn(false);
        when(targets.get(0)).thenReturn(targetPackage);
        when(flow.getInformationTargets()).thenReturn(targets);

        IDependency created = mock(IDependency.class);
        when(basicModelEditor.createDependency(eq(sourcePackageConvertedElement), eq(targetPackageConvertedElement), eq(""))).thenReturn(created);

        RelationConverter converter = new RelationConverter(converteds, util);
        INamedElement element = converter.convert(flow);
        assertThat(element,is(nullValue()));
        
        
    }

}
