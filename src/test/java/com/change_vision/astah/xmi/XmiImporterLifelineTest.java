package com.change_vision.astah.xmi;

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
import com.change_vision.jude.api.inf.model.ILifeline;
import com.change_vision.jude.api.inf.model.ISequenceDiagram;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.presentation.IPresentation;

@Ignore(value="現バージョンではプレゼンテーションはサポートしません。")
public class XmiImporterLifelineTest extends BasicXmiImporterTest {

	@BeforeClass
	public static void setup() throws IOException, LicenseNotFoundException,
			ProjectNotFoundException, ProjectLockedException, ClassNotFoundException,
			InvalidEditingException, InvalidUsingException {
		BasicXmiImporterTest.setup("lifeline");
	}
	
	@Test
	public void checkDiagram() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls0 = findRootSubElement("Class0", IClass.class);
		assertNotNull(cls0);
		ISequenceDiagram dgm = findNamedElement(cls0.getDiagrams(), "seqDgm0", ISequenceDiagram.class);
		assertNotNull(dgm);
		assertEquals(5, dgm.getPresentations().length);
	}
	
	@Test
	public void checkObject1() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		INodePresentation ps = (INodePresentation) findPresentation("Object1");
		assertNotNull(ps);
		assertEquals("Lifeline", ps.getType());
		ILifeline lifeline = (ILifeline) ps.getModel();
		assertNull(lifeline.getBase());
		assertEquals("normal", ps.getProperty("notation_type"));
	}
	
	@Test
	public void checkActor1() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass actor0 = findRootSubElement("Actor1", IClass.class);
		assertNotNull(actor0);
		
		INodePresentation ps = (INodePresentation) findPresentation("");
		assertNotNull(ps);
		assertEquals("Lifeline", ps.getType());
		ILifeline lifeline = (ILifeline) ps.getModel();
		assertEquals(actor0, lifeline.getBase());
		assertEquals("icon", ps.getProperty("notation_type"));
	}
	
	@Test
	public void checkObject2() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		INodePresentation ps = (INodePresentation) findPresentation("Object2");
		assertNotNull(ps);
		assertEquals("Lifeline", ps.getType());
		ILifeline lifeline = (ILifeline) ps.getModel();
		assertNull(lifeline.getBase());
//		assertEquals(0, lifeline.getStereotypes().length);
		assertEquals("normal", ps.getProperty("notation_type"));
	}
	
	@Test
	public void checkClass0() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls0 = findRootSubElement("Class0", IClass.class);
		assertNotNull(cls0);
		INodePresentation ps = (INodePresentation) findPresentation("c0");
		assertNotNull(ps);
		assertEquals("Lifeline", ps.getType());
		ILifeline lifeline = (ILifeline) ps.getModel();
		assertEquals(cls0, lifeline.getBase());
		assertEquals(0, lifeline.getStereotypes().length);
		assertEquals("normal", ps.getProperty("notation_type"));
	}
	
	@Test
	public void checkNestClass() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls1 = findRootSubElement("c1", IClass.class);
		assertNotNull(cls1);
		IClass cls2 = findNamedElement(cls1.getNestedClasses(), "c2", IClass.class);
		assertNotNull(cls2);
	}
	
	private IPresentation findPresentation(String name) throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls0 = findRootSubElement("Class0", IClass.class);
		assertNotNull(cls0);
		ISequenceDiagram dgm = findNamedElement(cls0.getDiagrams(), "seqDgm0", ISequenceDiagram.class);
		assertNotNull(dgm);
		return findPresentation(dgm, name);
	}
}
