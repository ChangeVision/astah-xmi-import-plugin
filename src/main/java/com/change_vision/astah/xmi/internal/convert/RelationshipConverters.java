package com.change_vision.astah.xmi.internal.convert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.Element;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.relationship.RelationshipConverter;
import com.change_vision.astah.xmi.internal.convert.relationship.AssociationClassConverter;
import com.change_vision.astah.xmi.internal.convert.relationship.AssociationConverter;
import com.change_vision.astah.xmi.internal.convert.relationship.DependencyConverter;
import com.change_vision.astah.xmi.internal.convert.relationship.GeneralizationConverter;
import com.change_vision.astah.xmi.internal.convert.relationship.PackageImportConverter;
import com.change_vision.astah.xmi.internal.convert.relationship.PackageMergeConverter;
import com.change_vision.astah.xmi.internal.convert.relationship.RealizationConverter;
import com.change_vision.astah.xmi.internal.convert.relationship.UsageConverter;
import com.change_vision.jude.api.inf.model.IElement;

public class RelationshipConverters {
    
    private List<RelationshipConverter> converters = new ArrayList<RelationshipConverter>();
    
    public RelationshipConverters(Map<Element, IElement> converteds, AstahAPIUtil util){
        this.converters.add(new GeneralizationConverter(converteds, util));
        this.converters.add(new UsageConverter(converteds, util));
        this.converters.add(new RealizationConverter(converteds, util));
        this.converters.add(new DependencyConverter(converteds, util));
        this.converters.add(new AssociationClassConverter(converteds, util));
        this.converters.add(new AssociationConverter(converteds,util));
        this.converters.add(new PackageImportConverter(converteds, util));
        this.converters.add(new PackageMergeConverter(converteds, util));
//        this.converters.add(new TemplateBindingConverter(converteds, util));
    }
    
    void addConverter(RelationshipConverter converter){
        this.converters.add(converter);
    }
    
    public List<RelationshipConverter> getConverters() {
        return Collections.unmodifiableList(converters);
    }
    
}
