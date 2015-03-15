package com.tsoft.tool.log;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;
import java.io.File;
import org.codehaus.plexus.util.DirectoryScanner;

public class LoggerDoc {
    public static void main(String[] args) {
        String sourceRoot = args[0];
        String[] fileNamePatterns = new String[] { "*.java" };

        JavaDocBuilder builder = new JavaDocBuilder();
        builder.addSourceTree(new File(sourceRoot));

        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(sourceRoot);
        scanner.setIncludes(fileNamePatterns);
        scanner.scan();

        String[] sourceFileNames = scanner.getIncludedFiles();
        for (String sourceFileName : sourceFileNames) {
            processFile(sourceRoot, sourceFileName, builder);
        }
    }

    private static void processFile(String sourceRoot, String sourceFileName, JavaDocBuilder builder) {
        String className = getTopLevelClassName(sourceFileName);
        JavaClass clazz = builder.getClassByName(className);
        JavaField[] fields = clazz.getFields();

        String loggerFieldName = null;
        for (JavaField field : fields) {
            if (field.getType().getValue().equals("java.util.logging.Logger")) {
                loggerFieldName = field.getName();
            }
        }
        if (loggerFieldName == null) {
            return;
        }

        String loggerUseOperation = loggerFieldName + '.';
        JavaMethod[] methods = clazz.getMethods();
        for (JavaMethod method : methods) {
            String code = method.getSourceCode();
            int pos = code.indexOf(loggerUseOperation);
            if (pos != -1) {
                System.out.println(clazz.getName() + ":" + method.getName());
            }
        }
    }

    private static String getTopLevelClassName(String sourceFile) {
        String className = sourceFile.substring(0, sourceFile.length() - 5); // strip ".java"
        return className.replace(File.separatorChar, '.');
    }
}
