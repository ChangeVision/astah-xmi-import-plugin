package com.change_vision.astah.xmi.convert.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.ConvertHelper;

public class ClassifierModelConverters {

    private List<ClassifierConverter> converters = new ArrayList<ClassifierConverter>();
    
    public ClassifierModelConverters(AstahAPIUtil util, ConvertHelper helper){
        this.converters.add(new PackageConverter(util, helper));
        this.converters.add(new ClassConverter(util, helper));
        this.converters.add(new InterfaceConverter(util, helper));
        this.converters.add(new DataTypeConverter(util, helper));
    }
    
    public List<ClassifierConverter> getConverters() {
        return Collections.unmodifiableList(converters);
    }
}
