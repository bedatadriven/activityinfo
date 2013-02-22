package org.activityinfo;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.google.common.collect.Sets;

public class ClassRefFinder {

	private Set<String> references = Sets.newHashSet();
	private Class i18nClass;

	public ClassRefFinder(Class i18nClass) throws IOException {
		this.i18nClass = i18nClass;
	}
	
	public Set<String> scan(File root) throws IOException {
		scanClasses(root);
		return references;
	}
	
	private void scanClasses(File parent) throws IOException {
		for(File file : parent.listFiles()) {
			if(file.getName().endsWith(".class")) {
				ClassReader reader = new ClassReader(new FileInputStream(file));
				reader.accept(new I18NRefFindingClassVisitor(), 0);
			} else if(file.isDirectory()) {
				scanClasses(file);
			}
		}
	}
	
	private class I18NRefFindingClassVisitor extends ClassVisitor implements Opcodes {

		public I18NRefFindingClassVisitor() {
			super(Opcodes.ASM4);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc,
				String signature, String[] exceptions) {
			return new I18NRefFindingMethodVisitor();
		}
	}
	
	private class I18NRefFindingMethodVisitor extends MethodVisitor {


		public I18NRefFindingMethodVisitor() {
			super(Opcodes.ASM4);
		
		}

		@Override
		public void visitMethodInsn(int opcode, String owner, String name,
				String desc) {
			if(i18nClass.getName().replace('.', '/').equals(owner)) {
				references.add(name);
			}
		}
	
	}
}
