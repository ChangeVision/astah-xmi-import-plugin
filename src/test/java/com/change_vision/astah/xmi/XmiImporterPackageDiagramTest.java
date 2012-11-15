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
import com.change_vision.jude.api.inf.model.IClassDiagram;
import com.change_vision.jude.api.inf.model.IPackage;

public class XmiImporterPackageDiagramTest extends BasicXmiImporterTest {

	@BeforeClass
	public static void setup() throws IOException, LicenseNotFoundException,
			ProjectNotFoundException, ProjectLockedException, ClassNotFoundException,
			InvalidEditingException, InvalidUsingException {
		BasicXmiImporterTest.setup("packageDiagram");
	}
	
	@Test
	@Ignore(value="現バージョンではプレゼンテーションはサポートしません。")
	public void checkDiagram() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClassDiagram dgm = findNamedElement(getRoot().getDiagrams(), "Model", IClassDiagram.class);
		assertNotNull(dgm);
		assertEquals(4, dgm.getPresentations().length);
	}
	

	@Test
	public void checkPackage() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IPackage pkg1 = findRootSubElement("Package1", IPackage.class);
		assertNotNull(pkg1);
		
		IPackage pkg2 = findRootSubElement("Package2", IPackage.class);
		assertNotNull(pkg2);

		assertEquals(1, pkg1.getClientDependencies().length);
		assertEquals(1, pkg2.getSupplierDependencies().length);
		assertEquals(pkg1.getClientDependencies()[0], pkg2.getSupplierDependencies()[0]);
	}
}
