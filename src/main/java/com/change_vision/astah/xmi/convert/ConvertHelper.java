package com.change_vision.astah.xmi.convert;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.VisibilityKind;

import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.editor.ModelEditorFactory;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IAssociation;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.INamedElement;

public class ConvertHelper {
	
	public ConvertHelper() {
		
	}
	
	public void setElementInfo(Element e, IElement element)
			throws InvalidEditingException, ClassNotFoundException {
		if (element instanceof IAssociation) {
			Association ass = (Association) e;
			IAssociation astahAss = (IAssociation) element;
			for (int i = 0; i < astahAss.getMemberEnds().length; i++) {
				IAttribute astahEnd = astahAss.getMemberEnds()[i];
				Property end = ass.getMemberEnds().get(i);
				astahEnd.setDerived(end.isDerived());
				BasicModelEditor bme = ModelEditorFactory.getBasicModelEditor();
				if (end.isOrdered()) {
					bme.createConstraint(astahEnd, "ordered");
				}
			}
		}
		setModelInformation(element, e);
	}
	
	private void setModelInformation(IElement m, Element e) throws InvalidEditingException, ClassNotFoundException {
		if (m instanceof INamedElement && e instanceof NamedElement) {
			
			VisibilityKind visibility = ((NamedElement) e).getVisibility();
			try {((INamedElement) m).setVisibility(visibility.getLiteral());} catch (InvalidEditingException ex) {}
			
			if (m instanceof IClass) {
				if (e instanceof Classifier) {
					Classifier cls = (Classifier) e;
					((IClass) m).setAbstract(cls.isAbstract());
					((IClass) m).setLeaf(cls.isLeaf());
				}
				if (e instanceof Class) {
					((IClass) m).setActive(((Class) e).isActive());
				}
			}
		}
		if (m instanceof IAssociation && e instanceof Association) {
			IAssociation ass = (IAssociation) m;
			for (int i = 0; i < 2; i++) {
				Property role = ((Association) e).getMemberEnds().get(i);
				IAttribute[] memberEnds = ass.getMemberEnds();
				if (role.isNavigable()) {
					memberEnds[i].setNavigability("Navigable");
				}
				memberEnds[i].setStatic(role.isStatic());
				if (role.isComposite()) {
					memberEnds[1 - i].setComposite();
				} else if (role.getAggregation().equals(AggregationKind.SHARED_LITERAL)) {
					memberEnds[1 - i].setAggregation();
				}
				if (role.getLowerValue() != null && role.getUpperValue() != null) {
					memberEnds[i].setMultiplicity(new int[][]{{role.getLower(), role.getUpper()}});
				}
			}
		}
		if (!(e instanceof Classifier)) {
			setStereotype(e, m);
		}
	}

	public void setStereotype(Element e, IElement newUMLModel)
			throws InvalidEditingException {
		if (e.getAppliedStereotypes().isEmpty()) {
			String stereotypeString = UMLUtil.getStereotypeString(e);
			if (!stereotypeString.equals("")) {
				newUMLModel.addStereotype(stereotypeString);
			}
		} else {
			for (Stereotype s : e.getAppliedStereotypes()) {
				String stereo = s.getName();
				if (stereo.equals("Interface")) {
					stereo = "interface";
				}
				newUMLModel.addStereotype(stereo);
			}
		}
	}
	
	public static boolean isValidData(String data) {
		return data != null && !data.equals("");
	}

}
