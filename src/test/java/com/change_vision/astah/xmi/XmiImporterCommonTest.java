package com.change_vision.astah.xmi;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import com.change_vision.jude.api.inf.model.IArtifact;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IClassDiagram;
import com.change_vision.jude.api.inf.model.IComment;
import com.change_vision.jude.api.inf.model.IHyperlink;
import com.change_vision.jude.api.inf.model.IRequirement;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.presentation.IPresentation;

@Ignore(value="現バージョンではプレゼンテーションはサポートしません。")
public class XmiImporterCommonTest extends BasicXmiImporterTest {

	@BeforeClass
	public static void setup() throws IOException, LicenseNotFoundException,
			ProjectNotFoundException, ProjectLockedException, ClassNotFoundException,
			InvalidEditingException, InvalidUsingException {
		BasicXmiImporterTest.setup("common");
	}
	
	@Test
	public void checkDocumentNotOnClassDgm() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IArtifact art = findRootSubElement("Document1", IArtifact.class);
		assertNotNull(art);
		assertEquals(0, art.getPresentations().length);
	}
	
	@Test
	public void checkArtifactNotOnClassDgm() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IArtifact art = findRootSubElement("Artifact1", IArtifact.class);
		assertNotNull(art);
		assertEquals(0, art.getPresentations().length);
	}
	
	@Test
	public void checkRequirementNotOnClassDgm() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IRequirement req = findRootSubElement("Requirement1", IRequirement.class);
		assertNotNull(req);
		assertEquals(0, req.getPresentations().length);
	}
	
	@Test
	public void checkIssue() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls = findRootSubElement("Issue1", IClass.class);
		assertNotNull(cls);
		assertArrayEquals(new String[] {"issue"}, cls.getStereotypes());
		assertEquals(1, cls.getPresentations().length);
	}
	
	@Test
	public void checkChange() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls = findRootSubElement("Change1", IClass.class);
		assertNotNull(cls);
		assertArrayEquals(new String[] {"change"}, cls.getStereotypes());
		assertEquals(1, cls.getPresentations().length);
	}
	
	@Test
	public void checkConstraintAsNote() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls = findRootSubElement("Class1", IClass.class);
		assertNotNull(cls);
		assertEquals(1, cls.getPresentations().length);
		INodePresentation clsPs = (INodePresentation) cls.getPresentations()[0];
		
		IClassDiagram dgm = findNamedElement(getRoot().getDiagrams(), "Model", IClassDiagram.class);
		assertNotNull(dgm);
		INodePresentation notePs = (INodePresentation) findPresentationByType(dgm, "Note");
		assertNotNull(notePs);
		IComment comment = (IComment) notePs.getModel();
		assertArrayEquals(new String[] {"invariant"}, comment.getStereotypes());
		assertEquals("{cons1}", comment.getBody());
		
		assertEquals(1, clsPs.getLinks().length);
		assertArrayEquals(clsPs.getLinks(), notePs.getLinks());
		ILinkPresentation linkPs = notePs.getLinks()[0];
		assertArrayEquals(new IPresentation[] {clsPs, notePs},
				new IPresentation[]{linkPs.getTargetEnd(), linkPs.getSourceEnd()});
		
	}
	
	@Test
	public void checkText() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClassDiagram dgm = findNamedElement(getRoot().getDiagrams(), "Model", IClassDiagram.class);
		assertNotNull(dgm);
		INodePresentation textPs = (INodePresentation) findPresentation(dgm, "text1");
		assertNotNull(textPs);
		assertEquals("text1", textPs.getLabel());
	}
	
	@Test
	public void checkRect() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass kk = findRootSubElement("kk", IClass.class);
		assertNull(kk);
		
		IClassDiagram dgm = findNamedElement(getRoot().getDiagrams(), "Model", IClassDiagram.class);
		assertNotNull(dgm);
		INodePresentation rectPs = (INodePresentation) findPresentationByType(dgm, "Rectangle");
		assertNotNull(rectPs);
	}
	
	@Test
	public void checkFileLink() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClassDiagram dgm = findNamedElement(getRoot().getDiagrams(), "Model", IClassDiagram.class);
		assertNotNull(dgm);
		INodePresentation textPs = (INodePresentation) findPresentation(dgm, "1.txt");
		assertNotNull(textPs);
		IHyperlink[] hyperlinks = textPs.getHyperlinks();
		assertEquals(1, hyperlinks.length);
		IHyperlink link = hyperlinks[0];
		assertTrue(link.isFile());
		assertEquals("d:", link.getPath());
		assertEquals("1.txt", link.getName());
	}
	
	@Test
	public void checkWebLink() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClassDiagram dgm = findNamedElement(getRoot().getDiagrams(), "Model", IClassDiagram.class);
		assertNotNull(dgm);
		INodePresentation textPs = (INodePresentation) findPresentation(dgm, "www.change-vision.com");
		assertNotNull(textPs);
		IHyperlink[] hyperlinks = textPs.getHyperlinks();
		assertEquals(1, hyperlinks.length);
		IHyperlink link = hyperlinks[0];
		assertTrue(link.isURL());
		assertEquals("www.change-vision.com", link.getName());
	}


}
