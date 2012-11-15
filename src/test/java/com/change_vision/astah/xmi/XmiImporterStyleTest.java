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
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.change_vision.jude.api.inf.presentation.INodePresentation;

@Ignore(value="現バージョンではプレゼンテーションはサポートしません。")
public class XmiImporterStyleTest extends BasicXmiImporterTest {

	@BeforeClass
	public static void setup() throws IOException, LicenseNotFoundException,
			ProjectNotFoundException, ProjectLockedException, ClassNotFoundException,
			InvalidEditingException, InvalidUsingException {
		BasicXmiImporterTest.setup("style");
	}
	
	@Test
	public void checkFontColor() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls1 = findRootSubElement("Class2", IClass.class);
		assertNotNull(cls1);
		assertEquals(1, cls1.getPresentations().length);
		INodePresentation clsPs = (INodePresentation) cls1.getPresentations()[0];
		assertEquals("#FF0000", clsPs.getProperty("font.color"));	//red
	}
	
	@Test
	public void checkFontColor2() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls1 = findRootSubElement("Class4", IClass.class);
		assertNotNull(cls1);
		assertEquals(1, cls1.getPresentations().length);
		INodePresentation clsPs = (INodePresentation) cls1.getPresentations()[0];
		assertEquals("#FFD700", clsPs.getProperty("font.color"));	//yellow
	}
	
	@Test
	public void checkFillColor() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls1 = findRootSubElement("Class3", IClass.class);
		assertNotNull(cls1);
		assertEquals(1, cls1.getPresentations().length);
		INodePresentation clsPs = (INodePresentation) cls1.getPresentations()[0];
		assertEquals("#00FF00", clsPs.getProperty("fill.color"));	//green
	}
	
	@Test
	public void checkFillColor2() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls1 = findRootSubElement("Class4", IClass.class);
		assertNotNull(cls1);
		assertEquals(1, cls1.getPresentations().length);
		INodePresentation clsPs = (INodePresentation) cls1.getPresentations()[0];
		assertEquals("#123456", clsPs.getProperty("fill.color"));	//green
	}
	
	@Test
	public void checkLine1() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls = findRootSubElement("Class3", IClass.class);
		assertNotNull(cls);
		assertEquals(1, cls.getPresentations().length);
		INodePresentation clsPs = (INodePresentation) cls.getPresentations()[0];
		assertEquals(1, clsPs.getLinks().length);
		ILinkPresentation linkPs = clsPs.getLinks()[0];
		assertEquals("#FF0000", linkPs.getProperty("line.color"));	//red
	}
	
	@Test
	public void checkLine2() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls = findRootSubElement("Class4", IClass.class);
		assertNotNull(cls);
		assertEquals(1, cls.getPresentations().length);
		INodePresentation clsPs = (INodePresentation) cls.getPresentations()[0];
		assertEquals(1, clsPs.getLinks().length);
		ILinkPresentation linkPs = clsPs.getLinks()[0];
		assertEquals("#0000FF", linkPs.getProperty("line.color"));	//blue
	}


}
