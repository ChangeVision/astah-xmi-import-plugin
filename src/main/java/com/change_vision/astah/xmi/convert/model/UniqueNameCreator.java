package com.change_vision.astah.xmi.convert.model;

import com.change_vision.jude.api.inf.model.INamedElement;

public class UniqueNameCreator {

    public String getUniqueName(INamedElement[] elements,final String name){
        if(name == null) return "";
        if(elements == null) return name;
        int count = -1;
        String result = name;
        for (INamedElement element : elements) {
            if(element.getName().equals(result)){
                count++;
                result = name + count;
            }
        }
        return result;
    }
    
}
