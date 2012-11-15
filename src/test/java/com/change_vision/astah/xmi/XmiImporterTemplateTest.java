package com.change_vision.astah.xmi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import com.change_vision.jude.api.inf.model.IClassifierTemplateParameter;
import com.change_vision.jude.api.inf.model.IGeneralization;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.model.ITemplateBinding;

@Ignore(value="現バージョンではプレゼンテーションはサポートしません。")
public class XmiImporterTemplateTest extends BasicXmiImporterTest {

	@BeforeClass
	public static void setup() throws IOException, LicenseNotFoundException,
			ProjectNotFoundException, ProjectLockedException, ClassNotFoundException,
			InvalidEditingException, InvalidUsingException {
		BasicXmiImporterTest.setup("template");
	}
	
	@Test
	public void checkTemplate1Parameters() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass template = findRootSubElement("Template1", IClass.class);
		assertNotNull(template);
		IClass cls1 = findRootSubElement("Class1", IClass.class);
		assertNotNull(cls1);
		IClass cls2 = findRootSubElement("Class2", IClass.class);
		assertNotNull(cls2);
		assertEquals(1, cls2.getGeneralizations().length);
		IGeneralization gen = cls2.getGeneralizations()[0];
		assertEquals(cls1, gen.getSuperType());

		IClassifierTemplateParameter[] templateParameters = template.getTemplateParameters();
		assertEquals(1, templateParameters.length);
		assertTemplateParameter(templateParameters[0], "T", cls1, cls2);
	}
	
	@Test
	public void checkTemplate2Parameters() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass template = findRootSubElement("Template2", IClass.class);
		assertNotNull(template);
		IClass cls1 = findRootSubElement("Class1", IClass.class);
		assertNotNull(cls1);
		IClass cls2 = findRootSubElement("Class2", IClass.class);
		assertNotNull(cls2);
		IClass template1 = findRootSubElement("Template1", IClass.class);
		assertNotNull(template1);
		assertEquals(0, template1.getGeneralizations().length);
		
		IClassifierTemplateParameter[] templateParameters = template.getTemplateParameters();
		assertEquals(4, templateParameters.length);
		assertTemplateParameter(templateParameters[0], "T", cls1, null);
		assertTemplateParameter(templateParameters[1], "S", null, cls2);
		assertTemplateParameter(templateParameters[2], "U", cls1, null);
		assertEquals("V", templateParameters[3].getName());
		assertTrue(templateParameters[3].getType().isPrimitiveType());
		assertEquals("int", templateParameters[3].getType().getName());
		assertEquals("10", templateParameters[3].getDefaultValue());
	}
	
	@Test
	public void checkTemplate3Parameters() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass template = findRootSubElement("Template3", IClass.class);
		assertNotNull(template);
		IPackage pkg1 = findRootSubElement("Package1", IPackage.class);
		assertNotNull(pkg1);
		IClass cls01 = findNamedElement(pkg1.getOwnedElements(), "Class01", IClass.class);
		assertNotNull(cls01);
		IClass cls0 = findNamedElement(pkg1.getOwnedElements(), "Class0", IClass.class);
		assertNotNull(cls0);
		IClassifierTemplateParameter[] templateParameters = template.getTemplateParameters();
		assertEquals(4, templateParameters.length);
		assertTemplateParameter(templateParameters[0], "T", null, cls0);
		assertTemplateParameter(templateParameters[1], "S", cls0, null);
		assertTemplateParameter(templateParameters[2], "U", cls01, null);
		assertTemplateParameter(templateParameters[3], "V", null, cls01);
	}
	
	@Test
	public void checkClass6TemplateBinding() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass template = findRootSubElement("Template1", IClass.class);
		assertNotNull(template);
		IClass cls = findRootSubElement("Class6", IClass.class);
		assertNotNull(cls);
		assertEquals(1, cls.getTemplateBindings().length);	//Template1<>
		ITemplateBinding binding = cls.getTemplateBindings()[0];
		assertEquals(template, binding.getTemplate());
		assertEquals(cls, binding.getBoundElement());
		assertEquals(0, binding.getActualMap().keySet().size());
	}
	
	@Test
	public void checkClass5TemplateBinding() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass template = findRootSubElement("Template1", IClass.class);
		assertNotNull(template);
		IClass cls5 = findRootSubElement("Class5", IClass.class);
		assertNotNull(cls5);
		IClass cls2 = findRootSubElement("Class2", IClass.class);
		assertNotNull(cls2);
		assertEquals(1, cls5.getTemplateBindings().length);
		ITemplateBinding binding = cls5.getTemplateBindings()[0];	//Template1<T->Class2>
		assertEquals(template, binding.getTemplate());
		assertEquals(cls5, binding.getBoundElement());
		assertEquals(1, binding.getActualMap().keySet().size());
		assertEquals(cls2, binding.getActualMap().get(template.getTemplateParameters()[0]));
	}
	
	@Test
	public void checkClass7TemplateBinding() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass template = findRootSubElement("Template1", IClass.class);
		assertNotNull(template);
		IClass cls5 = findRootSubElement("Class7", IClass.class);
		assertNotNull(cls5);
		assertEquals(1, cls5.getTemplateBindings().length);
		ITemplateBinding binding = cls5.getTemplateBindings()[0];	//Template1<T->"kkk"> [invalid]
		assertEquals(template, binding.getTemplate());
		assertEquals(cls5, binding.getBoundElement());
		assertEquals(0, binding.getActualMap().keySet().size());
	}
	
	@Test
	public void checkClass8TemplateBinding() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass template = findRootSubElement("Template1", IClass.class);
		assertNotNull(template);
		IClass cls5 = findRootSubElement("Class8", IClass.class);
		assertNotNull(cls5);
		assertEquals(1, cls5.getTemplateBindings().length);
		ITemplateBinding binding = cls5.getTemplateBindings()[0];	//Template1<T->Class6>  [invalid]
		assertEquals(template, binding.getTemplate());
		assertEquals(cls5, binding.getBoundElement());
		assertEquals(0, binding.getActualMap().keySet().size());
	}
	
	@Test
	public void checkClass9TemplateBinding() throws ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		IClass template1 = findRootSubElement("Template1", IClass.class);
		assertNotNull(template1);
		IClass template = findRootSubElement("Template2", IClass.class);
		assertNotNull(template);
		IClass cls5 = findRootSubElement("Class9", IClass.class);
		assertNotNull(cls5);
		assertEquals(1, cls5.getTemplateBindings().length);
		ITemplateBinding binding = cls5.getTemplateBindings()[0];	//Template2<S->Template1, V->"10">
		assertEquals(template, binding.getTemplate());
		assertEquals(cls5, binding.getBoundElement());
		assertEquals(2, binding.getActualMap().keySet().size());
		assertEquals(template1, binding.getActualMap().get(template.getTemplateParameters()[1]));
		assertEquals("20", binding.getActualMap().get(template.getTemplateParameters()[3]));
	}

	private void assertTemplateParameter(IClassifierTemplateParameter param,
			String name, Object type, Object defaultValue) {
		assertEquals(name, param.getName());
		assertEquals(type, param.getType());
		assertEquals(defaultValue, param.getDefaultValue());
	}
}
