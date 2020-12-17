package com.change_vision.astah.xmi.internal.convert;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.net.URL;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;

public class XmiToAstahTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    @Test
    public void XMIのファイルを読み込めること() throws Exception {
        URL resource = XmiToAstahTest.class.getResource("/33.uml");
        XmiToAstah converter = new XmiToAstah(resource.getPath());
        File file = folder.newFile("33.asta");
        converter.convert(file.getCanonicalPath());
        ProjectAccessor pa = ProjectAccessorFactory.getProjectAccessor();
        
        INamedElement[] elements;
        elements = pa.findElements(IClass.class, "Child");
        assertThat(elements.length,is(1));
        elements = pa.findElements(IClass.class, "Child2");
        assertThat(elements.length,is(1));
        elements = pa.findElements(IClass.class, "Child3");
        assertThat(elements.length,is(0));
    }

}
