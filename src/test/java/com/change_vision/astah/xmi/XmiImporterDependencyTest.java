package com.change_vision.astah.xmi;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IDependency;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IPackage;

public class XmiImporterDependencyTest extends BasicXmiImporterTest {

	@BeforeClass
	public static void setup() throws IOException, LicenseNotFoundException,
			ProjectNotFoundException, ProjectLockedException, ClassNotFoundException,
			InvalidEditingException, InvalidUsingException {
		BasicXmiImporterTest.setup("dependency");
	}
	
	@Test
	public void checkPackageImport() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IPackage pkg1 = findRootSubElement("Package1", IPackage.class);
		assertNotNull(pkg1);
		IPackage pkg2 = findRootSubElement("Package3", IPackage.class);
		assertNotNull(pkg2);

		//IDependency dep = findNamedElement(pkg1.getClientDependencies(), "pi", IDependency.class);
		IDependency dep = findDependency(pkg1, pkg2);
		assertNotNull(dep);
		assertArrayEquals(new String[] {"import"}, dep.getStereotypes());
		assertEquals(pkg1, dep.getClient());
		assertEquals(pkg2, dep.getSupplier());
	}
	
	private IDependency findDependency(INamedElement client, INamedElement supplier) {
		for (IDependency dep : client.getClientDependencies()) {
			if (dep.getSupplier() == supplier) {
				return dep;
			}
		}
		return null;
	}

	@Test
	public void checkPackageMerge() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IPackage pkg1 = findRootSubElement("Package1", IPackage.class);
		assertNotNull(pkg1);
		IPackage pkg2 = findRootSubElement("Package2", IPackage.class);
		assertNotNull(pkg2);

		//IDependency dep = findNamedElement(pkg1.getClientDependencies(), "pm", IDependency.class);
		IDependency dep = findDependency(pkg1, pkg2);
		assertNotNull(dep);
		assertArrayEquals(new String[] {"merge"}, dep.getStereotypes());
		assertEquals(pkg1, dep.getClient());
		assertEquals(pkg2, dep.getSupplier());
	}
	
	@Test
	public void checkTrace() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IPackage pkg1 = findRootSubElement("Package3", IPackage.class);
		assertNotNull(pkg1);
		IPackage pkg2 = findRootSubElement("Package2", IPackage.class);
		assertNotNull(pkg2);

		IDependency dep = findNamedElement(pkg1.getClientDependencies(), "trace", IDependency.class);
		assertNotNull(dep);
		//assertArrayEquals(new String[] {"trace"}, dep.getStereotypes());
		assertArrayEquals(new String[] {}, dep.getStereotypes());
		assertEquals(pkg1, dep.getClient());
		assertEquals(pkg2, dep.getSupplier());
	}
	
	@Test
	public void checkAbstraction() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IPackage pkg1 = findRootSubElement("Package3", IPackage.class);
		assertNotNull(pkg1);
		IPackage pkg2 = findRootSubElement("Package5", IPackage.class);
		assertNotNull(pkg2);

		//IDependency dep = findNamedElement(pkg1.getClientDependencies(), "abs", IDependency.class);
		IDependency dep = findDependency(pkg1, pkg2);
		assertNotNull(dep);
		//assertArrayEquals(new String[] {"abstraction"}, dep.getStereotypes());
		assertArrayEquals(new String[] {}, dep.getStereotypes());
		assertEquals(pkg1, dep.getClient());
		assertEquals(pkg2, dep.getSupplier());
	}
	
	@Test
	public void checkFlow() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IPackage pkg1 = findRootSubElement("Package4", IPackage.class);
		assertNotNull(pkg1);
		IPackage pkg2 = findRootSubElement("Package3", IPackage.class);
		assertNotNull(pkg2);

		IDependency dep = findNamedElement(pkg1.getClientDependencies(), "flow", IDependency.class);
		assertNull(dep);	//can NOT get InformationFlow's source and target by uml2
		//assertNotNull(dep);
//		assertArrayEquals(new String[] {"flow"}, dep.getStereotypes());
//		assertEquals(pkg1, dep.getClient());
//		assertEquals(pkg2, dep.getSupplier());
	}
	
	@Test
	public void checkUse() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IPackage pkg1 = findRootSubElement("Package4", IPackage.class);
		assertNotNull(pkg1);
		IPackage pkg2 = findRootSubElement("Package5", IPackage.class);
		assertNotNull(pkg2);

		IDependency dep = findNamedElement(pkg1.getClientDependencies(), "use", IDependency.class);
		assertNotNull(dep);
		//assertArrayEquals(new String[] {"use"}, dep.getStereotypes());
		assertArrayEquals(new String[] {}, dep.getStereotypes());
		assertEquals(pkg1, dep.getClient());
		assertEquals(pkg2, dep.getSupplier());
	}
	
	@Test
	public void checkDependency() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IPackage pkg1 = findRootSubElement("Package2", IPackage.class);
		assertNotNull(pkg1);
		IPackage pkg2 = findRootSubElement("Package4", IPackage.class);
		assertNotNull(pkg2);

		IDependency dep = findNamedElement(pkg1.getClientDependencies(), "dep", IDependency.class);
		assertNotNull(dep);
		//assertArrayEquals(new String[] {"call", "bind"}, dep.getStereotypes());
		assertArrayEquals(new String[] {}, dep.getStereotypes());
		assertEquals(pkg1, dep.getClient());
		assertEquals(pkg2, dep.getSupplier());
//		assertEquals("depAlias", dep.getAlias1());
//		assertEquals("dep note", dep.getDefinition());
		
//		IConstraint[] constraints = dep.getConstraints();
//		assertEquals(2, constraints.length);
//		assertEquals("con1", constraints[0].getName());
//		assertEquals("con2", constraints[1].getName());
//		
//		assertEquals(2, dep.getTaggedValues().length);
//		assertEquals("depAlias", dep.getTaggedValue("jude.multi_language.alias1"));
//		assertEquals("depVal", dep.getTaggedValue("depTag1"));
	}
}
