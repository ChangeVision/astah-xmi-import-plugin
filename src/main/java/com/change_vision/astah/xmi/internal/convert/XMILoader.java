package com.change_vision.astah.xmi.internal.convert;

import java.net.URL;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.CMOF202UMLResource;
import org.eclipse.uml2.uml.resource.CMOF242UMLResource;
import org.eclipse.uml2.uml.resource.CMOF2UMLExtendedMetaData;
import org.eclipse.uml2.uml.resource.CMOF2UMLResource;
import org.eclipse.uml2.uml.resource.UML212UMLExtendedMetaData;
import org.eclipse.uml2.uml.resource.UML212UMLResource;
import org.eclipse.uml2.uml.resource.UML22UMLExtendedMetaData;
import org.eclipse.uml2.uml.resource.UML22UMLResource;
import org.eclipse.uml2.uml.resource.UML302UMLExtendedMetaData;
import org.eclipse.uml2.uml.resource.UML302UMLResource;
import org.eclipse.uml2.uml.resource.UML402UMLExtendedMetaData;
import org.eclipse.uml2.uml.resource.UML402UMLResource;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resource.XMI212UMLResource;
import org.eclipse.uml2.uml.resource.XMI222UMLResource;
import org.eclipse.uml2.uml.resource.XMI242UMLResource;
import org.eclipse.uml2.uml.resource.XMI2UMLExtendedMetaData;
import org.eclipse.uml2.uml.resource.XMI2UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.change_vision.astah.xmi.internal.convert.exception.ContentsEmptyException;

public class XMILoader {

    private static final Logger logger = LoggerFactory.getLogger(XMILoader.class);

    private XMIResource res;

    public XMILoader(String xmiPath) throws ContentsEmptyException {
        ResourceSet resourceSet = createResourceSet();
        URI uri = URI.createURI(xmiPath);
        logger.info("load uri {}",uri);
        this.res = (XMIResource) resourceSet.getResource(uri, true);
        if (this.res.getContents().isEmpty()) {
            throw new ContentsEmptyException("Loaded xmi file's contents are empty.");
        }
    }

    public Package getRoot() {
        return (Package) EcoreUtil.getObjectByType(res.getContents(), UMLPackage.Literals.PACKAGE);
    }

    private ResourceSet createResourceSet() {
        ResourceSet resourceSet = new ResourceSetImpl();
        preloadLibraries(resourceSet);
        resourceSet.getLoadOptions().put(XMLResource.OPTION_RECORD_UNKNOWN_FEATURE, Boolean.TRUE);
        return resourceSet;
    }

    private void preloadLibraries(ResourceSet resourceSet) {
        UMLResourcesUtil.init(resourceSet);
        init(resourceSet);
        URI profileURI = URI.createURI(UMLResource.STANDARD_PROFILE_URI);
        URIConverter uriConverter = resourceSet.getURIConverter();
        uriConverter.getURIMap().put(URI.createURI("platform:/plugin/org.eclipse.uml2.uml/model/"),
                getBaseUMLResourceURI().appendFragment("/model/"));
        resourceSet.getResource(profileURI, true);
    }

    private URI getBaseUMLResourceURI() {
        URI umlMetamodel = URI.createURI(UMLResource.UML_METAMODEL_URI);
        URL resultURL = UMLResourcesUtil.class.getClassLoader().getResource(
                String.format("metamodels/%s", umlMetamodel.lastSegment())); //$NON-NLS-1$

        URI result;

        if (resultURL != null) {
            // remove the /metamodel/UML.metamodel.uml segments of the resource
            // we found
            result = URI.createURI(resultURL.toExternalForm(), true).trimSegments(2);
        } else {
            // probably, we're not running with JARs, so assume the source
            // project folder layout
            resultURL = UMLResourcesUtil.class.getResource("UMLResourcesUtil.class"); //$NON-NLS-1$

            String baseURL = resultURL.toExternalForm();
            baseURL = baseURL.substring(0, baseURL.lastIndexOf("/bin/")); //$NON-NLS-1$
            result = URI.createURI(baseURL, true);
        }

        return result;
    }
    private void init(ResourceSet resourceSet) {

        Map<String, Object> contentTypeToFactoryMap = resourceSet.getResourceFactoryRegistry()
                .getContentTypeToFactoryMap();

        contentTypeToFactoryMap.put(UML402UMLResource.UML_4_0_0_CONTENT_TYPE_IDENTIFIER,
                UMLResource.Factory.INSTANCE);
        contentTypeToFactoryMap.put(UML302UMLResource.UML_3_0_0_CONTENT_TYPE_IDENTIFIER,
                UMLResource.Factory.INSTANCE);
        contentTypeToFactoryMap.put(UML212UMLResource.UML_2_1_0_CONTENT_TYPE_IDENTIFIER,
                UMLResource.Factory.INSTANCE);
        contentTypeToFactoryMap.put(UML212UMLResource.UML_2_0_0_CONTENT_TYPE_IDENTIFIER,
                UMLResource.Factory.INSTANCE);
        contentTypeToFactoryMap.put(UML22UMLResource.UML2_CONTENT_TYPE_IDENTIFIER,
                UMLResource.Factory.INSTANCE);
        contentTypeToFactoryMap.put(XMI2UMLResource.UML_CONTENT_TYPE_IDENTIFIER,
                UMLResource.Factory.INSTANCE);
        contentTypeToFactoryMap.put(XMI242UMLResource.UML_2_4_1_CONTENT_TYPE_IDENTIFIER,
                UMLResource.Factory.INSTANCE);
        contentTypeToFactoryMap.put(XMI242UMLResource.UML_2_4_CONTENT_TYPE_IDENTIFIER,
                UMLResource.Factory.INSTANCE);
        contentTypeToFactoryMap.put(XMI222UMLResource.UML_2_2_CONTENT_TYPE_IDENTIFIER,
                UMLResource.Factory.INSTANCE);
        contentTypeToFactoryMap.put(XMI212UMLResource.UML_2_1_1_CONTENT_TYPE_IDENTIFIER,
                UMLResource.Factory.INSTANCE);
        contentTypeToFactoryMap.put(XMI212UMLResource.UML_2_1_CONTENT_TYPE_IDENTIFIER,
                UMLResource.Factory.INSTANCE);
        contentTypeToFactoryMap.put(CMOF2UMLResource.CMOF_CONTENT_TYPE_IDENTIFIER,
                UMLResource.Factory.INSTANCE);
        contentTypeToFactoryMap.put(CMOF242UMLResource.CMOF_2_4_1_CONTENT_TYPE_IDENTIFIER,
                UMLResource.Factory.INSTANCE);
        contentTypeToFactoryMap.put(CMOF242UMLResource.CMOF_2_4_CONTENT_TYPE_IDENTIFIER,
                UMLResource.Factory.INSTANCE);
        contentTypeToFactoryMap.put(CMOF202UMLResource.CMOF_2_0_CONTENT_TYPE_IDENTIFIER,
                UMLResource.Factory.INSTANCE);

        Map<URI, URI> uriMap = resourceSet.getURIConverter().getURIMap();

        uriMap.putAll(UML402UMLExtendedMetaData.getURIMap());
        uriMap.putAll(UML302UMLExtendedMetaData.getURIMap());
        uriMap.putAll(UML212UMLExtendedMetaData.getURIMap());
        uriMap.putAll(UML22UMLExtendedMetaData.getURIMap());
        uriMap.putAll(XMI2UMLExtendedMetaData.getURIMap());
        uriMap.putAll(CMOF2UMLExtendedMetaData.getURIMap());
    }

    public static String getId(EObject e) {
        return EcoreUtil.getURI(e).fragment();
    }
}
