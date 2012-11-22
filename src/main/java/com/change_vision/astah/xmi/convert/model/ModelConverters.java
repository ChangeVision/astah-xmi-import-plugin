package com.change_vision.astah.xmi.convert.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.ConvertHelper;

public class ModelConverters {

    private List<ModelConverter> converters = new ArrayList<ModelConverter>();
    private AstahAPIUtil util = new AstahAPIUtil();
    private ConvertHelper helper = new ConvertHelper();
    
    public ModelConverters(){
        this.converters.add(new PackageConverter(util, helper));
        this.converters.add(new ClassConverter(util, helper));
        this.converters.add(new InterfaceConverter(util, helper));
        this.converters.add(new DataTypeConverter(util, helper));
    }
    
    public List<ModelConverter> getConverters() {
        return Collections.unmodifiableList(converters);
    }
}
