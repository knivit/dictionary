package com.tsoft.gwt.development.gwtclient;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.plexus.util.DirectoryScanner;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.Annotation;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;
import com.thoughtworks.qdox.model.JavaSource;
import org.codehaus.plexus.util.FileUtils;

/**
 * Generate Async interfaces for a GWT applicaton
 */
public class AsyncInterfaceGenerator {
    private static final String REMOTE_SERVICE_INTERFACE = "com.google.gwt.user.client.rpc.RemoteService";
    private static final String ASYNC_CALLBACK_INTERFACE = "import com.google.gwt.user.client.rpc.AsyncCallback";
    private static final String REMOTE_SERVICE_RELATIVE_PATH_ANNOTATION = "com.google.gwt.user.client.rpc.RemoteServiceRelativePath";

    private final static Map<String, String> WRAPPERS = new HashMap<String, String>();
    static {
        WRAPPERS.put("boolean", Boolean.class.getName());
        WRAPPERS.put("byte", Byte.class.getName());
        WRAPPERS.put("char", Character.class.getName());
        WRAPPERS.put("short", Short.class.getName());
        WRAPPERS.put("int", Integer.class.getName());
        WRAPPERS.put("long", Long.class.getName());
        WRAPPERS.put("float", Float.class.getName());
        WRAPPERS.put("double", Double.class.getName());
    }

    /**
     * A (MessageFormat) Pattern to get the GWT-RPC servlet URL based on service interface name. For example to
     * "{0}.rpc" if you want to map GWT-RPC calls to "*.rpc" in web.xml, for example when using Spring dispatch servlet
     * to handle RPC requests.
     * @parameter default-value="{0}" expression="${gwt.rpcPattern}"
     */
    private String rpcPattern = "{0}";

    private String[] fileNamePatterns;

    private String sourceRoot;
    private String targetRoot;

    public void execute(JavaDocBuilder builder) throws Exception {
        File targetRootFile = new File(targetRoot);
        scan(new File(sourceRoot), targetRootFile, builder);
    }

    /**
     * @param sourceRoot the base directory to scan for RPC services
     * @return true if some file have been generated
     * @throws Exception generation failure
     */
    private void scan(File sourceRoot, File targetRoot, JavaDocBuilder builder) throws Exception {
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(sourceRoot);
        scanner.setIncludes(fileNamePatterns);
        scanner.scan();

        String[] sourceFileNames = scanner.getIncludedFiles();
        for (String sourceFileName : sourceFileNames) {
            processAsync(sourceRoot, sourceFileName, targetRoot, builder);
        }
    }

    private void processAsync(File sourceRoot, String sourceFileName, File targetRoot, JavaDocBuilder builder) throws Exception {
        File sourceFile = new File(sourceRoot, sourceFileName);

        String className = getTopLevelClassName(sourceFileName);
        JavaClass clazz = builder.getClassByName(className);
        if (isEligibleForGeneration(clazz)) {
            File targetAsyncFile = getTargetAsyncFile(targetRoot, sourceFileName);
            targetAsyncFile.getParentFile().mkdirs();
            generateAsync(clazz, targetAsyncFile);

            File targetFile = new File(targetRoot, sourceFileName);
            FileUtils.copyFile(sourceFile, targetFile);
        }
    }

    private File getTargetAsyncFile(File targetRoot, String sourceFileName) {
        String targetFileName = sourceFileName.substring(0, sourceFileName.length() - 5) + "Async.java";
        File targetFile = new File(targetRoot, targetFileName);
        return targetFile;
    }

    private void generateAsync(JavaClass clazz, File targetFile) throws IOException {
        PrintWriter writer = new PrintWriter(targetFile);

        JavaSource javaSource = clazz.getSource();
        writer.println("package " + javaSource.getPackage().getName() + ";");

        String[] imports = javaSource.getImports();
        for (String string : imports) {
            if (!REMOTE_SERVICE_INTERFACE.equals(string)) {
                writer.println("import " + string + ";");
            }
        }
        writer.println(ASYNC_CALLBACK_INTERFACE + ";");
        writer.println("public interface " + clazz.getName() + "Async {");

        JavaMethod[] methods = clazz.getMethods(false);
        for (JavaMethod method : methods) {
            writer.print("    public void " + method.getName() + "(");
            JavaParameter[] params = method.getParameters();
            for (int j = 0; j < params.length; j++) {
                JavaParameter param = params[j];
                if (j > 0) {
                    writer.print(", ");
                }
                writer.print(fixClassName(param.getType().getGenericValue()));
                if (param.getType().isArray()) {
                    writer.print("[]");
                }
                writer.print(" " + param.getName());
            }
            if (params.length > 0) {
                writer.print(", ");
            }

            if (method.getReturns().isVoid()) {
                writer.println("AsyncCallback<Void> callback);");
            } else if (method.getReturns().isPrimitive()) {
                String primitive = method.getReturns().getGenericValue();
                writer.println("AsyncCallback<" + WRAPPERS.get(primitive) + "> callback);");
            } else {
                String type = fixClassName(method.getReturns().getGenericValue());
                if (method.getReturns().isArray()) {
                    type += "[]";
                }
                writer.println("AsyncCallback<" + type + "> callback);");
            }
        }

        String uri = MessageFormat.format(rpcPattern, clazz.getName());
        if (clazz.getAnnotations() != null) {
            for (Annotation annotation : clazz.getAnnotations()) {
                if (annotation.getType().getValue().equals(REMOTE_SERVICE_RELATIVE_PATH_ANNOTATION)) {
                    uri = annotation.getNamedParameter("value").toString();
                    // remove quotes
                    uri = uri.substring(1, uri.length() - 1);
                }
            }
        }

        writer.println("}");
        writer.close();
    }

    private boolean isEligibleForGeneration(JavaClass javaClass) {
        return javaClass.isInterface() && javaClass.isPublic() && javaClass.isA(REMOTE_SERVICE_INTERFACE);
    }

    public static String getTopLevelClassName(String sourceFile) {
        String className = sourceFile.substring(0, sourceFile.length() - 5); // strip ".java"
        return className.replace(File.separatorChar, '.');
    }

    private String fixClassName(String original) {
        return original.replaceAll("\\$", ".");
    }

    public void setSourceRoot(String sourceRoot) {
        this.sourceRoot = sourceRoot;
    }

    public void setTargetRoot(String targetRoot) {
        this.targetRoot = targetRoot;
    }

    public void setFileNamePatterns(String[] fileNamePatterns) {
        this.fileNamePatterns = fileNamePatterns;
    }
}
