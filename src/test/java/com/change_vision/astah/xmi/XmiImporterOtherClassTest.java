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
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;

public class XmiImporterOtherClassTest extends BasicXmiImporterTest {

	@BeforeClass
	public static void setup() throws IOException, LicenseNotFoundException,
			ProjectNotFoundException, ProjectLockedException, ClassNotFoundException,
			InvalidEditingException, InvalidUsingException {
		BasicXmiImporterTest.setup("otherClass");
	}
	
	@Test
	public void checkDataType() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls = findRootSubElement("DataType1", IClass.class);
		assertNotNull(cls);
		assertArrayEquals(new String[] {"dataType"}, cls.getStereotypes());
//		assertEquals(1, cls.getPresentations().length);
	}
	
	@Test
	public void checkEnumeration() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls = findRootSubElement("Enumeration1", IClass.class);
		assertNotNull(cls);
		assertArrayEquals(new String[] {"enum"}, cls.getStereotypes());
//		assertEquals(1, cls.getPresentations().length);
		
		IAttribute[] attributes = cls.getAttributes();
		assertEquals(4, attributes.length);
		assertEquals("attr2", attributes[0].getName());
		assertEquals("attr3", attributes[1].getName());
		assertEquals("attr0", attributes[2].getName());
		assertEquals("attr1", attributes[3].getName());
		assertArrayEquals(new String[] {"enum constant"}, attributes[0].getStereotypes());
		assertArrayEquals(new String[] {"enum constant"}, attributes[1].getStereotypes());
		//assertArrayEquals(new String[] {"enum"}, attributes[2].getStereotypes());
		assertArrayEquals(new String[] {}, attributes[2].getStereotypes());
		assertEquals(0, attributes[3].getStereotypes().length);
		
		assertEquals(1, cls.getOperations().length);
		assertEquals("op1", cls.getOperations()[0].getName());
	}
	
	@Test
	public void checkPrimtivie() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls = findRootSubElement("PrimitiveType1", IClass.class);
		assertNotNull(cls);
		assertArrayEquals(new String[] {"primitive"}, cls.getStereotypes());
//		assertEquals(1, cls.getPresentations().length);
	}
	
	@Test
	public void checkIgnoreAssociaiton() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls = findRootSubElement("AssClass", IClass.class);
		assertNull(cls);
	}

	@Test
	public void checkTable() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls = findRootSubElement("Table1", IClass.class);
		assertNotNull(cls);
		//assertArrayEquals(new String[] {"table"}, cls.getStereotypes());
		assertArrayEquals(new String[] {}, cls.getStereotypes());
//		assertEquals(1, cls.getPresentations().length);
		
//		ITaggedValue[] taggedValues = cls.getTaggedValues();
//		assertEquals(3, taggedValues.length);
//		String[] keys = new String[] {"DBVERSION", "OWNER", "TABLESPACE"};
//		for (int i = 0; i < keys.length; i++) {
//			assertEquals(keys[i], taggedValues[i].getKey());
//			assertEquals("", taggedValues[i].getValue());
//		}
	}
	
	@Test
	public void checkSignal() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls = findRootSubElement("Signal1", IClass.class);
		assertNotNull(cls);
		assertArrayEquals(new String[] {"signal"}, cls.getStereotypes());
//		assertEquals(1, cls.getPresentations().length);
	}
}
