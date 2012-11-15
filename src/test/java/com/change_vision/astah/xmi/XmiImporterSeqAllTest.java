package com.change_vision.astah.xmi;

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
import com.change_vision.jude.api.inf.model.ICombinedFragment;
import com.change_vision.jude.api.inf.model.IGate;
import com.change_vision.jude.api.inf.model.IHyperlink;
import com.change_vision.jude.api.inf.model.IInteractionOperand;
import com.change_vision.jude.api.inf.model.IInteractionUse;
import com.change_vision.jude.api.inf.model.ILifeline;
import com.change_vision.jude.api.inf.model.IMessage;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.ISequenceDiagram;
import com.change_vision.jude.api.inf.model.IStateInvariant;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.presentation.IPresentation;

@Ignore(value="現バージョンではプレゼンテーションはサポートしません。")
public class XmiImporterSeqAllTest extends BasicXmiImporterTest {


	@BeforeClass
	public static void setup() throws IOException, LicenseNotFoundException,
			ProjectNotFoundException, ProjectLockedException, ClassNotFoundException,
			InvalidEditingException, InvalidUsingException {
		BasicXmiImporterTest.setup("seqAll");
	}
	
	@Test
	public void checkDiagram() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		ISequenceDiagram dgm0 = findNamedElement(getRoot().getDiagrams(), "seqDgm0", ISequenceDiagram.class);
		assertNotNull(dgm0);
		ISequenceDiagram dgm1 = findNamedElement(getRoot().getDiagrams(), "seqDgm1", ISequenceDiagram.class);
		assertNotNull(dgm1);
	}
	
	@Test
	public void checkObjectColor() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		checkLifelineAndActivationColor("Object1", "#FF0000");
		checkLifelineAndActivationColor("Object2", "#00FF00");
	}
	
	@Test
	public void checkMsgColor() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IPresentation ps = findPresentation("msg21");
		assertEquals("Message", ps.getType());
		assertEquals("#FF0000", ps.getProperty("line.color"));
		
		IPresentation ps2 = findPresentation("msg22");
		assertEquals("Message", ps2.getType());
		assertEquals("#800080", ps2.getProperty("line.color"));
	}
	
	@Test
	public void checkObjectFontColor() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IPresentation ps = findPresentation("Object3");
		assertEquals("Lifeline", ps.getType());
		assertEquals("#00CCFF", ps.getProperty("font.color"));
	}
	
	@Test
	public void checkInteractionUseColor() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IPresentation ps = findPresentation("seqDgm11");
		assertEquals("InteractionUse", ps.getType());
		assertEquals("#CCFFCC", ps.getProperty("fill.color"));
		assertEquals("#FF0000", ps.getProperty("font.color"));
	}
	
	private void checkLifelineAndActivationColor(String name, String color) throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		INodePresentation objPs = (INodePresentation) findPresentation(name);
		assertNotNull(objPs);
		assertEquals("Lifeline", objPs.getType());
		assertEquals(color, objPs.getProperty("fill.color"));
		for (INodePresentation child : objPs.getChildren()) {
			if (child.getType().equals("Activation")) {
				assertEquals(color, child.getProperty("fill.color"));
			}
		}
	}
	
	@Test
	public void checkObjectTaggedValue() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		INodePresentation lifelinePs = (INodePresentation) findPresentation("Object1");
		ILifeline lifeline = (ILifeline) lifelinePs.getModel();
		
		assertEquals(3, lifeline.getTaggedValues().length);	//and two hyperlinks
		assertEquals("objVal1", lifeline.getTaggedValue("objTag1"));
	}
	
	@Test
	public void checkObjectHyperlink() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		INodePresentation lifelinePs = (INodePresentation) findPresentation("Object1");
		ILifeline lifeline = (ILifeline) lifelinePs.getModel();
		
		IHyperlink[] hyperlinks = lifeline.getHyperlinks();
		assertEquals(2, hyperlinks.length);
		assertTrue(hyperlinks[0].isFile());
		assertEquals("1.txt", hyperlinks[0].getName());
		assertEquals("e:", hyperlinks[0].getPath());
		assertTrue(hyperlinks[1].isURL());
		assertEquals("www.google.com", hyperlinks[1].getName());
		assertEquals("", hyperlinks[1].getPath());
	}

	@Test
	public void checkCombinedFragment1() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		INodePresentation fragPs = (INodePresentation) findPresentation("frag1");
		ICombinedFragment frag = (ICombinedFragment) fragPs.getModel();
		assertNotNull(frag);
		assertTrue(frag.isAlt());
		assertEquals(1, frag.getInteractionOperands().length);
		IInteractionOperand operand = frag.getInteractionOperands()[0];
		assertEquals("", operand.getGuard());
		assertEquals(1, operand.getMessages().length);
		IMessage msg1 = findAndCheckMsg("msg1", "Object1", "Object2");
		assertMsgAndOperand(msg1, operand);
		
		assertEquals(0, fragPs.getLinks().length);
		assertNull(findPresentation("msg12"));
	}
	
	@Test
	public void checkCombinedFragment2() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		INodePresentation fragPs = (INodePresentation) findPresentation("frag2");
		ICombinedFragment frag = (ICombinedFragment) fragPs.getModel();
		assertNotNull(frag);
		assertTrue(frag.isAssert());
		IInteractionOperand[] operands = frag.getInteractionOperands();
		assertEquals(3, operands.length);
		assertEquals(1, operands[0].getMessages().length);
		assertEquals(1, operands[1].getMessages().length);
		assertEquals(1, operands[2].getMessages().length);
		assertEquals("op1", operands[0].getGuard());
		assertEquals("op2", operands[1].getGuard());
		assertEquals("op3", operands[2].getGuard());
		IMessage msg21 = findAndCheckMsg("msg21", "Object2", "Object3");
		assertMsgAndOperand(msg21, operands[0]);
		IMessage msg22 = findAndCheckMsg("msg22", "Object2", "Object3");
		assertMsgAndOperand(msg22, operands[1]);
		IMessage msg23 = findAndCheckMsg("msg23", "Object2", "Object3");
		assertMsgAndOperand(msg23, operands[2]);
		
	}
	
	@Test
	public void checkCombinedFragment3() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		INodePresentation fragPs = (INodePresentation) findPresentation("frag3");
		ICombinedFragment frag = (ICombinedFragment) fragPs.getModel();
		assertNotNull(frag);
		assertTrue(frag.isLoop());
		IInteractionOperand[] operands = frag.getInteractionOperands();
		assertEquals(1, operands.length);
		assertEquals(1, operands[0].getMessages().length);
		assertEquals("op1", operands[0].getGuard());
		IMessage msg = findAndCheckMsg("selfMsg2", "Object4", "Object4");
		assertMsgAndOperand(msg, operands[0]);
		
	}
	
	private void assertMsgAndOperand(IMessage msg, IInteractionOperand operand) {
		assertEquals(msg, operand.getMessages()[0]);
//		assertEquals(operand, msg.getContainer());
//		assertEquals(1, msg.getContainers().length);
//		assertEquals(operand, msg.getContainers()[0]);
	}

	@Test
	public void checkInteractionUse1() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		INodePresentation interUsePs = (INodePresentation) findPresentation("seqDgm11");
		IInteractionUse interUse = (IInteractionUse) interUsePs.getModel();
		assertNotNull(interUse);
//		assertEquals(0, interUse.getStereotypes().length);
		assertEquals("arg2", interUse.getArgument());
		
		ISequenceDiagram seqDgm1 = findNamedElement(getRoot().getDiagrams(), "seqDgm1", ISequenceDiagram.class);
		assertNotNull(seqDgm1);
		assertEquals(seqDgm1, interUse.getSequenceDiagram());
		
		ILinkPresentation mp = (ILinkPresentation) findPresentation("msg01");
		assertNotNull(mp);
		assertEquals(findPresentation("Object2").getModel(), ((IMessage) mp.getModel()).getSource());
		INamedElement target = ((IMessage) mp.getModel()).getTarget();
		assertTrue(target instanceof IGate);
		assertEquals(interUsePs, mp.getTarget());
	}

	@Test
	public void checkInteractionUseTaggedValue() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		INodePresentation interUsePs = (INodePresentation) findPresentation("seqDgm11");
		IInteractionUse interUse = (IInteractionUse) interUsePs.getModel();
		assertNotNull(interUse);
		assertEquals(2, interUse.getTaggedValues().length);
		assertEquals("val1", interUse.getTaggedValue("tag1"));
		assertEquals("val2", interUse.getTaggedValue("tag2"));
	}
	
	public void checkInteractionUse2() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		INodePresentation interUsePs = (INodePresentation) findPresentation("seqDgm1");
		IInteractionUse interUse = (IInteractionUse) interUsePs.getModel();
		assertNotNull(interUse);
		assertEquals(0, interUse.getStereotypes().length);
		assertEquals("", interUse.getArgument());
		assertEquals(0, interUse.getTaggedValues().length);
		
		ISequenceDiagram seqDgm1 = findNamedElement(getRoot().getDiagrams(), "seqDgm1", ISequenceDiagram.class);
		assertNotNull(seqDgm1);
		assertEquals(seqDgm1, interUse.getSequenceDiagram());
	}
	
	public void checkStateInvariant() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		INodePresentation interUsePs = (INodePresentation) findPresentation("{State1}");
		IStateInvariant state = (IStateInvariant) interUsePs.getModel();
		assertNotNull(state);
		assertEquals(0, state.getStereotypes().length);
		assertEquals(0, state.getTaggedValues().length);
	}

	
	private IPresentation findPresentation(String name) throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		ISequenceDiagram dgm = findNamedElement(getRoot().getDiagrams(), "seqDgm0", ISequenceDiagram.class);
		assertNotNull(dgm);
		IPresentation ps = findPresentation(dgm, name);
//		assertNotNull(ps);
		return ps;
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
