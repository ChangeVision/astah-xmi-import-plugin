package learning;

import static util.UML2TestUtil.*;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.TemplateSignature;
import org.junit.Test;

public class UML2ModelLearning {
    
    @Test
    public void learningTemplateBinding() throws Exception {
        TemplateSignature signature = createTemplateSignature();
        Class clazz = createClass("TestTemplateBinding");
        clazz.createTemplateBinding(signature);
        for (Element element : clazz.getOwnedElements()) {
            System.out.println(element);
        }
    }
    
    @Test
    public void learningPackageImport() throws Exception {
        Package pack = createPackage("Hoge");
        Package imported = createPackage("Imporeted");
        pack.createPackageImport(imported);
        for (Element element : pack.getOwnedElements()) {
            System.out.println(element);
        }
    }

}
