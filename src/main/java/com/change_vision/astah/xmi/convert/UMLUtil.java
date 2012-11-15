package com.change_vision.astah.xmi.convert;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.InformationFlow;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.PackageMerge;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.TemplateBinding;

public class UMLUtil {

	public static String getStereotypeString(Element e) {
		String stereotype = "";
		if (e instanceof Enumeration) {
			stereotype = "enum";
		} else if (e instanceof EnumerationLiteral) {
			stereotype = "enum constant";
		} else if (e instanceof PrimitiveType) {
			stereotype = "primitive";
		} else if (e instanceof Signal) {
			stereotype = "signal";
		} else if (e instanceof DataType) {
			stereotype = "dataType";
		} else if (e instanceof InformationFlow) {
			stereotype = "flow";
		} else if (e instanceof Interface) {
			stereotype = "interface";
		} else if (e instanceof PackageImport) {
			stereotype = "import";
		} else if (e instanceof PackageMerge) {
			stereotype = "merge";
		}
		return stereotype;
	}
	
	public static Element getSource(Relationship rel) {
		if (rel instanceof Generalization) {
			return ((Generalization) rel).getSpecific();
		} else if (rel instanceof PackageImport) {
			return ((PackageImport) rel).getImportedPackage();
		} else if (rel instanceof PackageMerge) {
			return ((PackageMerge) rel).getMergedPackage();
		} else if (rel instanceof Dependency) {
			return ((Dependency) rel).getSuppliers().get(0);
		} else if (rel instanceof InformationFlow) {
			if (!((InformationFlow) rel).getInformationSources().isEmpty()) {
				return ((InformationFlow) rel).getInformationSources().get(0);
			}
		} else if (rel instanceof Association) {
			Association ass = (Association) rel;
			if (!ass.getMemberEnds().isEmpty()) {
				Property role0 = ass.getMemberEnds().get(0);
				return role0.getType();
			}
		} else if (rel instanceof TemplateBinding) {
			return ((TemplateBinding) rel).getBoundElement();
		}
		return null;
	}
	
	public static Element getTarget(Relationship rel) {
		if (rel instanceof Generalization) {
			return ((Generalization) rel).getGeneral();
		} else if (rel instanceof PackageImport) {
			return ((PackageImport) rel).getImportingNamespace();
		} else if (rel instanceof PackageMerge) {
			return ((PackageMerge) rel).getReceivingPackage();
		} else if (rel instanceof Dependency) {
			return ((Dependency) rel).getClients().get(0);
		} else if (rel instanceof InformationFlow) {
			if (!((InformationFlow) rel).getInformationTargets().isEmpty()) {
				return ((InformationFlow) rel).getInformationTargets().get(0);
			}
		} else if (rel instanceof Association) {
			Association ass = (Association) rel;
			if (!ass.getMemberEnds().isEmpty()) {
				Property role0 = ass.getMemberEnds().get(1);
				return role0.getType();
			}
		} else if (rel instanceof TemplateBinding) {
			return ((TemplateBinding) rel).getSignature().getTemplate();
		}
		return null;
	}
	
	public static String getName(Element e) {
		if (e instanceof NamedElement) {
			NamedElement namedElement = (NamedElement) e;
			if (namedElement.isSetName()) {
				return namedElement.getName();
			}
		}
		return "";
	}
	
	public static Parameter getReturnParameter(Operation operation) {
		EList<Parameter> ownedParameters = operation.getOwnedParameters();
		for (int i = ownedParameters.size() - 1; i >= 0; i--) {
			Parameter param = ownedParameters.get(i);
			if (param.getDirection().equals(ParameterDirectionKind.RETURN_LITERAL)) {
				return param;
			}
		}
		return null;
	}
}
