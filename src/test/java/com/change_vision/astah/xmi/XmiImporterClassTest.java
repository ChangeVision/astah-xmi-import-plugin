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
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IClassDiagram;
import com.change_vision.jude.api.inf.model.IHyperlink;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.model.ITaggedValue;

public class XmiImporterClassTest extends BasicXmiImporterTest {


	@BeforeClass
	public static void setup() throws IOException, LicenseNotFoundException,
			ProjectNotFoundException, ProjectLockedException, ClassNotFoundException,
			InvalidEditingException, InvalidUsingException {
		BasicXmiImporterTest.setup("class");
	}
	
	@Test
	public void checkControl() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		checkIconClass("Control1", "control");
	}
	
	@Test
	public void checkEntity() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		checkIconClass("Entity1", "entity");
	}
	
	@Test
	public void checkBoundary() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		checkIconClass("Boundary1", "boundary");
	}
	
	@Test
	public void checkInterface1() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls = findRootSubElement("Interface1", IClass.class);
		assertArrayEquals(new String[]{"interface"}, cls.getStereotypes());
	}
	
	@Test
	public void checkInterface2() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		//checkNotationClass("Interface2", "normal", "interface");
		IClass cls = findRootSubElement("Interface2", IClass.class);
		assertArrayEquals(new String[]{"interface"}, cls.getStereotypes());
	}
	
	@Test
	public void checkNormalStereotypeClass() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		checkNotationClass("ClassWithStereotype", "normal", "my1", "my2");
	}
	
	@Test
	@Ignore(value="現バージョンでは別名はサポートしません。")
	public void checkAlias() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls = findRootSubElement("ClassWithAlias", IClass.class);
		assertNotNull(cls);
		assertEquals("aliasForClass", cls.getAlias1());
	}
	
	@Test
	@Ignore(value="現バージョンではプレゼンテーションはサポートしません。")
	public void checkNote() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls = findRootSubElement("ClassWithNote", IClass.class);
		assertNotNull(cls);
		assertEquals("note for class", cls.getDefinition());
	}
	
	@Test
	public void checkPublicClass() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls = findRootSubElement("PublicClass", IClass.class);
		assertNotNull(cls);
		assertTrue(cls.isPublicVisibility());
	}
	
	@Test
	public void checkPackageClass() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls = findRootSubElement("PackageClass", IClass.class);
		assertNotNull(cls);
		assertTrue(cls.isPackageVisibility());
	}
	
	@Test
	public void checkProtectedClass() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls = findRootSubElement("ProtectedClass", IClass.class);
		assertNotNull(cls);
		assertTrue(cls.isProtectedVisibility());
	}
	
	@Test
	public void checkPrivateClass() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls = findRootSubElement("PrivateClass", IClass.class);
		assertNotNull(cls);
		assertTrue(cls.isPrivateVisibility());
	}
	
	@Test
	public void checkAbstractClass() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls = findRootSubElement("AbstractClass", IClass.class);
		assertNotNull(cls);
		assertTrue(cls.isAbstract());
	}
	
	@Test
	public void checkLeafClass() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls = findRootSubElement("LeafClass", IClass.class);
		assertNotNull(cls);
		assertTrue(cls.isLeaf());
	}
	
	@Test
	public void checkActiveClass() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls = findRootSubElement("ActiveClass", IClass.class);
		assertNotNull(cls);
		assertTrue(cls.isActive());
	}
	
	@Test
	@Ignore(value="現バージョンではハイパーリンクはサポートしません。")
	public void checkClassWithHyperlinks() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls = findRootSubElement("ClassWithHyperlinks", IClass.class);
		assertNotNull(cls);
		IHyperlink[] hyperlinks = cls.getHyperlinks();
		assertEquals(2, hyperlinks.length);
		assertTrue(hyperlinks[0].isFile());
		assertEquals("test.txt", hyperlinks[0].getName());
		assertEquals("C:", hyperlinks[0].getPath());
		assertEquals("note1", hyperlinks[0].getComment());
		assertTrue(hyperlinks[1].isURL());
		assertEquals("www.change-vision.com", hyperlinks[1].getName());
		assertEquals("website for cv", hyperlinks[1].getComment());
	}
	
	@Test
	@Ignore(value="現バージョンではタグ付き値はサポートしません。")
	public void checkClassWithTaggedValues() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls = findRootSubElement("ClassWithTaggedValue", IClass.class);
		assertNotNull(cls);
		ITaggedValue[] taggedValues = cls.getTaggedValues();
		assertEquals(2, taggedValues.length);
		assertEquals("tag1", taggedValues[0].getKey());
		assertEquals("value1", taggedValues[0].getValue());
		assertEquals("tag2", taggedValues[1].getKey());
		assertEquals("value2", taggedValues[1].getValue());
	}
	
	@Test
	@Ignore(value="現バージョンではプレゼンテーションはサポートしません。")
	public void checkDiagram() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IPackage root = getRoot();
		assertNotNull(root);
		
		IClassDiagram clsDgm = findNamedElement(root.getDiagrams(), "Model", IClassDiagram.class);
		assertNotNull(clsDgm);
		assertEquals(18, clsDgm.getPresentations().length);	//including frame
	}
	
	private void checkIconClass(String name, String... stereotype) throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		checkNotationClass(name, "icon", stereotype);
	}
	
	private void checkNotationClass(String name, String notationType, String... stereotype) throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls = findRootSubElement(name, IClass.class);
		assertNotNull(cls);
		//assertArrayEquals(stereotype, cls.getStereotypes());
		assertArrayEquals(new String[]{}, cls.getStereotypes());
		
//		assertEquals(1, cls.getPresentations().length);
//		INodePresentation clsPs = (INodePresentation) cls.getPresentations()[0];
//		assertEquals(notationType, clsPs.getProperty("notation_type"));
	}
}
