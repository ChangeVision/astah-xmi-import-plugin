package com.change_vision.astah.xmi.convert.relationship;

import org.eclipse.uml2.uml.PackageMerge;
import org.eclipse.uml2.uml.Relationship;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.ConvertHelper;
import com.change_vision.astah.xmi.convert.exception.NotImplementedException;
import com.change_vision.jude.api.inf.model.IElement;

public class PackageMergeConverter implements RelationshipConverter {

    private AstahAPIUtil util;
    private ConvertHelper helper;

    public PackageMergeConverter(AstahAPIUtil util, ConvertHelper helper) {
        this.util = util;
        this.helper = helper;
    }

    @Override
    public boolean accepts(Relationship relationship) {
        return relationship instanceof PackageMerge;
    }
    
    @Override
    public IElement convert(Relationship relationship) {
        throw new NotImplementedException();
    }
    
}
