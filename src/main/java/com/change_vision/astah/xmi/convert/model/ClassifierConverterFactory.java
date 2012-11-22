package com.change_vision.astah.xmi.convert.model;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.internal.convert.ConvertHelper;

public interface ClassifierConverterFactory {
    
    public ClassifierConverter createConverter(AstahAPIUtil util, ConvertHelper helper);

}
