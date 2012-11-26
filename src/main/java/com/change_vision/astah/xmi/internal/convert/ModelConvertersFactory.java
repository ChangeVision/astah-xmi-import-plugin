package com.change_vision.astah.xmi.internal.convert;


import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.model.ClassifierConverter;
import com.change_vision.astah.xmi.convert.relationship.RelationshipConverter;

public class ModelConvertersFactory implements BundleActivator {

	private static ServiceTracker classifierModelConverterTracker;
    private static ServiceTracker relationshipModelConverterTracker;

    public void start(BundleContext context) {
	    classifierModelConverterTracker = new ServiceTracker(context, ClassifierConverter.class.getName(), null);
	    classifierModelConverterTracker.open(true);
	    
	    relationshipModelConverterTracker = new ServiceTracker(context, RelationConverter.class.getName(), null);
	    relationshipModelConverterTracker.open(true);
    }

	public void stop(BundleContext context) {
	    if(classifierModelConverterTracker != null){
	        classifierModelConverterTracker.close();
	    }
	    if(relationshipModelConverterTracker != null){
	        relationshipModelConverterTracker.close();
	    }
	}
	
	public static ClassifierModelConverters createClassifierModelConverters(AstahAPIUtil util, ConvertHelper helper){
	    ClassifierModelConverters modelConverters = new ClassifierModelConverters(util, helper);
	    if(classifierModelConverterTracker == null) return modelConverters;
	    Object[] services = classifierModelConverterTracker.getServices();
	    if(services == null) return modelConverters;
	    for (Object object : services) {
            if (object instanceof ClassifierConverter) {
                ClassifierConverter converter = (ClassifierConverter) object;
                modelConverters.addConverter(converter);
            }
        }
        return modelConverters;
	}
	
	public static RelationshipConverters createRelationshipConverters(AstahAPIUtil util){
	    RelationshipConverters relationshipConverters = new RelationshipConverters(util);
	    if(relationshipModelConverterTracker == null) return relationshipConverters;
	    Object[] services = relationshipModelConverterTracker.getServices();
	    if(services == null) return relationshipConverters;
	    for (Object object : services) {
            if (object instanceof RelationshipConverter) {
                RelationshipConverter converter = (RelationshipConverter) object;
                relationshipConverters.addConverter(converter);
            }
        }
        return relationshipConverters;
	}
}
