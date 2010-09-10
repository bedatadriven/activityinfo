/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */
package org.sigmah.client.mock;

import com.bedatadriven.rebar.persistence.mapping.client.BindEntities;
import com.bedatadriven.rebar.persistence.client.PersistenceUnit;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.PropertyOracle;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.linker.Artifact;
import com.google.gwt.core.ext.linker.GeneratedResource;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.dev.resource.ResourceOracle;
import com.bedatadriven.rebar.persistence.mapping.UnitMapping;
import com.bedatadriven.rebar.persistence.mapping.MappingException;
import com.bedatadriven.rebar.persistence.rebind.PersistenceUnitGenerator;

import javax.tools.*;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.*;
import java.util.*;
import java.net.URLClassLoader;
import java.net.URL;

/**
 * Compiles and instantiates a PersistenceUnit that can be run completely on the
 * JRE for testing purposes.
 *
 * @author Alex Bertram
 */
public class MockPersistenceUnitFactory {

  List<JavaFileObject> compilationUnits = new ArrayList<JavaFileObject>();

  private class MockLogger extends TreeLogger {
    @Override
    public TreeLogger branch(Type type, String msg, Throwable caught, HelpInfo helpInfo) {
      log(type, msg, caught, helpInfo);
      return this;
    }

    @Override
    public boolean isLoggable(Type type) {
      return true;
    }

    @Override
    public void log(Type type, String msg, Throwable caught, HelpInfo helpInfo) {
      System.out.println("[" + type.toString() + "] " + msg);
      if (caught != null) {
        caught.printStackTrace();
      }
    }
  }

  private class JavaSource extends SimpleJavaFileObject {
    private File file;
    private final String className;


    public JavaSource(File file, String className) {
      super(file.toURI(), JavaFileObject.Kind.SOURCE);
      this.file = file;
      this.className = className;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
      try {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuffer code = new StringBuffer();
        while (reader.ready()) {
          code.append(reader.readLine()).append("\n");
        }
        return code;
      } catch (Exception e) {
        throw new AssertionError(e);
      }
    }

    @Override
    public String toString() {
      return className;
    }
  }

  private class MockGeneratorContext implements GeneratorContext {
    private File currentSourceFile;
    private FileOutputStream currentFos;
    private String currentClass;

    @Override
    public PrintWriter tryCreate(TreeLogger logger, String packageName, String simpleName) {

      currentClass = simpleName;

      File packageDir = new File("target/test-em-compile/" + packageName.replace(".", "/"));
      packageDir.mkdirs();

      try {
        currentSourceFile = new File(packageDir.getAbsolutePath() + "/" + simpleName + ".java");

        currentFos = new FileOutputStream(
            currentSourceFile);

        return new PrintWriter(currentFos);

      } catch (FileNotFoundException e) {
        throw new AssertionError(e);
      }

    }

    @Override
    public void commit(TreeLogger logger, PrintWriter pw) {

      try {
        currentFos.close();
        compilationUnits.add(new JavaSource(currentSourceFile, currentClass));


      } catch (IOException e) {
        throw new AssertionError(e);
      }
    }

    @Override
    public void commitArtifact(TreeLogger logger, Artifact<?> artifact) throws UnableToCompleteException {
      throw new Error("not impl");
    }

    @Override
    public GeneratedResource commitResource(TreeLogger logger, OutputStream os) throws UnableToCompleteException {
      throw new Error("not impl");
    }

    @Override
    public PropertyOracle getPropertyOracle() {
      throw new Error("not impl");
    }

    @Override
    public ResourceOracle getResourcesOracle() {
      throw new Error("not impl");
    }

    @Override
    public TypeOracle getTypeOracle() {
      throw new Error("not impl");
    }

    @Override
    public OutputStream tryCreateResource(TreeLogger logger, String partialPath) throws UnableToCompleteException {
      throw new Error("not impl");
    }
  }

  public <T extends PersistenceUnit> T create(Class<T> contextClass) {


    try {
      JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
      MockLogger logger = new MockLogger();

      UnitMapping mapping = new UnitMapping(contextClass);
      BindEntities boundEntities = (BindEntities) contextClass.getAnnotation(BindEntities.class);
      if(boundEntities != null) {
        for (Class boundEntity : boundEntities.value()) {
          if((boundEntity.getAnnotation(Entity.class)==null) && (boundEntity.getAnnotation(Embeddable.class)==null))
            throw new MappingException(boundEntity.getName() + " is not annotated with @Entity and  " +
                "so cannot be bound to the persistence unit " + contextClass.getName());
          logger.log(TreeLogger.Type.DEBUG, "Binding " + boundEntity.getName());
          mapping.getMapping(boundEntity);
        }
      }

      PersistenceUnitGenerator gtor = new PersistenceUnitGenerator();
      gtor.writeClasses(logger, new MockGeneratorContext(), mapping);

      DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

      StandardJavaFileManager jfm = compiler.getStandardFileManager(diagnostics, null, null);
      File outputDir = new File("target/test-classes");
      outputDir.mkdirs();

      jfm.setLocation(StandardLocation.CLASS_OUTPUT, Collections.singleton(outputDir));

      JavaCompiler.CompilationTask task = compiler.getTask(null, jfm, diagnostics, null, null, compilationUnits);

      boolean success = task.call();

      jfm.close();


      for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
        System.out.println("[" + diagnostic.getKind() + "] " +
            cleanupMessage(diagnostic.getMessage(null)));
      }

      if (!success) {
        throw new AssertionError("Compiliation failed");
      }

      URLClassLoader loader = new URLClassLoader(new URL[] { outputDir.toURI().toURL() } );

      Class<T> context = (Class<T>) loader.loadClass(mapping.getPersistenceContextImplQualifiedClass());
      return context.newInstance();

    } catch (Exception e) {
    
      throw new RuntimeException(e);
    }
  }

  private String cleanupMessage(String message) {
    int colon = message.indexOf(':', 5);
    if (colon == -1)
      return message;
    int pathStart = message.substring(0, colon).lastIndexOf('/');
    return message.substring(pathStart + 1);

  }


}
