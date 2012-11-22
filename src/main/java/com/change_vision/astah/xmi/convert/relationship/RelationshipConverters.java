package com.change_vision.astah.xmi.convert.relationship;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.ConvertHelper;

public class RelationshipConverters {
    
    private List<RelationshipConverter> converters = new ArrayList<RelationshipConverter>();
    private AstahAPIUtil util = new AstahAPIUtil();
    private ConvertHelper helper = new ConvertHelper();
    
    public RelationshipConverters(){
        this.converters.add(new GeneralizationConverter(util, helper));
        this.converters.add(new RealizationConverter(util, helper));
        this.converters.add(new UsageConverter(util, helper));
        this.converters.add(new DependencyConverter(util, helper));
        this.converters.add(new AssociationConverter(util, helper));
        this.converters.add(new AssociationClassConverter(util, helper));
        this.converters.add(new TemplateBindingConverter(util, helper));
        this.converters.add(new PackageImportConverter(util, helper));
        this.converters.add(new PackageMergeConverter(util, helper));
    }
    
    public List<RelationshipConverter> getConverters() {
        return Collections.unmodifiableList(converters);
    }
    
}
