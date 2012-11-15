package com.change_vision.astah.xmi;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IClassDiagram;
import com.change_vision.jude.api.inf.model.IDiagram;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.presentation.IPresentation;

public class XmiImporterAttributeTest extends BasicXmiImporterTest {

	@BeforeClass
	public static void setup() throws IOException, LicenseNotFoundException,
			ProjectNotFoundException, ProjectLockedException, ClassNotFoundException,
			InvalidEditingException, InvalidUsingException {
		BasicXmiImporterTest.setup("attribute");
	}
	
	@Test
	public void checkClass1() throws ClassNotFoundException, ProjectNotFoundException {
		IClass cls1 = findRootSubElement("Class1", IClass.class);
		assertNotNull(cls1);
		IAttribute[] attributes = cls1.getAttributes();
		assertEquals(6, attributes.length);
		assertEquals("attrWithAlias", attributes[0].getName());
		//assertEquals("alias for attrWithAlias", attributes[0].getAlias1());
		assertEquals("attrWithMultiplicity", attributes[1].getName());
		assertEquals(1, attributes[1].getMultiplicity().length);
		assertEquals(1, attributes[1].getMultiplicity()[0].getLower());
		assertEquals(10, attributes[1].getMultiplicity()[0].getUpper());
		assertEquals("attrWithNote", attributes[2].getName());
//		assertEquals("this is attrWithNote's notes", attributes[2].getDefinition());
		assertEquals("constAttr", attributes[3].getName());
		//const attrbiute in astah?
		assertEquals("initializedAttr", attributes[4].getName());
		assertEquals("19", attributes[4].getInitialValue());
		assertEquals("staticAttr", attributes[5].getName());
		assertTrue(attributes[5].isStatic());
	}
	
	@Test
	public void checkClass2() throws ClassNotFoundException, ProjectNotFoundException {
		IClass cls2 = findRootSubElement("Class2", IClass.class);
		assertNotNull(cls2);
		IAttribute[] attributes = cls2.getAttributes();
		assertEquals(12, attributes.length);
		assertNameAndType(attributes[0], "anyAttr", "any");
		assertNameAndType(attributes[1], "boolAttr", "bool");
		assertNameAndType(attributes[2], "booleanAttr0", "boolean");
		assertNameAndType(attributes[3], "byteAttr", "byte");
		assertNameAndType(attributes[4], "charAttr", "char");
		assertNameAndType(attributes[5], "Class1Attr", "Class1");
		assertNameAndType(attributes[6], "doubleAttr", "double");
		assertNameAndType(attributes[7], "floatAttr", "float");
		assertNameAndType(attributes[8], "intAttr", "int");
		assertNameAndType(attributes[9], "longAttr", "long");
		assertNameAndType(attributes[10], "nonAttr", "int");
		assertNameAndType(attributes[11], "shortAttr", "short");
	}
	
	@Test
	public void checkClass3() throws ClassNotFoundException, ProjectNotFoundException {
		IClass cls1 = findRootSubElement("Class3", IClass.class);
		assertNotNull(cls1);
		IAttribute[] attributes = cls1.getAttributes();
		assertEquals(3, attributes.length);
		assertEquals("attrWithConstraint", attributes[0].getName());
//		IConstraint[] constraints = attributes[0].getConstraints();
//		assertEquals(2, constraints.length);
//		assertEquals("cons1", constraints[0].getSpecification());
//		assertEquals("cons2", constraints[1].getSpecification());
		
		assertEquals("attrWithStereotype", attributes[1].getName());
		//assertArrayEquals(new String[] {"pk", "fk"}, attributes[1].getStereotypes());
		assertArrayEquals(new String[] {}, attributes[1].getStereotypes());
		
		assertEquals("attrWithTag", attributes[2].getName());
//		ITaggedValue[] tvs = attributes[2].getTaggedValues();
//		assertEquals(3, tvs.length);
//		assertEquals("tag1", tvs[0].getKey());
//		assertEquals("val1", tvs[0].getValue());
//		assertEquals("tag2", tvs[1].getKey());
//		assertEquals("val2", tvs[1].getValue());
//		assertEquals("tag3", tvs[2].getKey());
//		assertEquals("val3", tvs[2].getValue());
	}
	
	@Test
	public void checkClass4() throws ClassNotFoundException, ProjectNotFoundException {
		IClass cls1 = findRootSubElement("Class4", IClass.class);
		assertNotNull(cls1);
		IAttribute[] attributes = cls1.getAttributes();
		assertEquals(4, attributes.length);
		assertEquals("packageAttr", attributes[0].getName());
		assertTrue(attributes[0].isPackageVisibility());
		assertEquals("privateAttr", attributes[1].getName());
		assertTrue(attributes[1].isPrivateVisibility());
		assertEquals("protectedAttr", attributes[2].getName());
		assertTrue(attributes[2].isProtectedVisibility());
		assertEquals("publicAttr", attributes[3].getName());
		assertTrue(attributes[3].isPublicVisibility());
	}
	
	@Test
	@Ignore(value="現バージョンではプレゼンテーションはサポートしません。")
	public void checkDiagram() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IPackage root = getRoot();
		assertNotNull(root);
		
		IClassDiagram clsDgm = findNamedElement(root.getDiagrams(), "Model", IClassDiagram.class);
		assertNotNull(clsDgm);
		assertEquals(5, clsDgm.getPresentations().length);	//including frame
		
		assertPresentationNameAndType(clsDgm, "Class1", "Class");
		assertPresentationNameAndType(clsDgm, "Class2", "Class");
		assertPresentationNameAndType(clsDgm, "Class3", "Class");
		assertPresentationNameAndType(clsDgm, "Class4", "Class");
	}

	private void assertNameAndType(IAttribute attr, String name, String type) {
		assertNotNull(attr);
		assertEquals(name, attr.getName());
		assertEquals(type, attr.getType().getName());
	}
	
	private void assertPresentationNameAndType(IDiagram dgm, String name, String type) throws InvalidUsingException {
		IPresentation cls1Ps = findPresentation(dgm, name);
		assertNotNull(cls1Ps);
		assertEquals("Class", cls1Ps.getType());
	}
}
