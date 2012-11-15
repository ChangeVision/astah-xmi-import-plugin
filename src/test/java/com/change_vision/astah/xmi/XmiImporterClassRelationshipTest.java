package com.change_vision.astah.xmi;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

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
import com.change_vision.jude.api.inf.model.IDependency;
import com.change_vision.jude.api.inf.model.IGeneralization;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.model.IRealization;
import com.change_vision.jude.api.inf.model.IUsage;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.presentation.IPresentation;

public class XmiImporterClassRelationshipTest extends BasicXmiImporterTest {

	@BeforeClass
	public static void setup() throws IOException, LicenseNotFoundException,
			ProjectNotFoundException, ProjectLockedException, ClassNotFoundException,
			InvalidEditingException, InvalidUsingException {
		BasicXmiImporterTest.setup("classRel");
	}
	
	@Test
	public void checkGeneralization() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {

		IClass cls1 = findRootSubElement("Class1", IClass.class);
		assertNotNull(cls1);
		IClass cls2 = findRootSubElement("Class2", IClass.class);
		assertNotNull(cls2);
		
		assertEquals(1, cls2.getGeneralizations().length);
		assertEquals(1, cls1.getSpecializations().length);
		assertEquals(cls1.getSpecializations()[0], cls2.getGeneralizations()[0]);
		
		IGeneralization gen = cls2.getGeneralizations()[0];
		//assertEquals("gen1", gen.getName());
		//assertArrayEquals(new String[] {"implementation", "my"}, gen.getStereotypes());
		assertArrayEquals(new String[] {}, gen.getStereotypes());
		
		//assertEquals("genAlias", gen.getAlias1());
//		assertEquals("gen note", gen.getDefinition());
		
//		IConstraint[] constraints = gen.getConstraints();
//		assertEquals(2, constraints.length);
//		assertEquals("con1", constraints[0].getName());
//		assertEquals("con2", constraints[1].getName());
//		
//		assertEquals(2, gen.getTaggedValues().length);
//		assertEquals("genVal1", gen.getTaggedValue("genTag1"));
		
	}
	
	@Test
	@Ignore(value="現バージョンではプレゼンテーションはサポートしません。")
	public void checkConstraintForGen() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {

		ILinkPresentation genPs = (ILinkPresentation) findPresentation("gen1");
		assertNotNull(genPs);

		INodePresentation notePs = (INodePresentation) findPresentation("{post1}");
		assertNotNull(notePs);
		
		ILinkPresentation[] links = notePs.getLinks();
		assertEquals(1, links.length);
		ILinkPresentation linkPs = links[0];
		assertEquals(genPs, linkPs.getTargetEnd());
	}
	
	@Test
	public void checkRealization() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {

		IClass interface1 = findRootSubElement("Interface1", IClass.class);
		assertNotNull(interface1);
		assertArrayEquals(new String[] {"interface"}, interface1.getStereotypes());
		IClass cls2 = findRootSubElement("Class2", IClass.class);
		assertNotNull(cls2);
		
		assertEquals(1, cls2.getClientRealizations().length);
		assertEquals(1, interface1.getSupplierRealizations().length);
		assertEquals(interface1.getSupplierRealizations()[0], cls2.getClientRealizations()[0]);
		
		IRealization gen = cls2.getClientRealizations()[0];
		assertEquals("realization1", gen.getName());
		//assertArrayEquals(new String[] {"realize", "my"}, gen.getStereotypes());
		assertArrayEquals(new String[] {"realize"}, gen.getStereotypes());
	}
	
	@Test
	public void checkUsage() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {

		IClass interface2 = findRootSubElement("Interface2", IClass.class);
		assertNotNull(interface2);
		assertArrayEquals(new String[] {"interface"}, interface2.getStereotypes());
		IClass cls2 = findRootSubElement("Class2", IClass.class);
		assertNotNull(cls2);
		
		assertEquals(1, cls2.getClientUsages().length);
		assertEquals(1, interface2.getSupplierUsages().length);
		
		IUsage usage = interface2.getSupplierUsages()[0];
		assertTrue(Arrays.asList(cls2.getClientUsages()).contains(usage));
		assertEquals("", usage.getName());
		assertArrayEquals(new String[] {"use"}, usage.getStereotypes());
	}
	
	@Test
	public void checkNoExistRel() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IPackage pkg = findRootSubElement("Package1", IPackage.class);
		assertNotNull(pkg);
		assertEquals(1, pkg.getSupplierDependencies().length);
		
		IClass cls2 = findRootSubElement("Class2", IClass.class);
		assertNotNull(cls2);
		assertEquals(1, cls2.getClientDependencies().length);
		
		IDependency dep = pkg.getSupplierDependencies()[0];
		assertEquals(dep, cls2.getClientDependencies()[0]);
		assertEquals(pkg, dep.getSupplier());
		assertEquals(cls2, dep.getClient());
		assertEquals("ngReal", dep.getName());
		
	}
	
	@Test
	@Ignore(value="現バージョンではプレゼンテーションはサポートしません。")
	public void checkNoteWithoutAnchor() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		
		INodePresentation ps1 = (INodePresentation) findPresentation("note22");
		assertNotNull(ps1);
		assertEquals(0, ps1.getLinks().length);

	}

	private IPresentation findPresentation(String name) throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IPackage root = getRoot();
		IClassDiagram dgm = findNamedElement(root.getDiagrams(), "Model", IClassDiagram.class);
		assertNotNull(dgm);
		return findPresentation(dgm, name);
	}
}
