package com.change_vision.astah.xmi;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IMessage;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IOperation;
import com.change_vision.jude.api.inf.model.ISequenceDiagram;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.change_vision.jude.api.inf.presentation.INodePresentation;

@Ignore(value="現バージョンではプレゼンテーションはサポートしません。")
public class XmiImporterSeqMsgTest extends BasicXmiImporterTest {


	@BeforeClass
	public static void setup() throws IOException, LicenseNotFoundException,
			ProjectNotFoundException, ProjectLockedException, ClassNotFoundException,
			InvalidEditingException, InvalidUsingException {
		BasicXmiImporterTest.setup("seqMsg");
	}
	
	@Ignore
	@Test
	public void checkMsg1() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IMessage msg = findAndCheckMsg("msg1", "Object1", "Object2");
		assertNull(msg.getOperation());
		assertEquals("arg1", msg.getArgument());
		assertEquals("retVal1", msg.getReturnValue());
		assertEquals("", msg.getReturnValueVariable());
		assertArrayEquals(new String[] {"become", "copy"}, msg.getStereotypes());
		assertTrue(msg.isAsynchronous());
//		assertFalse(msg.isCreateMessage());
//		assertFalse(msg.isDestroyMessage());
		//sequence diagram not allows alias, definition
		assertEquals("", msg.getAlias1());
		assertEquals("", msg.getDefinition());
	}

	@Ignore
	@Test
	public void checkCreateMsg() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
//		IMessage msg = findAndCheckMsg("createMsg0", "Object2", "Object3");
//		assertTrue(msg.isCreateMessage());
	}
	
	@Ignore
	@Test
	public void checkDestroyMsg() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
//		IMessage msg = findAndCheckMsg("deleteMsg0", "Object2", "Object3");
//		assertTrue(msg.isDestroyMessage());
	}
	
	@Ignore
	@Test
	public void checkMsg2() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass cls = findRootSubElement("Class0", IClass.class);
		IOperation op = findNamedElement(cls.getOperations(), "op3", IOperation.class);
		assertNotNull(op);
		
		IMessage msg = findAndCheckMsg("op3", "Object2", "Object3");
		assertEquals(op, msg.getOperation());
		assertEquals("", msg.getArgument());
		assertEquals("void", msg.getReturnValue());
		assertEquals("", msg.getReturnValueVariable());
		assertEquals(0, msg.getStereotypes().length);
		assertEquals("", msg.getAlias1());
		assertTrue(msg.isSynchronous());
//		assertFalse(msg.isCreateMessage());
//		assertFalse(msg.isDestroyMessage());
		assertFalse(msg.isReturnMessage());
		assertEquals("", msg.getDefinition());
	}
	
	@Test
	public void checkRetMsg() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IMessage msg = findAndCheckMsg("retMsg2", "Object3", "Object2");
		assertTrue(msg.isReturnMessage());
	}
	
	@Test
	public void checkLostMsg() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		findAndCheckMsg("lostMsg0", "Object3", null);
	}
	
	@Test
	public void checkFoundMsg() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		findAndCheckMsg("foundMsg0", null, "Object1");
	}
	
	@Test
	public void checkSelfMsg() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		findAndCheckMsg("selfMsg0", "Object1", "Object1");
	}
	
	private IMessage findAndCheckMsg(String name, String source, String target) throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		ISequenceDiagram dgm = findNamedElement(getRoot().getDiagrams(), "seqDgm0", ISequenceDiagram.class);
		assertNotNull(dgm);
		ILinkPresentation ps = (ILinkPresentation) findPresentation(dgm, name);
		assertNotNull(ps);
		assertEquals("Message", ps.getType());

		INamedElement sourceModel = null;
		if (source != null) {
			INodePresentation sourcePs = (INodePresentation) findPresentation(dgm, source);
			assertNotNull(sourcePs);
			assertEquals("Lifeline", sourcePs.getType());
			sourceModel = (INamedElement) sourcePs.getModel();
		}
		
		INamedElement targetModel = null;
		if (target != null) {
			INodePresentation targetPs = (INodePresentation) findPresentation(dgm, target);
			assertNotNull(targetPs);
			assertEquals("Lifeline", targetPs.getType());
			targetModel = (INamedElement) targetPs.getModel();
		}
		
		IMessage msg = (IMessage) ps.getModel();
		assertEquals(sourceModel, msg.getSource());
		assertEquals(targetModel, msg.getTarget());
		return msg;
	}
}
