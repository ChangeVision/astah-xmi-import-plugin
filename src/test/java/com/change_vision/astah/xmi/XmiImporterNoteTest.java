package com.change_vision.astah.xmi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IClassDiagram;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.presentation.IPresentation;

@Ignore(value="現バージョンではプレゼンテーションはサポートしません。")
public class XmiImporterNoteTest extends BasicXmiImporterTest {

	@BeforeClass
	public static void setup() throws IOException, LicenseNotFoundException,
			ProjectNotFoundException, ProjectLockedException, ClassNotFoundException,
			InvalidEditingException, InvalidUsingException {
		BasicXmiImporterTest.setup("note");
	}
	
	@Test
	public void checkNoteForPackage() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IPresentation pkgPs = findPresentation("Package1");
		assertNotNull(pkgPs);
		INodePresentation notePs = (INodePresentation) findPresentation("note for package");
		assertNotNull(notePs);
		assertEquals("Note", notePs.getType());
		assertEquals(1, notePs.getLinks().length);
		assertNoteLinkEnd(notePs.getLinks()[0], pkgPs);
	}
	
	@Test
	public void checkNoteForClassAndRelationship() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IPresentation clsPs = findPresentation("Class1");
		assertNotNull(clsPs);
		IPresentation depPs = findPresentationByType("Dependency");
		assertNotNull(depPs);
		IPresentation assPs = findPresentationByType("Association");
		assertNotNull(assPs);
		INodePresentation notePs = (INodePresentation) findPresentation("note for class and relationship");
		assertNotNull(notePs);
		assertEquals("Note", notePs.getType());
		assertEquals(3, notePs.getLinks().length);
		assertNoteLinkEnds(notePs.getLinks(), clsPs, depPs, assPs);
	}

	@Test
	public void checkNoteForNote() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IPresentation note1Ps = findPresentation("note for multi");
		assertNotNull(note1Ps);
		INodePresentation notePs = (INodePresentation) findPresentation("note for note");
		assertNotNull(notePs);
		assertEquals("Note", notePs.getType());
		assertEquals(1, notePs.getLinks().length);
		assertNoteLinkEnd(notePs.getLinks()[0], note1Ps);
	}

	@Test
	public void checkNoteForMulti() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IPresentation pkgPs = findPresentation("Package1");
		assertNotNull(pkgPs);
		IPresentation clsPs = findPresentation("Class1");
		assertNotNull(clsPs);
		IPresentation noteNotePs = findPresentation("note for note");
		assertNotNull(noteNotePs);
		
		INodePresentation notePs = (INodePresentation) findPresentation("note for multi");
		assertNotNull(notePs);
		assertEquals("Note", notePs.getType());
		ILinkPresentation[] links = notePs.getLinks();
		assertEquals(3, links.length);
		assertNoteLinkEnds(links, pkgPs, clsPs, noteNotePs);
	}
	
	private IPresentation findPresentation(String name) throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IPackage root = getRoot();
		IClassDiagram dgm = findNamedElement(root.getDiagrams(), "Model", IClassDiagram.class);
		assertNotNull(dgm);
		return findPresentation(dgm, name);
	}
	
	private IPresentation findPresentationByType(String type) throws InvalidUsingException, ClassNotFoundException, ProjectNotFoundException {
		IPackage root = getRoot();
		IClassDiagram dgm = findNamedElement(root.getDiagrams(), "Model", IClassDiagram.class);
		assertNotNull(dgm);
		return findPresentationByType(dgm, type);
	}
	
	private void assertNoteLinkEnd(ILinkPresentation linkPs, IPresentation end) {
		assertTrue(linkPs.getSource() == end || linkPs.getTarget() == end);
	}
	
	private void assertNoteLinkEnds(ILinkPresentation[] links, IPresentation... end) {
		assertEquals(links.length, end.length);
		List<IPresentation> ends = new ArrayList<IPresentation>();
		for (ILinkPresentation l : links) {
			ends.add(l.getSourceEnd());
			ends.add(l.getTargetEnd());
		}
		assertTrue(ends.containsAll(Arrays.asList(end)));
	}

}
