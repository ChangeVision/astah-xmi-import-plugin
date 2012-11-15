package com.change_vision.astah.xmi.convert;

import java.io.File;
import java.net.URL;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class XmiToAstahTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    @Test
    public void XMIのファイルを読み込めること() throws Exception {
        URL resource = XmiToAstahTest.class.getResource("/33.uml");
        XmiToAstah converter = new XmiToAstah(resource.toString());
        File file = folder.newFile("33.asta");
        converter.convert(file.getCanonicalPath());
    }

}
