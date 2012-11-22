package util;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Actor;
import org.eclipse.uml2.uml.Artifact;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Collaboration;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.DeploymentSpecification;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Extend;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Include;
import org.eclipse.uml2.uml.InformationFlow;
import org.eclipse.uml2.uml.InformationItem;
import org.eclipse.uml2.uml.Interaction;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Node;
import org.eclipse.uml2.uml.OpaqueBehavior;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Realization;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.TemplateBinding;
import org.eclipse.uml2.uml.TemplateSignature;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.Usage;
import org.eclipse.uml2.uml.UseCase;

public class UML2TestUtil {
    
    private static UMLFactory instance = UMLFactory.eINSTANCE;

    public static Class createClass(String name) {
        Class clazz = instance.createClass();
        clazz.setName(name);
        return clazz;
    }
    
    public static Property createProperty(String name){
        Property p = instance.createProperty();
        p.setName(name);
        return p;
    }
    
    public static EnumerationLiteral createEnumerationLiteral(String name){
        EnumerationLiteral e = instance.createEnumerationLiteral();
        e.setName(name);
        return e;
    }
    
    public static Enumeration createEnumeration(String name){
        Enumeration e = instance.createEnumeration();
        e.setName(name);
        return e;
    }
    
    public static Operation createOperation(String name){
        Operation o = instance.createOperation();
        o.setName(name);
        return o;
    }
    
    public static Parameter createParameter(String name){
        Parameter p = instance.createParameter();
        p.setName(name);
        return p;
    }
    
    public static Interface createInterface(String name){
        Interface intr = instance.createInterface();
        intr.setName(name);
        return intr;
    }
    
    public static Package createPackage(String name){
        Package pack = instance.createPackage();
        pack.setName(name);
        return pack;
    }
    
    public static Association createAssociation(String name){
        Association association = instance.createAssociation();
        association.setName(name);
        return association;
    }
    
    public static AssociationClass createAssociationClass(String name){
        AssociationClass associationClass = instance.createAssociationClass();
        associationClass.setName(name);
        return associationClass;
    }
    
    public static PrimitiveType createPrimitiveType(String name){
        PrimitiveType primitiveType = instance.createPrimitiveType();
        primitiveType.setName(name);
        return primitiveType;
    }
    
    public static DataType createDataType(String name){
        DataType dataType = instance.createDataType();
        dataType.setName(name);
        return dataType;
    }
    
    public static Generalization createGeneralization(){
        Generalization gen = instance.createGeneralization();
        return gen;
    }
    
    public static Dependency createDependency(String name){
        Dependency dep = instance.createDependency();
        dep.setName(name);
        return dep;
    }
    
    public static Usage createUsage(String name){
        Usage usage = instance.createUsage();
        usage.setName(name);
        return usage;
    }
    
    public static UseCase createUseCase(String name){
        UseCase useCase = instance.createUseCase();
        useCase.setName(name);
        return useCase;
    }
    
    public static Actor createActor(String name){
        Actor actor = instance.createActor();
        actor.setName(name);
        return actor;
    }
    
    public static State createState(String name){
        State state = instance.createState();
        state.setName(name);
        return state;
    }
    
    public static Node createNode(String name){
        Node node = instance.createNode();
        node.setName(name);
        return node;
    }
    
    public static Stereotype createStereotype(String name){
        Stereotype stereotype = instance.createStereotype();
        stereotype.setName(name);
        return stereotype;
    }
    
    public static Component createComponent(String name){
        Component component = instance.createComponent();
        component.setName(name);
        return component;
    }
    
    public static Collaboration createCollaboration(String name){
        Collaboration collaboration = instance.createCollaboration();
        collaboration.setName(name);
        return collaboration;
    }
    
    public static Artifact createArtifact(String name){
        Artifact artifact = instance.createArtifact();
        artifact.setName(name);
        return artifact;
    }
    
    public static InformationItem createInformationItem(){
        InformationItem item = instance.createInformationItem();
        return item;
    }
    
    public static DeploymentSpecification createDeploymentSpecification(String name){
        DeploymentSpecification spec = instance.createDeploymentSpecification();
        spec.setName(name);
        return spec;
    }
    
    public static Signal createSignal(String name){
        Signal signal = instance.createSignal();
        signal.setName(name);
        return signal;
    }
    
    public static TemplateSignature createTemplateSignature(){
        TemplateSignature signature = instance.createTemplateSignature();
        return signature;
    }
    
    public static TemplateBinding createTemplateBinding(){
        TemplateBinding binding = instance.createTemplateBinding();
        return binding;
    }
    
    
    public static Activity createActivity(String name){
       Activity activity = instance.createActivity();
       activity.setName(name);
       return activity;
    }
    
    public static Interaction createInteraction(String name){
        Interaction interaction = instance.createInteraction();
        interaction.setName(name);
        return interaction;
    }
    
    public static OpaqueBehavior createOpaqueBehavior(String name){
        OpaqueBehavior behavior = instance.createOpaqueBehavior();
        behavior.setName(name);
        return behavior;
    }
    
    public static StateMachine createStateMachine(String name){
        StateMachine stateMachine = instance.createStateMachine();
        stateMachine.setName(name);
        return stateMachine;
    }
    
    public static Extend createExtend(String name){
        Extend extend = instance.createExtend();
        extend.setName(name);
        return extend;
    }
    
    public static Include createInclude(String name){
        Include include = instance.createInclude();
        include.setName(name);
        return include;
    }
    
    public static Realization createRealization(String name){
        Realization realization = instance.createRealization();
        realization.setName(name);
        return realization;
    }
        
    public static InformationFlow createInformationFlow(String name){
        InformationFlow flow = instance.createInformationFlow();
        flow.setName(name);
        return flow;
    }
    
}
