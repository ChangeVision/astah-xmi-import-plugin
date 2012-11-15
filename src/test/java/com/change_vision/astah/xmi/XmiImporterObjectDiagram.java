package com.change_vision.astah.xmi;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
import com.change_vision.jude.api.inf.model.IInstanceSpecification;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.presentation.IPresentation;

@Ignore(value="現バージョンではプレゼンテーションはサポートしません。")
public class XmiImporterObjectDiagram extends BasicXmiImporterTest {

	@BeforeClass
	public static void setup() throws IOException, LicenseNotFoundException,
			ProjectNotFoundException, ProjectLockedException, ClassNotFoundException,
			InvalidEditingException, InvalidUsingException {
		BasicXmiImporterTest.setup("objDgm");
	}
	
	@Test
	public void checkDiagram() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClassDiagram dgm = findNamedElement(getRoot().getDiagrams(), "Model", IClassDiagram.class);
		assertNotNull(dgm);
		assertEquals(8, dgm.getPresentations().length);
	}
	
	@Test
	public void checkActor() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass actor = findRootSubElement("Actor1", IClass.class);
		assertNotNull(actor);
		assertArrayEquals(new String[] {"actor"}, actor.getStereotypes());
		assertEquals(1, actor.getPresentations().length);
	}
	
	@Test
	public void checkObject1() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		INodePresentation ps = (INodePresentation) findPresentation("Object1");
		assertNotNull(ps);
		assertEquals("InstanceSpecification", ps.getType());
		IInstanceSpecification instance = (IInstanceSpecification) ps.getModel();
		assertNull(instance.getClassifier());
	}
	
	@Test
	public void checkObjectWithBase() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		INodePresentation ps = (INodePresentation) findPresentation("c0");
		assertNotNull(ps);
		assertEquals("InstanceSpecification", ps.getType());
		IInstanceSpecification instance = (IInstanceSpecification) ps.getModel();
		IClass base = instance.getClassifier();
		assertNotNull(base);
		IClass cls0 = findRootSubElement("Class0", IClass.class);
		assertEquals(cls0, base);
	}
	
	@Test
	public void checkObject2() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		INodePresentation ps = (INodePresentation) findPresentation("Object2");
		assertNotNull(ps);
		assertEquals("InstanceSpecification", ps.getType());
		IInstanceSpecification instance = (IInstanceSpecification) ps.getModel();
		assertNull(instance.getClassifier());
		assertEquals(0, instance.getStereotypes().length);
	}
	
	@Test
	public void checkObject3() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		INodePresentation ps = (INodePresentation) findPresentation("Object3");
		assertNotNull(ps);
		assertEquals("InstanceSpecification", ps.getType());
		IInstanceSpecification instance = (IInstanceSpecification) ps.getModel();
		assertNull(instance.getClassifier());
		assertEquals(0, instance.getStereotypes().length);
	}
	
	@Test
	public void checkObject4() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		INodePresentation ps = (INodePresentation) findPresentation("Object4");
		assertNotNull(ps);
		assertEquals("InstanceSpecification", ps.getType());
		IInstanceSpecification instance = (IInstanceSpecification) ps.getModel();
		assertNull(instance.getClassifier());
		assertEquals(0, instance.getStereotypes().length);
	}

	@Test
	public void checkDependency() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		INodePresentation ps2 = (INodePresentation) findPresentation("Object4");
		assertNotNull(ps2);
		INodePresentation ps4 = (INodePresentation) findPresentation("Object4");
		assertNotNull(ps4);
		
		assertArrayEquals(ps2.getLinks(), ps4.getLinks());
		assertEquals(0, ps2.getLinks().length);
//		assertEquals(1, ps2.getLinks());
//		ILinkPresentation linkPs = ps2.getLinks()[0];
//		assertEquals("Dependency", linkPs);
//		assertEquals(ps2, linkPs.getSource());
//		assertEquals(ps4, linkPs.getTarget());
	}
	
	@Test
	public void checkText() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		INodePresentation ps = (INodePresentation) findPresentation("note1");
		assertNotNull(ps);
		assertEquals("Text", ps.getType());
	}
	
	private IPresentation findPresentation(String name) throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IPackage root = getRoot();
		IClassDiagram dgm = findNamedElement(root.getDiagrams(), "Model", IClassDiagram.class);
		assertNotNull(dgm);
		return findPresentation(dgm, name);
	}
}
