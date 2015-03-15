package com.tsoft.gwt.development.gwtclient;

import java.io.File;
import java.util.ArrayList;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.Annotation;
import com.thoughtworks.qdox.model.JavaClass;
import com.tsoft.gwt.development.Utils;
import java.util.Arrays;
import java.util.logging.Logger;

import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class GWTClientHelper {
    private static final Logger logger = Logger.getLogger(GWTClientHelper.class.getName());

    private static final String GWT_XML_FILE_EXT = ".gwt.xml";

    public void process(GWTClientConfiguration clientConfiguration) throws Exception {
        // clean target folder
        File targetRootFile = new File(clientConfiguration.getTargetRoot());
        targetRootFile.mkdirs();
        FileUtils.cleanDirectory(targetRootFile);

        // create Javadoc builder
        JavaDocBuilder builder = createJavaDocBuilder(clientConfiguration.getSourceRoot());

        // generate GWT Async-interfaces
        AsyncInterfaceGenerator generator = new AsyncInterfaceGenerator();
        generator.setFileNamePatterns(clientConfiguration.getInterfaceFilePatterns());
        generator.setSourceRoot(clientConfiguration.getSourceRoot());
        generator.setTargetRoot(clientConfiguration.getTargetRoot());
        generator.execute(builder);

        // copy java sources for GWT
        File sourceRootFile = new File(clientConfiguration.getSourceRoot());
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(sourceRootFile);
        scanner.setIncludes(clientConfiguration.getGwtFilePatterns());
        scanner.scan();

        String[] sourceFileNames = scanner.getIncludedFiles();
        for (String sourceFileName : sourceFileNames) {
            processGWTClient(sourceRootFile, sourceFileName, targetRootFile, builder, clientConfiguration.getGwtClientAnnotation());
        }

        // copy marker annotation itself
        String gwtClientAnnotationFileName = clientConfiguration.getGwtClientAnnotation().replace('.', File.separatorChar) + ".java";
        File gwtClientAnnotationFile = new File(clientConfiguration.getSourceRoot(), gwtClientAnnotationFileName);
        copyFile(gwtClientAnnotationFile, gwtClientAnnotationFileName, targetRootFile);

        // compile sources
        GWTClientCompiler compiler = new GWTClientCompiler();
        ArrayList compileSourceRoots = new ArrayList();
        compileSourceRoots.add(clientConfiguration.getTargetRoot());
        compiler.setCompileSourceRoots(compileSourceRoots);
        compiler.setOutputDirectory(targetRootFile);
        String classPath = System.getProperty("java.class.path");
        compiler.setClasspathElements(Arrays.asList(new String[] { classPath }));
        compiler.setSource("1.6");
        compiler.setTarget("1.6");
        compiler.setCompilerArgument("-proc:none"); // to prevent  a beforehand starting of Datanucleus Enhancer
        compiler.execute();

        // copy GWT xml file
        String gwtXmlFileName = clientConfiguration.getSourceGwtFileName().replace('.', File.separatorChar) + GWT_XML_FILE_EXT;
        File gwtXmlFile = new File(clientConfiguration.getSourceGwtRoot(), gwtXmlFileName);
        copyFile(gwtXmlFile, gwtXmlFileName, targetRootFile);

        // create a client jar file
        Commandline cli = new Commandline();
        cli.setWorkingDirectory(clientConfiguration.getWorkingDirectory());
        cli.setExecutable(Utils.getJdkToolExecutable("jar"));
        cli.addArguments(new String[] { "cvf", clientConfiguration.getOutputJarFileName(), "-C", clientConfiguration.getGeneratorWorkingFolder(), "." });
        CommandLineUtils.StringStreamConsumer out = new CommandLineUtils.StringStreamConsumer();
        CommandLineUtils.StringStreamConsumer err = new CommandLineUtils.StringStreamConsumer();
        logger.info("Create the jar file: " + cli.toString());
        CommandLineUtils.executeCommandLine(cli, out, err);
    }

    private JavaDocBuilder createJavaDocBuilder(String sourceRoot) throws Exception {
        JavaDocBuilder builder = new JavaDocBuilder();
        builder.addSourceTree(new File(sourceRoot));
        return builder;
    }

    private void processGWTClient(File sourceRoot, String sourceFileName, File targetRoot, JavaDocBuilder builder, String gwtClientAnnotation) throws Exception {
        File sourceFile = new File(sourceRoot, sourceFileName);

        String className = AsyncInterfaceGenerator.getTopLevelClassName(sourceFileName);
        JavaClass clazz = builder.getClassByName(className);
        if (isEligibleForCopy(clazz, gwtClientAnnotation)) {
            copyFile(sourceFile, sourceFileName, targetRoot);
        }
    }

    private boolean isEligibleForCopy(JavaClass javaClass, String gwtClientAnnotationClassName) {
        Annotation[] annotations = javaClass.getAnnotations();
        if (annotations == null) {
            return false;
        }

        for (Annotation annotation : annotations) {
            if (gwtClientAnnotationClassName.equals(annotation.getType().getFullQualifiedName())) {
                return true;
            }
        }
        return false;
    }

    private File getTargetGWTFile(File targetRoot, String sourceFileName) {
        String targetFileName = sourceFileName;
        File targetFile = new File(targetRoot, targetFileName);
        return targetFile;
    }

    private void copyFile(File sourceFile, String sourceFileName, File targetRoot) throws Exception {
        File targetFile = getTargetGWTFile(targetRoot, sourceFileName);
        targetFile.getParentFile().mkdirs();

        FileUtils.copyFile(sourceFile, targetFile);
    }
}
