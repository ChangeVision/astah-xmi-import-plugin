package com.change_vision.astah.xmi.internal.convert;

import com.change_vision.astah.xmi.internal.convert.exception.ContentsEmptyException;

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
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

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
        UMLResourcesUtil.init(resourceSet);
        initUriMap();
        preloadLibraries(resourceSet);
        resourceSet.getLoadOptions().put(XMLResource.OPTION_RECORD_UNKNOWN_FEATURE, Boolean.TRUE);
        return resourceSet;
    }

    private void initUriMap() {
      URI umlResourceURI = URI.createURI("platform:/plugin/org.eclipse.uml2.uml/");
      URI actualUMLResourceURI = getBaseUMLResourceURI();
      URIConverter.URI_MAP.put(umlResourceURI, actualUMLResourceURI);
    }

    private void preloadLibraries(ResourceSet resourceSet) {
        URI profileURI = URI.createURI(UMLResource.STANDARD_PROFILE_URI);
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

    public static String getId(EObject e) {
        return EcoreUtil.getURI(e).fragment();
    }
}
