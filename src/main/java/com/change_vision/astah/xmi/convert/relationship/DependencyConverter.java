package com.change_vision.astah.xmi.convert.relationship;

import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Deployment;
import org.eclipse.uml2.uml.Relationship;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.ConvertHelper;
import com.change_vision.astah.xmi.convert.exception.NotImplementedException;
import com.change_vision.jude.api.inf.model.IElement;

public class DependencyConverter implements RelationshipConverter {

    private AstahAPIUtil util;
    private ConvertHelper helper;

    public DependencyConverter(AstahAPIUtil util, ConvertHelper helper) {
        this.util = util;
        this.helper = helper;
    }

    @Override
    public boolean accepts(Relationship relationship) {
        if ((relationship instanceof Dependency) == false )return false;
        if (relationship instanceof Deployment) return false;
        return true;
    }
    
    @Override
    public IElement convert(Relationship relationship) {
        throw new NotImplementedException();
    }
    
}
