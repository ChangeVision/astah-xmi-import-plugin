package com.change_vision.astah.xmi.internal.convert;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.uml2.common.util.UML2Util;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resource.XMI2UMLResource;

import com.change_vision.astah.xmi.internal.convert.exception.ContentsEmptyException;


public class XMILoader {
	
	private XMIResource res;
	
	public XMILoader(String xmiPath) throws ContentsEmptyException {
		ResourceSet resourceSet = createResourceSet();
		URI uri = URI.createURI(xmiPath);
		UML2Util.load(resourceSet, URI.createURI(xmiPath), UMLPackage.Literals.PACKAGE);
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
		regestryPackage(resourceSet);
		regesitryExtension(resourceSet);		
		resourceSet.getLoadOptions().put(XMLResource.OPTION_RECORD_UNKNOWN_FEATURE, Boolean.TRUE);
		return resourceSet;
	}

	private void regesitryExtension(ResourceSet resourceSet) {
		Map<String, Object> extensionToFactoryMap = resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap();
		extensionToFactoryMap.put(XMI2UMLResource.FILE_EXTENSION, new XMIResourceFactoryImpl());
		extensionToFactoryMap.put("xml", new XMIResourceFactoryImpl());
		extensionToFactoryMap.put("uml", new XMIResourceFactoryImpl());
		extensionToFactoryMap.put("profile.uml", new XMIResourceFactoryImpl());
	}

	private void regestryPackage(ResourceSet resourceSet) {
		Registry packageRegistry = resourceSet.getPackageRegistry();
		packageRegistry.put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
		// UMLPackage.eNS_URI=http://www.eclipse.org/uml2/2.1.0/UML
		// This gives a ConnectException when loading the model unless 2.0.0 namespace is also registered
		packageRegistry.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		// register the UML package from org.eclipse.uml2
		packageRegistry.put("http://www.eclipse.org/uml2/1.0.0/UML", UMLPackage.eINSTANCE);
		packageRegistry.put("http://www.eclipse.org/uml2/2.0.0/UML", UMLPackage.eINSTANCE);
		packageRegistry.put("http://www.eclipse.org/uml2/2.1.0/UML", UMLPackage.eINSTANCE);
		packageRegistry.put("http://www.eclipse.org/uml2/2.2.0/UML", UMLPackage.eINSTANCE);
		packageRegistry.put("http://www.eclipse.org/uml2/2.3.0/UML", UMLPackage.eINSTANCE);
		packageRegistry.put("http://www.eclipse.org/uml2/3.0.0/UML", UMLPackage.eINSTANCE);
		packageRegistry.put("http://www.eclipse.org/uml2/3.1.0/UML", UMLPackage.eINSTANCE);
		//packageRegistry.put(Ecore2XMLPackage.eNS_URI, Ecore2XMLPackage.eINSTANCE);
		packageRegistry.put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
		// register the UML2 schema against the standard UML namespace for UML 2.0 and 2.1
		// see: http://dev.eclipse.org/newslists/news.eclipse.tools.uml2/msg03392.html
		packageRegistry.put("http://schema.omg.org/spec/XMI/2.0", UMLPackage.eINSTANCE);
		packageRegistry.put("http://schema.omg.org/spec/XMI/2.0.1", UMLPackage.eINSTANCE);
		packageRegistry.put("http://schema.omg.org/spec/XMI/2.1", UMLPackage.eINSTANCE);
		packageRegistry.put("http://schema.omg.org/spec/XMI/2.1.1", UMLPackage.eINSTANCE);
		packageRegistry.put("http://schema.omg.org/spec/XMI/2.1.2", UMLPackage.eINSTANCE);
		packageRegistry.put("http://schema.omg.org/spec/XMI/2.2", UMLPackage.eINSTANCE);
		packageRegistry.put("http://schema.omg.org/spec/XMI/2.3", UMLPackage.eINSTANCE);
		packageRegistry.put("http://www.omg.org/spec/XMI/20110701", UMLPackage.eINSTANCE);
		packageRegistry.put("http://schema.omg.org/spec/UML/2.0", UMLPackage.eINSTANCE);
		packageRegistry.put("http://schema.omg.org/spec/UML/2.1", UMLPackage.eINSTANCE);
		packageRegistry.put("http://schema.omg.org/spec/UML/2.1.1", UMLPackage.eINSTANCE);
		packageRegistry.put("http://schema.omg.org/spec/UML/2.1.2", UMLPackage.eINSTANCE);
		packageRegistry.put("http://schema.omg.org/spec/UML/2.2", UMLPackage.eINSTANCE);
		packageRegistry.put("http://schema.omg.org/spec/UML/2.3", UMLPackage.eINSTANCE);
		packageRegistry.put("http://www.omg.org/spec/UML/20090901", UMLPackage.eINSTANCE);
		packageRegistry.put("http://www.omg.org/spec/UML/20110701", UMLPackage.eINSTANCE);
		
		Map<URI, URI> uriMap = resourceSet == null
				? URIConverter.URI_MAP
				: resourceSet.getURIConverter().getURIMap();

			uriMap.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP), URI
				.createPlatformPluginURI(
					"/org.eclipse.uml2.uml.resources/libraries/", true)); //$NON-NLS-1$
			uriMap.put(URI.createURI(UMLResource.METAMODELS_PATHMAP), URI
				.createPlatformPluginURI(
					"/org.eclipse.uml2.uml.resources/metamodels/", true)); //$NON-NLS-1$
			uriMap.put(URI.createURI(UMLResource.PROFILES_PATHMAP), URI
				.createPlatformPluginURI(
					"/org.eclipse.uml2.uml.resources/profiles/", true)); //$NON-NLS-1$
	}

	public static String getId(EObject e) {
		return EcoreUtil.getURI(e).fragment();
	}
}
