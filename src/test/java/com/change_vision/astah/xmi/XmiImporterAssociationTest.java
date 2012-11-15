package com.change_vision.astah.xmi;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IAssociation;
import com.change_vision.jude.api.inf.model.IAssociationClass;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IConstraint;
import com.change_vision.jude.api.inf.model.IMultiplicityRange;
import com.change_vision.jude.api.inf.model.IRequirement;

public class XmiImporterAssociationTest extends BasicXmiImporterTest {

	@BeforeClass
	public static void setup() throws IOException, LicenseNotFoundException,
			ProjectNotFoundException, ProjectLockedException, ClassNotFoundException,
			InvalidEditingException, InvalidUsingException {
		BasicXmiImporterTest.setup("association");
	}
	
	@Test
	public void checkClass1() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls1 = findRootSubElement("Class1", IClass.class);
		assertNotNull(cls1);
		IAssociation ass = findAssociation(cls1, "selfAss");
		assertNotNull(ass);
		IAttribute[] memberEnds = ass.getMemberEnds();
		assertEquals(2, memberEnds.length);
		assertEquals(cls1, memberEnds[0].getType());
		assertEquals(cls1, memberEnds[1].getType());
		assertTrue(Arrays.asList(cls1.getAttributes()).containsAll(Arrays.asList(memberEnds)));
	}

	@Test
	public void checkNonNavigableAssociation() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls1 = findRootSubElement("Class1", IClass.class);
		assertNotNull(cls1);
		IClass cls2 = findRootSubElement("Class2", IClass.class);
		assertNotNull(cls2);
		IAssociation ass = findAssociation(cls1, "nonNavigableAss");
		assertNotNull(ass);
		assertEquals("nonNavigableAss", ass.getName());
		IAttribute[] memberEnds = ass.getMemberEnds();
		assertEquals(2, memberEnds.length);
		assertEquals(cls1, memberEnds[0].getType());
		assertEquals(cls2, memberEnds[1].getType());
		assertTrue(Arrays.asList(cls1.getAttributes()).contains(memberEnds[1]));
		assertTrue(Arrays.asList(cls2.getAttributes()).contains(memberEnds[0]));
		for (IAttribute e : memberEnds) {
			assertEquals("", e.getName());
			//assertEquals("Non_Navigable", e.getNavigability());
			assertEquals("Unspecified", e.getNavigability());
		}
	}
	
	@Test
	public void checkNoShowAssociation() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IRequirement req = findRootSubElement("Requirement1", IRequirement.class);
		assertNull(req);
		IAssociation ass = findRootSubElement("noShowAss", IAssociation.class);
		assertNull(ass);
		
	}
	
	@Test
	public void checkCompositeAssociation() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls1 = findRootSubElement("Class2", IClass.class);
		assertNotNull(cls1);
		IClass cls2 = findRootSubElement("Class3", IClass.class);
		assertNotNull(cls2);
		IAttribute end2 = findNamedElement(cls1.getAttributes(), "compositeRole", IAttribute.class);
		assertNotNull(end2);
		IAssociation ass = end2.getAssociation();
		assertNotNull(ass);
		assertEquals("compAss", ass.getName());
		IAttribute[] memberEnds = ass.getMemberEnds();
		assertEquals(2, memberEnds.length);
		assertEquals(cls1, memberEnds[0].getType());
		assertEquals("", memberEnds[0].getName());
		assertFalse(memberEnds[0].isComposite() || memberEnds[0].isAggregate());
		assertEquals(cls2, memberEnds[1].getType());
//		assertEquals("compositeRole", memberEnds[1].getName());
		assertTrue(memberEnds[1].isComposite());
		assertTrue(Arrays.asList(cls1.getAttributes()).contains(memberEnds[1]));
		assertTrue(Arrays.asList(cls2.getAttributes()).contains(memberEnds[0]));
	}
	
	@Test
	public void checkAggregationssociation() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls1 = findRootSubElement("Class3", IClass.class);
		assertNotNull(cls1);
		IClass cls2 = findRootSubElement("Class4", IClass.class);
		assertNotNull(cls2);
		IAttribute end2 = findNamedElement(cls1.getAttributes(), "aggregationRole", IAttribute.class);
		assertNotNull(end2);
		IAssociation ass = end2.getAssociation();
		assertNotNull(ass);
		assertEquals("aggAss", ass.getName());
		IAttribute[] memberEnds = ass.getMemberEnds();
		assertEquals(2, memberEnds.length);
		assertEquals(cls1, memberEnds[0].getType());
		assertEquals("staticRole", memberEnds[0].getName());
		//assertTrue(memberEnds[0].isStatic());
		assertFalse(memberEnds[0].isComposite() || memberEnds[0].isAggregate());
		assertEquals(cls2, memberEnds[1].getType());
		assertFalse(memberEnds[1].isStatic());
		assertEquals("aggregationRole", memberEnds[1].getName());
		assertTrue(memberEnds[1].isAggregate());
		assertTrue(Arrays.asList(cls1.getAttributes()).contains(memberEnds[1]));
		assertTrue(Arrays.asList(cls2.getAttributes()).contains(memberEnds[0]));
		//assertEquals("assAlias", ass.getAlias1());
		//assertArrayEquals(new String[]{"import", "merge"}, ass.getStereotypes());
		assertArrayEquals(new String[]{}, ass.getStereotypes());
	}
	
	@Test
	public void checkTagAssociation() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls1 = findRootSubElement("Class2", IClass.class);
		assertNotNull(cls1);
		IClass cls2 = findRootSubElement("AssClass1", IClass.class);
		assertNotNull(cls2);
		IAssociation ass = findAssociation(cls1, "tagAss");
		assertNotNull(ass);
		IAttribute[] memberEnds = ass.getMemberEnds();
		assertEquals(2, memberEnds.length);
		assertEquals(cls1, memberEnds[0].getType());
		assertEquals("", memberEnds[0].getName());
		assertEquals(cls2, memberEnds[1].getType());
		assertEquals("", memberEnds[1].getName());
		assertTrue(Arrays.asList(cls1.getAttributes()).contains(memberEnds[1]));
		assertTrue(Arrays.asList(cls2.getAttributes()).contains(memberEnds[0]));
//		assertEquals(2, ass.getTaggedValues().length);
//		assertEquals("val1", ass.getTaggedValue("tag1"));
//		assertEquals("val2", ass.getTaggedValue("tag2"));
	}
	
	private IAssociation findAssociation(IClass cls, String name) {
		for (IAttribute end : cls.getAttributes()) {
			IAssociation ass = end.getAssociation();
			if (ass != null && ass.getName().equals(name)) {
				return ass;
			}
		}
		return null;
	}
	
	@Test
	public void checkConsAssociation() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls1 = findRootSubElement("Class3", IClass.class);
		assertNotNull(cls1);
		IClass cls2 = findRootSubElement("AssClass1", IClass.class);
		assertNotNull(cls2);
		IAssociation ass = findAssociation(cls1, "consAss");
		assertNotNull(ass);
		IAttribute[] memberEnds = ass.getMemberEnds();
		assertEquals(2, memberEnds.length);
		assertEquals(cls1, memberEnds[0].getType());
		assertEquals("", memberEnds[0].getName());
		assertEquals(cls2, memberEnds[1].getType());
		assertEquals("", memberEnds[1].getName());
		assertTrue(Arrays.asList(cls1.getAttributes()).contains(memberEnds[1]));
		assertTrue(Arrays.asList(cls2.getAttributes()).contains(memberEnds[0]));
//		IConstraint[] constraints = ass.getConstraints();
//		assertEquals(2, constraints.length);
//		assertEquals("con1", constraints[0].getName());
//		assertEquals("con2", constraints[1].getName());
	}
	
	@Test
	public void checkNavigableAssociation() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls1 = findRootSubElement("AssClass1", IClass.class);
		assertNotNull(cls1);
		IClass cls2 = findRootSubElement("Class5", IClass.class);
		assertNotNull(cls2);
		IAttribute end2 = findNamedElement(cls1.getAttributes(), "publicRole", IAttribute.class);
		assertNotNull(end2);
		IAssociation ass = end2.getAssociation();
		assertNotNull(ass);
		assertEquals("navAss", ass.getName());
		IAttribute[] memberEnds = ass.getMemberEnds();
		assertEquals(2, memberEnds.length);
		assertEquals(cls1, memberEnds[0].getType());
		assertEquals(cls2, memberEnds[1].getType());
		assertTrue(Arrays.asList(cls1.getAttributes()).contains(memberEnds[1]));
		assertTrue(Arrays.asList(cls2.getAttributes()).contains(memberEnds[0]));
//		assertEquals(2, ass.getTaggedValues().length);
		for (IAttribute e : memberEnds) {
			if (e.getName().equals("privateRole")) {
				assertTrue(e.isPrivateVisibility());
				assertFalse(e.isDerived());
				assertEquals("Unspecified", e.getNavigability());
				IMultiplicityRange[] multiplicity = e.getMultiplicity();
				assertEquals(1, e.getMultiplicity().length);
				IMultiplicityRange range = multiplicity[0];
				assertEquals(-100, range.getLower());
				assertEquals(-100, range.getUpper());
//				assertEquals(0, e.getConstraints().length);
			} else if (e.getName().equals("publicRole")) {
				assertTrue(e.isPublicVisibility());
				assertTrue(e.isDerived());
				assertEquals("Navigable", e.getNavigability());
				IMultiplicityRange[] multiplicity = e.getMultiplicity();
				assertEquals(1, multiplicity.length);
				IMultiplicityRange range = multiplicity[0];
				assertEquals(0, range.getLower());
				assertEquals(-1, range.getUpper());
				
//				IConstraint[] constraints = e.getConstraints();
//				assertEquals(2, constraints.length);
//				assertEquals("ordered", constraints[0].getName());
//				assertEquals("role5 constraint", constraints[1].getName());
			} else {
				fail("wrong name");
			}
		}
	}
	
	@Test
	public void checkAssociationClass() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IAssociationClass assCls = findRootSubElement("AssClass1", IAssociationClass.class);
		assertNotNull(assCls);
//		assertEquals("acNote", assCls.getDefinition());
//		assertEquals(1, assCls.getTaggedValues().length);
//		assertEquals("acVal1", assCls.getTaggedValue("acTag1"));
//		assertEquals(1, assCls.getConstraints().length);
//		assertEquals("con1", assCls.getConstraints()[0].getName());

		IAttribute[] memberEnds = assCls.getMemberEnds();
		assertEquals(2, memberEnds.length);
		for (IAttribute e : memberEnds) {
			if (e.getName().equals("packageRole")) {
				assertTrue(e.isPackageVisibility());
				assertTrue(e.isDerived());
				//assertEquals("Non_Navigable", e.getNavigability());
				assertEquals("Unspecified", e.getNavigability());
				IMultiplicityRange[] multiplicity = e.getMultiplicity();
				assertEquals(1, multiplicity.length);
				IMultiplicityRange range = multiplicity[0];
				assertEquals(1, range.getLower());
				assertEquals(-1, range.getUpper());
				
//				IConstraint[] constraints = e.getConstraints();
//				assertEquals(2, constraints.length);
//				assertEquals("ordered", constraints[0].getName());
//				assertEquals("con3", constraints[1].getName());
			} else if (e.getName().equals("protectedRole")){
				assertTrue(e.isProtectedVisibility());
				assertFalse(e.isDerived());
				assertEquals("Navigable", e.getNavigability());
				IMultiplicityRange[] multiplicity = e.getMultiplicity();
				assertEquals(1, multiplicity.length);
				IMultiplicityRange range = multiplicity[0];
				assertEquals(-100, range.getLower());
				assertEquals(-100, range.getUpper());
				
				IConstraint[] constraints = e.getConstraints();
				assertEquals(0, constraints.length);
			} else {
				fail("wrong name");
			}
		}
	}
}
