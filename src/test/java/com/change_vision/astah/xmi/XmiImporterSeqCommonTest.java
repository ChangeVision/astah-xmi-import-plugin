package com.change_vision.astah.xmi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.ILifeline;
import com.change_vision.jude.api.inf.model.ISequenceDiagram;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.presentation.IPresentation;

@Ignore(value="現バージョンではプレゼンテーションはサポートしません。")
public class XmiImporterSeqCommonTest extends BasicXmiImporterTest {


	@BeforeClass
	public static void setup() throws IOException, LicenseNotFoundException,
			ProjectNotFoundException, ProjectLockedException, ClassNotFoundException,
			InvalidEditingException, InvalidUsingException {
		BasicXmiImporterTest.setup("seqCommon");
	}
	
	@Test
	public void checkLifelineArtifact() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		assertInvalidBaseLifeline("Artifact1");
	}
	
	@Test
	public void checkLifelineRequirement() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		assertInvalidBaseLifeline("Requirement1");
	}
	
	private void assertInvalidBaseLifeline(String name) throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		INodePresentation lifelinePs = (INodePresentation) findPresentation(name);
		assertNotNull(lifelinePs);
		assertEquals("Lifeline", lifelinePs.getType());
		
		ILifeline lifeline = (ILifeline) lifelinePs.getModel();
		assertNotNull(lifeline);
		assertEquals(null, lifeline.getBase());
	}

	@Test
	public void checkNote() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		INodePresentation notePs = (INodePresentation) findPresentation("note1");
		assertNotNull(notePs);
		assertEquals("Note", notePs.getType());
		
		INodePresentation objPs = (INodePresentation) findPresentation("Object1");
		assertNotNull(objPs);
		assertEquals(1, notePs.getLinks().length);
		ILinkPresentation linkPs = notePs.getLinks()[0];
		assertEquals(objPs, linkPs.getTarget());
	}
	
	@Test
	public void checkConstraint() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		INodePresentation notePs = (INodePresentation) findPresentation("{cons1}");
		assertNotNull(notePs);
		assertEquals("Note", notePs.getType());
		
		ILinkPresentation msgPs = (ILinkPresentation) findPresentation("msg1");
		assertNotNull(msgPs);
		
		assertEquals(1, notePs.getLinks().length);
		ILinkPresentation linkPs = notePs.getLinks()[0];
		assertEquals(msgPs, linkPs.getTargetEnd());
	}
	
	@Test
	public void checkText() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		INodePresentation textPs = (INodePresentation) findPresentation("text1");
		assertNotNull(textPs);
		assertEquals("Text", textPs.getType());
	}
	
	@Test
	public void checkLegend() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		INodePresentation textPs = (INodePresentation) findPresentation("Legend");
		assertNotNull(textPs);
		assertEquals("Text", textPs.getType());
	}
	
	@Test
	public void checkRect() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		ISequenceDiagram dgm = findNamedElement(getRoot().getDiagrams(), "seqDgm0", ISequenceDiagram.class);
		assertNotNull(dgm);
		INodePresentation ps = (INodePresentation) findPresentationByType(dgm, "Rectangle");
		assertNotNull(ps);
	}
	
	private IPresentation findPresentation(String name) throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		ISequenceDiagram dgm = findNamedElement(getRoot().getDiagrams(), "seqDgm0", ISequenceDiagram.class);
		assertNotNull(dgm);
		return findPresentation(dgm, name);
	}
	
}
