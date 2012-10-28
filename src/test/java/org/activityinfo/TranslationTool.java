package org.activityinfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.Set;

import org.activityinfo.client.i18n.UIConstants;
import org.activityinfo.client.i18n.UIMessages;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.google.appengine.repackaged.com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.gwt.i18n.client.Messages.DefaultMessage;

public class TranslationTool {

	public static void main(String[] args) throws IOException {
		
		cleanConstants();
		cleanMessages();
		
	}


	private static void cleanConstants() throws IOException {
		Set<String> references = findReferences(UIConstants.class);
		cleanProperties(UIConstants.class, null, references);
		cleanProperties(UIConstants.class, "fr", references);
	}


	private static void cleanMessages() throws IOException {
		Set<String> references = findReferences(UIMessages.class);
		cleanProperties(loadTranslationsFromInterface(UIMessages.class), references);
		cleanProperties(UIMessages.class, "fr", references);
	}
	
	
	
	private static void cleanProperties(Properties defined, Set<String> referenced) {
		for(Object definedKey : defined.keySet()) {
			if(!referenced.contains(definedKey)) {
				System.out.println("Remove unsused key '" + definedKey + "' from UIMessages.java");
			}
		}
	}


	private static void cleanProperties(Class i18nClass, String locale, Set<String> references) throws IOException {
		String path = "src/main/java" + translationPath(i18nClass, locale);
		File file = new File(path);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		StringWriter writer = new StringWriter();
		
		String line;
		int removed = 0;
		while((line=reader.readLine())!=null) {
			String key = parseKey(line);
			if(key == null || references.contains(key)) {
				writer.write(line);
				writer.write("\n");
			} else {
				removed ++;
			}
		}
		reader.close();
	
		Files.write(writer.toString(), file, Charsets.UTF_8);
		
		System.out.println("Removed " + removed + " unused properties from " + file.getName());
	}

	private static String parseKey(String line) {
		if(line.trim().isEmpty()) {
			return null;
		}
		if(line.startsWith("#")) {
			return null;
		}
		int equalPos = line.indexOf('=');
		if(equalPos == -1) {
			return null;
		}
		String key = line.substring(0, equalPos).trim();
		return key;
	}

	public static Set<String> findReferences(Class i18nClass) throws IOException {
		Set<String> references = Sets.newHashSet();
		File root = new File("war/WEB-INF/classes");
		scanClasses(i18nClass, root, references);
		return references;
	}
	
	private static void scanClasses(Class i18nClass, File parent, Set<String> references) throws IOException {
		for(File file : parent.listFiles()) {
			if(file.getName().endsWith(".class")) {
				ClassReader reader = new ClassReader(new FileInputStream(file));
				reader.accept(new I18NRefFindingClassVisitor(i18nClass, references), 0);
			} else if(file.isDirectory()) {
				scanClasses(i18nClass, file, references);
			}
		}
	}
	
	private static class I18NRefFindingClassVisitor extends ClassVisitor implements Opcodes {

		private Class clazz;
		private Set<String> references;

		public I18NRefFindingClassVisitor(Class clazz, Set<String> references) {
			super(Opcodes.ASM4);
			this.clazz = clazz;
			this.references = references;
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc,
				String signature, String[] exceptions) {
			return new I18NRefFindingMethodVisitor(clazz, references);
		}
	}
	
	private static class I18NRefFindingMethodVisitor extends MethodVisitor {

		private String className;
		private Set<String> references;

		public I18NRefFindingMethodVisitor(Class clazz, Set<String> references) {
			super(Opcodes.ASM4);
			this.className = clazz.getName().replace('.', '/');
			this.references = references;
		}

		@Override
		public void visitMethodInsn(int opcode, String owner, String name,
				String desc) {
			if(className.equals(owner)) {
				references.add(name);
			}
		}
	
	}
	

	public static Properties loadTranslations(Class clazz, String locale) throws IOException {
		StringBuilder propertiesPath = translationPath(clazz, locale);
		InputStream in = clazz.getResourceAsStream(propertiesPath.toString());
		if(in == null) {
			throw new IOException("Could not find resource '" + propertiesPath.toString() + "'");
		}
		InputStreamReader reader = new InputStreamReader(in);
		Properties properties = new Properties();
		properties.load(reader);
		reader.close();
		return properties;
	}
	
	public static Properties loadTranslationsFromInterface(Class clazz) {
		Properties properties = new Properties();
		for(Method method : clazz.getMethods()) {
			DefaultMessage defaultMessage = method.getAnnotation(DefaultMessage.class);
			if(defaultMessage != null) {
				properties.setProperty(method.getName(), defaultMessage.value());
			}
		}
		return properties;
	}

	private static StringBuilder translationPath(Class clazz, String locale) {
		StringBuilder propertiesPath = new StringBuilder();
		propertiesPath.append('/').append(clazz.getName().replace('.', '/'));
		if(locale != null) {
			propertiesPath.append("_").append(locale);
		}
		propertiesPath.append(".properties");
		return propertiesPath;
	}
}
