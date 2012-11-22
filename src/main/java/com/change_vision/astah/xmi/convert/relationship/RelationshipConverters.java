package com.change_vision.astah.xmi.convert.relationship;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.Element;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.jude.api.inf.model.IElement;

public class RelationshipConverters {
    
    private List<RelationshipConverter> converters = new ArrayList<RelationshipConverter>();
    
    public RelationshipConverters(Map<Element, IElement> converteds, AstahAPIUtil util){
        this.converters.add(new GeneralizationConverter(converteds, util));
        this.converters.add(new RealizationConverter(converteds, util));
        this.converters.add(new UsageConverter(converteds, util));
        this.converters.add(new DependencyConverter(converteds, util));
        this.converters.add(new AssociationClassConverter(converteds, util));
        this.converters.add(new AssociationConverter(converteds,util));
        this.converters.add(new PackageImportConverter(converteds, util));
        this.converters.add(new PackageMergeConverter(converteds, util));
        this.converters.add(new TemplateBindingConverter(converteds, util));
    }
    
    public List<RelationshipConverter> getConverters() {
        return Collections.unmodifiableList(converters);
    }
    
}
