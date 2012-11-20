package com.change_vision.astah.xmi.convert;

import java.util.Map;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.PackageMerge;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Realization;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.Usage;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IAssociation;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.INamedElement;

public class RelationConverter {

    private Map<Element, IElement> converteds;
    private AstahAPIUtil util;

    public RelationConverter(Map<Element, IElement> converteds,AstahAPIUtil util) {
        this.converteds = converteds;
        this.util = util;
    }
    
    public INamedElement convert(Relationship rel) throws InvalidEditingException{
        INamedElement astahSource = (INamedElement) converteds.get(UMLUtil
                .getSource(rel));
        INamedElement astahTarget = (INamedElement) converteds.get(UMLUtil
                .getTarget(rel));
        if (astahSource == null || astahTarget == null) {
            return null;
        }
        INamedElement astahRel = null;
        String name = UMLUtil.getName(rel);
        if (astahSource instanceof IClass && astahTarget instanceof IClass) {
            IClass astahSourceClass = (IClass) astahSource;
            IClass astahTargetClass = (IClass) astahTarget;
            if (rel instanceof Generalization) {
                astahRel = getBasicModelEditor().createGeneralization(astahSourceClass, astahTargetClass, name);
            } else if (rel instanceof Realization) {
                astahRel = getBasicModelEditor().createRealization(astahTargetClass, astahSourceClass, name);
            } else if (rel instanceof Usage) {
                astahRel = getBasicModelEditor().createUsage(astahTargetClass, astahSourceClass, name);
            } else if (rel instanceof Association) {
                Property end0 = ((Association) rel).getMemberEnds().get(0);
                Property end1 = ((Association) rel).getMemberEnds().get(1);
                if (rel instanceof AssociationClass) {
                    astahRel = getBasicModelEditor().createAssociationClass(astahSourceClass, astahTargetClass,
                            name, UMLUtil.getName(end0), UMLUtil.getName(end1));
                } else {
                    astahRel = getBasicModelEditor().createAssociation(astahSourceClass, astahTargetClass,
                            name, UMLUtil.getName(end0), UMLUtil.getName(end1));
                }
                if (astahRel instanceof IAssociation) {
                    IAttribute[] memberEnds = ((IAssociation) astahRel)
                            .getMemberEnds();
                    converteds.put(end0, memberEnds[0]);
                    converteds.put(end1, memberEnds[1]);
                }
            }
        } else if (rel instanceof PackageImport || rel instanceof PackageMerge
                || rel instanceof Dependency ) {
            astahRel = getBasicModelEditor().createDependency(astahSource, astahTarget, name);
        }
        if (astahRel != null) {
            converteds.put(rel, astahRel);
        }
        return astahRel;
    }

    private BasicModelEditor getBasicModelEditor() {
        return util.getBasicModelEditor();
    }
    
}
