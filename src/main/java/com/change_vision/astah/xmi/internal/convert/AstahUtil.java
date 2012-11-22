package com.change_vision.astah.xmi.internal.convert;

import java.io.File;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IHyperlink;
import com.change_vision.jude.api.inf.model.IHyperlinkOwner;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IPackage;

public class AstahUtil {
	
	private static final String PRIMITIVE_PKG = "Primitive Package";
	public static final String[] ASTAH_PRIMITIVES = new String[] {
		"boolean", "byte", "char", "double", "float", "int", "long", "short", "void",	//java
		"long long", "unsigned long long", "wchar", "octet", "sequence", "wstring",	//IDL
		"bool", "decimal", "object", "sbyte", "string", "uint", "ulong", "ushort",	//C#
		"signed char", "unsigned char", "unsigned short", "short int", "signed unsigned short",	//C++
		"unsigned short int", "signed int", "unsigned int", "unsigned long", "long int",	//C++
		"signed long int", "unsigned long int", "long double", "wchar_t"	//C++
	};
	
	private static AstahAPIUtil util = new AstahAPIUtil();

	public static String getUniqueName(INamedElement[] children, String name) {
		for (INamedElement child : children) {
			if (child.getName().equals(name)) {
				return getUniqueName(children, name + "0");
			}
		}
		return name;
	}
	
	public static IClass getOrCreatePrimitiveClass(String name) throws InvalidEditingException {
		if (name == null) {
			return null;
		}
		BasicModelEditor bme = util.getBasicModelEditor();
		
		IModel root = util.getProject();
		IPackage primitivePkg = findModel(root, PRIMITIVE_PKG, IPackage.class);
		if (primitivePkg == null) {
			primitivePkg = bme.createPackage(root, PRIMITIVE_PKG);
		}
		IClass primitiveClass = findModel(primitivePkg, name, IClass.class);
		if (primitiveClass == null) {
			primitiveClass = bme.createClass(primitivePkg, name);
			primitiveClass.addStereotype("primitive");
		}
		return primitiveClass;
	}

	private static <T extends INamedElement> T findModel(IPackage parent, String name, Class<T> clazz) {
		for (INamedElement child : parent.getOwnedElements()) {
			if (clazz.isInstance(child) && child.getName().equals(name)) {
				return clazz.cast(child);
			}
		}
		return null;
	}
	
	public static String toMultiplicityRange(int range) {
		if (range == -1) {
			return "*";
		}
		return String.valueOf(range);
	}
	
	public static IHyperlink createFileHyperlink(IHyperlinkOwner owner, String nameAndPath,
			String comment) throws InvalidEditingException {
		String name = nameAndPath;
		String path = "";
        int lastSeparator = nameAndPath.lastIndexOf(File.separator);
        if (lastSeparator != -1 ){
        	name = nameAndPath.substring(lastSeparator + 1);
            path = nameAndPath.substring(0,lastSeparator);
        }
		return owner.createFileHyperlink(name, path, comment);
	
	}
}
