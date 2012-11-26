package com.change_vision.astah.xmi.internal.convert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

public class RelationshipConverters {
    
    private List<RelationshipConverter> converters = new ArrayList<RelationshipConverter>();
    
    RelationshipConverters(AstahAPIUtil util){
        this.converters.add(new GeneralizationConverter(util));
        this.converters.add(new UsageConverter(util));
        this.converters.add(new RealizationConverter(util));
        this.converters.add(new DependencyConverter(util));
        this.converters.add(new AssociationClassConverter(util));
        this.converters.add(new AssociationConverter(util));
        this.converters.add(new PackageImportConverter(util));
        this.converters.add(new PackageMergeConverter(util));
//        this.converters.add(new TemplateBindingConverter(util));
    }
    
    void addConverter(RelationshipConverter converter){
        this.converters.add(converter);
    }
    
    public List<RelationshipConverter> getConverters() {
        return Collections.unmodifiableList(converters);
    }
    
}
