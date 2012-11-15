package com.change_vision.astah.xmi;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IOperation;
import com.change_vision.jude.api.inf.model.IParameter;

public class XmiImporterOperationTest extends BasicXmiImporterTest {

	@BeforeClass
	public static void setup() throws IOException, LicenseNotFoundException,
			ProjectNotFoundException, ProjectLockedException, ClassNotFoundException,
			InvalidEditingException, InvalidUsingException {
		BasicXmiImporterTest.setup("operation");
	}
	
	@Test
	public void checkClass1() throws ClassNotFoundException, ProjectNotFoundException {
		IClass cls = findRootSubElement("Class1", IClass.class);
		assertNotNull(cls);
		IOperation[] operations = cls.getOperations();
		assertEquals(4, operations.length);
		//op1():void
		assertOperation(operations[0], "op1", "void", 0);
		//op1(p1: char, [inout] p2: Class2, [out] p3: double, [return] p4: any): boolean
		assertOperation(operations[1], "op1", "boolean", 4);
		IParameter[] params = operations[1].getParameters();
		assertParameter(params[0], "p1", "char", "in");
		assertParameter(params[1], "p2", "Class2", "inout");
		assertParameter(params[2], "p3", "double", "out");
		assertParameter(params[3], "p4", "any", "in");
		//op3():Class2
		assertOperation(operations[2], "op3", "Class2", 0);
		//op4():any
		assertOperation(operations[3], "op4", "any", 0);
	}
	
	@Test
	public void checkClass2() throws ClassNotFoundException, ProjectNotFoundException {
		IClass cls = findRootSubElement("Class2", IClass.class);
		assertNotNull(cls);
		IOperation[] operations = cls.getOperations();
		assertEquals(4, operations.length);
		//op1():void
		assertOperation(operations[0], "packageOp", "void", 0);
		assertTrue(operations[0].isPackageVisibility());
		assertOperation(operations[1], "privateOp", "void", 0);
		assertTrue(operations[1].isPrivateVisibility());
		assertOperation(operations[2], "protectedOp", "void", 0);
		assertTrue(operations[2].isProtectedVisibility());
		assertOperation(operations[3], "publicOp", "void", 0);
		assertTrue(operations[3].isPublicVisibility());
	}
	
	@Test
	public void checkClass3() throws ClassNotFoundException, ProjectNotFoundException {
		IClass cls = findRootSubElement("Class3", IClass.class);
		assertNotNull(cls);
		IOperation[] operations = cls.getOperations();
		assertEquals(5, operations.length);

		assertOperation(operations[0], "abstractOp", "void", 0);
		assertTrue(operations[0].isAbstract());
		
		assertOperation(operations[1], "operationWithAlias", "void", 0);
//		assertEquals("opAlias", operations[1].getAlias1());
		
		assertOperation(operations[2], "operationWithNote", "void", 0);
//		assertEquals("note for op", operations[2].getDefinition());
		
		assertOperation(operations[3], "paramWithAlias", "void", 1);
		IParameter param = operations[3].getParameters()[0];
		assertParameter(param, "p1", "int", "in");
//		assertEquals("paramAlias", param.getAlias1());
		
		assertOperation(operations[4], "staticOp", "void", 0);
		assertTrue(operations[4].isStatic());
	}
	
	@Test
	public void checkClass4() throws ClassNotFoundException, ProjectNotFoundException {
		
		IClass cls = findRootSubElement("Class4", IClass.class);
		assertNotNull(cls);
		IOperation[] operations = cls.getOperations();
		assertEquals(4, operations.length);

		assertOperation(operations[0], "operationWithPostCondition", "void", 0);
		assertArrayEquals(new String[]{"post1"}, operations[0].getPostConditions());
		
		assertOperation(operations[1], "operationWithPreCondition", "void", 0);
		assertArrayEquals(new String[]{"pre1", "pre2", "pre3"}, operations[1].getPreConditions());
		
		assertOperation(operations[2], "operationWithStereotype", "void", 0);
		//assertArrayEquals(new String[]{"event", "operator"}, operations[2].getStereotypes());
		assertArrayEquals(new String[]{}, operations[2].getStereotypes());
		
		assertOperation(operations[3], "operationWithTag", "void", 0);
//		ITaggedValue[] tvs = operations[3].getTaggedValues();
//		assertEquals(2, tvs.length);
//		assertEquals("val1", operations[3].getTaggedValue("tag1"));
//		assertEquals("val2", operations[3].getTaggedValue("tag2"));
		//order is strange, in xmi, below order is NG,
		//but it's OK in XmiImporterClassTest.checkClassWithTaggedValues()
//		assertEquals("tag1", tvs[0].getKey());
//		assertEquals("val1", tvs[0].getValue());
//		assertEquals("tag2", tvs[1].getKey());
//		assertEquals("val2", tvs[1].getValue());
	}

	private void assertOperation(IOperation op, String name, String type, int paramSize) {
		assertNotNull(op);
		assertEquals(name, op.getName());
		assertEquals(type, op.getReturnType().getName());
		assertEquals(paramSize, op.getParameters().length);
	}

	private void assertParameter(IParameter param, String name, String type, String direction) {
		assertNotNull(param);
		assertEquals(name, param.getName());
		assertEquals(type, param.getType().getName());
		assertEquals(direction, param.getDirection());
	}

}
