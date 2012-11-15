package com.change_vision.astah.xmi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.AfterClass;

import com.change_vision.astah.xmi.convert.XmiToAstah;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IComment;
import com.change_vision.jude.api.inf.model.IDiagram;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.presentation.IPresentation;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;

public abstract class BasicXmiImporterTest {

	private static final String EA_FILE = "src/test/resources/ea/%s.xmi";
	private static final String ASTAH_FILE = "src/test/resources/astah/%s.asta";

	protected static void setup(String xmiFileName) throws IOException, LicenseNotFoundException,
			ProjectNotFoundException, ProjectLockedException, ClassNotFoundException,
			InvalidEditingException, InvalidUsingException {
		XmiToAstah converter = new XmiToAstah(String.format(EA_FILE, xmiFileName));
		converter.convert(String.format(ASTAH_FILE, xmiFileName));
		ProjectAccessor pa = ProjectAccessorFactory.getProjectAccessor();
		pa.validateProject();
	}
	
	@AfterClass
	public static void tearDown() throws ClassNotFoundException {
		ProjectAccessor pa = ProjectAccessorFactory.getProjectAccessor();
		pa.close();
	}

	protected IPackage getRoot() throws ClassNotFoundException, ProjectNotFoundException {
		ProjectAccessor pa = ProjectAccessorFactory.getProjectAccessor();
		IPackage model1 = findNamedElement(pa.getProject().getOwnedElements(), "Model", IPackage.class);
		assertNotNull(model1);
		return findNamedElement(model1.getOwnedElements(), "Model", IPackage.class);
	}
	
	protected <T extends INamedElement> T findNamedElement(INamedElement[] children, String name, Class<T> clazz) {
		for (INamedElement e : children) {
			if (clazz.isInstance(e) && e.getName().equals(name)) {
				return clazz.cast(e);
			}
		}
		return null;
	}
	
	protected <T extends INamedElement> T findRootSubElement(String name, Class<T> clazz)
			throws ClassNotFoundException, ProjectNotFoundException {
		IPackage root = getRoot();
		assertNotNull(root);
		return findNamedElement(root.getOwnedElements(), name, clazz);
	}
	
	
	protected IPresentation findPresentation(IDiagram dgm, String name) throws InvalidUsingException {
		for (IPresentation ps : dgm.getPresentations()) {
			if (ps.getModel() instanceof INamedElement) {
				String modelName = ((INamedElement) ps.getModel()).getName();
				if (ps.getModel() instanceof IComment) {
					modelName = ((IComment) ps.getModel()).getBody();
				}
				if (modelName.equals(name)) {
					return ps;
				}
			} else if (name.equals(ps.getLabel())) {
				return ps;
			}
		}
		return null;
	}
	
	protected IPresentation findPresentationByType(IDiagram dgm, String type) throws InvalidUsingException {
		for (IPresentation ps : dgm.getPresentations()) {
			if (ps.getType().equals(type)) {
				return ps;
			}
		}
		return null;
	}
	
	protected void assertCollectionEquals(Object[] objs1, Object... objs2) {
		if (objs1 == null || objs2 == null) {
			assertSame(objs1, objs2);
		}
		assertEquals(objs1.length, objs2.length);
		assertTrue(Arrays.asList(objs1).containsAll(Arrays.asList(objs2)));
	}
	
	protected void assertLinkPresentation(ILinkPresentation linkPs, INodePresentation ps1, INodePresentation ps2) {
		assertNotNull(linkPs);
		assertCollectionEquals(new IPresentation[] {ps1, ps2},
				linkPs.getSourceEnd(), linkPs.getTargetEnd());
		assertTrue(Arrays.asList(ps1.getLinks()).contains(linkPs));
		assertTrue(Arrays.asList(ps2.getLinks()).contains(linkPs));
	}
}
