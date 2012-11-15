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
import com.change_vision.jude.api.inf.model.IHyperlink;
import com.change_vision.jude.api.inf.model.IPackage;

public class XmiImporterPackageTest extends BasicXmiImporterTest {

	@BeforeClass
	public static void setup() throws IOException, LicenseNotFoundException,
			ProjectNotFoundException, ProjectLockedException, ClassNotFoundException,
			InvalidEditingException, InvalidUsingException {
		BasicXmiImporterTest.setup("package");
	}
	
	@Test
	public void checkPackageContainer() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IPackage pkg1 = findRootSubElement("Package1", IPackage.class);
		assertNotNull(pkg1);
		
		IPackage pkg2 = findNamedElement(pkg1.getOwnedElements(), "Package2", IPackage.class);
		assertNotNull(pkg2);
		
		IClass cls2 = findNamedElement(pkg2.getOwnedElements(), "Class2", IClass.class);
		assertNotNull(cls2);
		
//		INodePresentation pkg1Ps = assertPresentation(pkg1, "Package", null);
//		INodePresentation pkg2Ps = assertPresentation(pkg2, "Package", pkg1Ps);
//		INodePresentation cls2Ps = assertPresentation(cls2, "Class", pkg2Ps);
		
		IPackage pkg3 = findRootSubElement("Package3", IPackage.class);
		assertNotNull(pkg3);
		
		IClass cls3 = findRootSubElement("Class3", IClass.class);
		assertNotNull(cls3);
		
//		INodePresentation pkg3Ps = assertPresentation(pkg3, "Package", null);
//		INodePresentation cls3Ps = assertPresentation(cls3, "Class", null);

//		assertArrayEquals(new INodePresentation[] {pkg2Ps}, pkg1Ps.getChildren());
//		assertArrayEquals(new INodePresentation[] {cls2Ps}, pkg2Ps.getChildren());
//		assertEquals(0, pkg3Ps.getChildren().length);
//		assertEquals(0, cls2Ps.getChildren().length);
//		assertEquals(0, cls3Ps.getChildren().length);
		
//		assertEquals(0, pkg1Ps.getLinks().length);
//		assertEquals(0, pkg2Ps.getLinks().length);
//		assertEquals(0, pkg3Ps.getLinks().length);
//		assertEquals(0, cls2Ps.getLinks().length);
//		assertEquals(0, cls3Ps.getLinks().length);
	}
	
	@Test
	public void checkPackageStereotype() throws ClassNotFoundException, ProjectNotFoundException {
		IPackage pkg = findRootSubElement("pkgStereotype", IPackage.class);
		assertNotNull(pkg);
		//assertArrayEquals(new String[] {"stub", "model"}, pkg.getStereotypes());
		assertArrayEquals(new String[] {}, pkg.getStereotypes());
	}
	
	@Test
	@Ignore(value="現バージョンでは別名はサポートしません。")
	public void checkPackageAlias() throws ClassNotFoundException, ProjectNotFoundException {
		IPackage pkg = findRootSubElement("pkgAlias", IPackage.class);
		assertNotNull(pkg);
		assertEquals("packageAlias", pkg.getAlias1());
	}
	
	@Test
	@Ignore(value="現バージョンではプレゼンテーションはサポートしません。")
	public void checkPackageNote() throws ClassNotFoundException, ProjectNotFoundException {
		IPackage pkg = findRootSubElement("pkgNote", IPackage.class);
		assertNotNull(pkg);
		assertEquals("note6", pkg.getDefinition());
	}
	
	@Test
	@Ignore(value="現バージョンではタグ付き値はサポートしません。")
	public void checkPackageTag() throws ClassNotFoundException, ProjectNotFoundException {
		IPackage pkg = findRootSubElement("pkgTag", IPackage.class);
		assertNotNull(pkg);
		assertEquals(2, pkg.getTaggedValues().length);
		assertEquals("tag1", pkg.getTaggedValues()[0].getKey());
		assertEquals("tag2", pkg.getTaggedValues()[1].getKey());
		assertEquals("val1", pkg.getTaggedValues()[0].getValue());
		assertEquals("val2", pkg.getTaggedValues()[1].getValue());
	}
	
	@Test
	@Ignore(value="現バージョンでは制約はサポートしません。")
	public void checkPackageConstraint() throws ClassNotFoundException, ProjectNotFoundException {
		IPackage pkg = findRootSubElement("pkgTag", IPackage.class);
		assertNotNull(pkg);
		assertEquals(0, pkg.getConstraints().length);//Astah not support constraint for package
	}
	
	@Test
	@Ignore(value="現バージョンではハイパーリンクはサポートしません。")
	public void checkPackageWithFile() throws ClassNotFoundException, ProjectNotFoundException {
		IPackage pkg = findRootSubElement("pkgWithFile", IPackage.class);
		assertNotNull(pkg);
		IHyperlink[] hyperlinks = pkg.getHyperlinks();
		assertEquals(2, hyperlinks.length);
		assertTrue(hyperlinks[0].isURL());
		assertEquals("astah.net", hyperlinks[0].getName());
		assertTrue(hyperlinks[1].isFile());
		assertEquals("tmp.txt", hyperlinks[1].getName());
		assertEquals("", hyperlinks[1].getPath());
	}
}
