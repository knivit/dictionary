package com.tsoft.gwt.development.compiler;

import com.tsoft.gwt.development.Utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.codehaus.plexus.compiler.CompilerConfiguration;
import org.codehaus.plexus.compiler.CompilerError;
import org.codehaus.plexus.compiler.CompilerException;
import org.codehaus.plexus.compiler.CompilerOutputStyle;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class JavaCompiler {
    private static final Logger logger = Logger.getLogger(JavaCompiler.class.getName());

    private static final String EOL = System.getProperty("line.separator");
    private static final String WARNING_PREFIX = "warning: ";

    private CompilerOutputStyle compilerOutputStyle;
    private String inputFileEnding;
    private String outputFileEnding;
    private String outputFile;

    public JavaCompiler() {
        this(CompilerOutputStyle.ONE_OUTPUT_FILE_PER_INPUT_FILE, ".java", ".class", null);
    }

    public JavaCompiler(CompilerOutputStyle compilerOutputStyle, String inputFileEnding, String outputFileEnding, String outputFile) {
        this.compilerOutputStyle = compilerOutputStyle;
        this.inputFileEnding = inputFileEnding;
        this.outputFileEnding = outputFileEnding;
        this.outputFile = outputFile;
    }

    public List compile(CompilerConfiguration config) throws CompilerException {
        File destinationDir = new File(config.getOutputLocation());
        if (!destinationDir.exists()) {
            destinationDir.mkdirs();
        }

        String[] sourceFiles = getSourceFiles(config);
        if ((sourceFiles == null) || (sourceFiles.length == 0)) {
            return Collections.EMPTY_LIST;
        }

        logger.info("Compiling " + sourceFiles.length +
                    " source file" + (sourceFiles.length == 1 ? "" : "s") +
                    " to " + destinationDir.getAbsolutePath());

        List messages;
        String[] args = buildCompilerArguments(config, sourceFiles);
        if (config.isFork()) {
            String executable = config.getExecutable();
            if (StringUtils.isEmpty(executable)) {
                try {
                    executable = Utils.getJdkToolExecutable("javac");
                } catch (IOException ex) {
                    logger.warning("Unable to autodetect 'javac' path, using 'javac' from the environment.");
                    executable = "javac";
                }
            }
            messages = compileOutOfProcess(config, executable, args);
        } else {
            messages = compileInProcess(args);
        }

        return messages;
    }

    public String[] createCommandLine(CompilerConfiguration config) throws CompilerException {
        return buildCompilerArguments(config, getSourceFiles(config));
    }

    private String[] buildCompilerArguments(CompilerConfiguration config, String[] sourceFiles) {
        List args = new ArrayList();

        File destinationDir = new File(config.getOutputLocation());
        args.add("-d");
        args.add(destinationDir.getAbsolutePath());

        List classpathEntries = config.getClasspathEntries();
        if ((classpathEntries != null) && !classpathEntries.isEmpty()) {
            args.add("-classpath");
            args.add(getPathString(classpathEntries));
        }

        List sourceLocations = config.getSourceLocations();
        if ((sourceLocations != null) && !sourceLocations.isEmpty()) {
            // always pass source path, even if sourceFiles are declared,
            // needed for jsr269 annotation processing, see MCOMPILER-98
            args.add("-sourcepath");
            args.add(getPathString(sourceLocations));
        }

        args.addAll(Arrays.asList(sourceFiles));

        if (config.isOptimize()) {
            args.add("-O");
        }

        if (config.isDebug()) {
            if (StringUtils.isNotEmpty(config.getDebugLevel())) {
                args.add("-g:" + config.getDebugLevel());
            } else {
                args.add("-g");
            }
        }

        if (config.isVerbose()) {
            args.add("-verbose");
        }

        if (config.isShowDeprecation()) {
            args.add("-deprecation");

            // This is required to actually display the deprecation messages
            config.setShowWarnings(true);
        }

        if (!config.isShowWarnings()) {
            args.add("-nowarn");
        }

        if (StringUtils.isEmpty(config.getTargetVersion())) {
            // Required, or it defaults to the target of your JDK (eg 1.5)
            args.add("-target");
            args.add("1.1");
        } else {
            args.add("-target");
            args.add(config.getTargetVersion());
        }

        if (!suppressSource(config) && StringUtils.isEmpty(config.getSourceVersion())) {
            // If omitted, later JDKs complain about a 1.1 target
            args.add("-source");
            args.add("1.3");
        } else if (!suppressSource(config)) {
            args.add("-source");
            args.add(config.getSourceVersion());
        }

        if (!suppressEncoding(config) && !StringUtils.isEmpty(config.getSourceEncoding())) {
            args.add("-encoding");
            args.add(config.getSourceEncoding());
        }

        for (Iterator it = config.getCustomCompilerArguments().entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            if (StringUtils.isEmpty(key)) {
                continue;
            }

            args.add(key);
            String value = (String) entry.getValue();
            if (StringUtils.isEmpty(value)) {
                continue;
            }

            args.add(value);
        }

        return (String[]) args.toArray(new String[args.size()]);
    }

    /**
     * Determine if the compiler is a version prior to 1.4.
     * This is needed as 1.3 and earlier did not support -source or -encoding parameters
     *
     * @param config The compiler configuration to test.
     * @return true if the compiler configuration represents a Java 1.4 compiler or later, false otherwise
     */
    private boolean isPreJava14(CompilerConfiguration config) {
        String v = config.getCompilerVersion();
        if (v == null) {
            return false;
        }

        return v.startsWith("1.3") || v.startsWith("1.2") || v.startsWith("1.1") || v.startsWith("1.0");
    }

    private boolean suppressSource(CompilerConfiguration config) {
        return isPreJava14(config);
    }

    private boolean suppressEncoding(CompilerConfiguration config) {
        return isPreJava14(config);
    }

    /**
     * Compile the java sources in a external process, calling an external executable, like javac.
     */
    private List compileOutOfProcess(CompilerConfiguration config, String executable, String[] args) throws CompilerException {
        Commandline cli = new Commandline();
        cli.setWorkingDirectory(config.getWorkingDirectory().getAbsolutePath());
        cli.setExecutable(executable);

        try {
            File argumentsFile = createFileWithArguments(args);
            cli.addArguments(new String[] {"@" + argumentsFile.getCanonicalPath().replace(File.separatorChar, '/') });

            if (!StringUtils.isEmpty(config.getMaxmem())) {
                cli.addArguments(new String[] { "-J-Xmx" + config.getMaxmem() });
            }

            if (!StringUtils.isEmpty(config.getMeminitial())) {
                cli.addArguments(new String[] { "-J-Xms" + config.getMeminitial() });
            }
        } catch (IOException e) {
            throw new CompilerException("Error creating file with javac arguments", e);
        }

        CommandLineUtils.StringStreamConsumer out = new CommandLineUtils.StringStreamConsumer();
        CommandLineUtils.StringStreamConsumer err = new CommandLineUtils.StringStreamConsumer();

        int returnCode;
        List messages;
        try {
            returnCode = CommandLineUtils.executeCommandLine(cli, out, err);
            messages = parseModernStream(new BufferedReader(new StringReader(err.getOutput())));
        } catch (CommandLineException e) {
            throw new CompilerException("Error while executing the external compiler.", e);
        } catch (IOException e) {
            throw new CompilerException("Error while executing the external compiler.", e);
        }

        if ((returnCode != 0) && messages.isEmpty()) {
            if (err.getOutput().length() == 0) {
                throw new CompilerException("Unknown error trying to execute the external compiler: " + EOL + cli.toString());
            } else {
                messages.add(new CompilerError("Failure executing javac,  but could not parse the error:" + EOL + err.getOutput(), true));
            }
        }

        return messages;
    }

    /**
     * Compile the java sources in the current JVM, without calling an external executable,
     * using <code>com.sun.tools.javac.Main</code> class
     */
    private List compileInProcess(String[] args) throws CompilerException {
        IsolatedClassLoader cl = new IsolatedClassLoader();
        File toolsJar = new File(System.getProperty("jdk.home"), "../lib/tools.jar");
        if (toolsJar.exists()) {
            try {
                cl.addURL(toolsJar.toURI().toURL());
            } catch (MalformedURLException e) {
                throw new CompilerException("Could not convert the file reference to tools.jar to a URL, path to tools.jar: '" + toolsJar.getAbsolutePath() + "'.");
            }
        }

        Class c;
        try {
            c = cl.loadClass("com.sun.tools.javac.Main");
        } catch (ClassNotFoundException e) {
            String message = "Unable to locate the Javac Compiler in:" + EOL + "  " + toolsJar.getAbsolutePath() + EOL
                    + "Please ensure you are using JDK 1.4 or above and" + EOL
                    + "not a JRE (the com.sun.tools.javac.Main class is required)." + EOL
                    + "In most cases you can change the location of your Java" + EOL
                    + "installation by setting the JAVA_HOME environment variable.";
            return Collections.singletonList(new CompilerError(message, true));
        }

        Integer resultCode;
        List messages;
        StringWriter out = new StringWriter();
        try {
            Method compile = c.getMethod("compile", new Class[] { String[].class, PrintWriter.class });
            resultCode = (Integer) compile.invoke(null, new Object[]{args, new PrintWriter(out)});
            messages = parseModernStream(new BufferedReader(new StringReader(out.toString())));
        } catch (NoSuchMethodException e) {
            throw new CompilerException("Error while executing the compiler.", e);
        } catch (IllegalAccessException e) {
            throw new CompilerException("Error while executing the compiler.", e);
        } catch (InvocationTargetException e) {
            throw new CompilerException("Error while executing the compiler.", e);
        } catch (IOException e) {
            throw new CompilerException("Error while executing the compiler.", e);
        }

        if ((resultCode.intValue() != 0) && messages.isEmpty()) {
            messages.add(new CompilerError("Failure executing javac, but could not parse the error:" + EOL + out.toString(), true));
        }

        return messages;
    }

    /**
     * Parse the output from the compiler into a list of CompilerError objects
     */
    private List parseModernStream(BufferedReader input) throws IOException {
        String line;
        StringBuffer buffer;
        List errors = new ArrayList();
        while (true) {
            // cleanup the buffer
            buffer = new StringBuffer(); // this is quicker than clearing it

            // most errors terminate with the '^' char
            do {
                line = input.readLine();
                if (line == null) {
                    return errors;
                }

                // TODO: there should be a better way to parse these
                if ((buffer.length() == 0) && line.startsWith("error: ")) {
                    errors.add(new CompilerError(line, true));
                } else if ((buffer.length() == 0) && line.startsWith("Note: ")) {
                    // skip this one - it is JDK 1.5 telling us that the interface is deprecated.
                } else {
                    buffer.append(line);
                    buffer.append(EOL);
                }
            } while (!line.endsWith("^"));

            // add the error bean
            errors.add(parseModernError(buffer.toString()));
        }
    }

    /**
     * Construct a CompilerError object from a line of the compiler output
     *
     * @param error output line from the compiler
     * @return the CompilerError object
     */
    public CompilerError parseModernError(String error) {
        StringTokenizer tokens = new StringTokenizer(error, ":");

        boolean isError = true;
        StringBuffer msgBuffer;
        try {
            // With Java 6 error output lines from the compiler got longer. For backward compatibility
            // .. and the time being, we eat up all (if any) tokens up to the erroneous file and source
            // .. line indicator tokens.
            boolean tokenIsAnInteger;
            String previousToken = null;
            String currentToken = null;
            do {
                previousToken = currentToken;
                currentToken = tokens.nextToken();

                // Probably the only backward compatible means of checking if a string is an integer.
                tokenIsAnInteger = true;
                try {
                    Integer.parseInt(currentToken);
                } catch (NumberFormatException e) {
                    tokenIsAnInteger = false;
                }
            } while (!tokenIsAnInteger);

            String file = previousToken;
            String lineIndicator = currentToken;
            int startOfFileName = previousToken.lastIndexOf("]");
            if (startOfFileName > -1) {
                file = file.substring(startOfFileName + 2);
            }

            // When will this happen?
            if (file.length() == 1) {
                file = new StringBuffer(file).append(":").append(tokens.nextToken()).toString();
            }

            int line = Integer.parseInt(lineIndicator);
            msgBuffer = new StringBuffer();
            String msg = tokens.nextToken(EOL).substring(2);
            isError = !msg.startsWith(WARNING_PREFIX);

            // Remove the 'warning: ' prefix
            if (!isError) {
                msg = msg.substring(WARNING_PREFIX.length());
            }

            msgBuffer.append(msg);
            msgBuffer.append(EOL);
            String context = tokens.nextToken(EOL);
            String pointer = tokens.nextToken(EOL);

            if (tokens.hasMoreTokens()) {
                msgBuffer.append(context);    // 'symbol' line
                msgBuffer.append(EOL);
                msgBuffer.append(pointer);    // 'location' line
                msgBuffer.append(EOL);
                context = tokens.nextToken(EOL);

                try {
                    pointer = tokens.nextToken(EOL);
                } catch (NoSuchElementException e) {
                    pointer = context;
                    context = null;
                }
            }

            String message = msgBuffer.toString();
            int startcolumn = pointer.indexOf("^");
            int endcolumn = context == null ? startcolumn : context.indexOf(" ", startcolumn);
            if (endcolumn == -1) {
                endcolumn = context.length();
            }

            return new CompilerError(file, isError, line, startcolumn, line, endcolumn, message);
        } catch (NoSuchElementException e) {
            return new CompilerError("no more tokens - could not parse error message: " + error, isError);
        } catch (NumberFormatException e) {
            return new CompilerError("could not parse error message: " + error, isError);
        } catch (Exception e) {
            return new CompilerError("could not parse error message: " + error, isError);
        }
    }

    /**
     * Put args into a temp file to be referenced using the @ option in javac command line
     */
    private File createFileWithArguments(String[] args) throws IOException {
        PrintWriter writer = null;
        try {
            File tempFile = File.createTempFile(JavaCompiler.class.getName(), "arguments");
            tempFile.deleteOnExit();
            writer = new PrintWriter(new FileWriter(tempFile));
            for (int i = 0; i < args.length; i++) {
                String argValue = args[i].replace(File.separatorChar, '/');
                writer.write("\"" + argValue + "\"");
                writer.println();
            }

            writer.flush();
            return tempFile;
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private String[] getSourceFiles(CompilerConfiguration config) {
        Set sources = new HashSet();
        Set sourceFiles = config.getSourceFiles();
        if (sourceFiles != null && !sourceFiles.isEmpty()) {
            for (Iterator it = sourceFiles.iterator(); it.hasNext();) {
                File sourceFile = (File) it.next();
                sources.add(sourceFile.getAbsolutePath());
            }
        } else {
            for (Iterator it = config.getSourceLocations().iterator(); it.hasNext();) {
                String sourceLocation = (String) it.next();
                sources.addAll(getSourceFilesForSourceRoot(config, sourceLocation));
            }
        }

        String[] result;
        if (sources.isEmpty()) {
            result = new String[0];
        } else {
            result = (String[]) sources.toArray(new String[sources.size()]);
        }

        return result;
    }

    private Set getSourceFilesForSourceRoot(CompilerConfiguration config, String sourceLocation) {
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(sourceLocation);
        
        Set includes = config.getIncludes();
        if (includes != null && !includes.isEmpty()) {
            String[] inclStrs = (String[]) includes.toArray(new String[includes.size()]);
            scanner.setIncludes(inclStrs);
        } else {
            scanner.setIncludes(new String[]{"**/*.java"});
        }

        Set excludes = config.getExcludes();
        if (excludes != null && !excludes.isEmpty()) {
            String[] exclStrs = (String[]) excludes.toArray(new String[excludes.size()]);
            scanner.setIncludes(exclStrs);
        }

        scanner.scan();
        String[] sourceDirectorySources = scanner.getIncludedFiles();

        Set sources = new HashSet();
        for (int j = 0; j < sourceDirectorySources.length; j++) {
            File f = new File(sourceLocation, sourceDirectorySources[j]);
            sources.add(f.getPath());
        }

        return sources;
    }

    private String getPathString(List pathElements) {
        StringBuffer sb = new StringBuffer();
        for (Iterator it = pathElements.iterator(); it.hasNext();) {
            sb.append(it.next()).append(File.pathSeparator);
        }
        return sb.toString();
    }

    public CompilerOutputStyle getCompilerOutputStyle() {
        return compilerOutputStyle;
    }

    public String getInputFileEnding(CompilerConfiguration configuration) throws CompilerException {
        return inputFileEnding;
    }

    public String getOutputFileEnding(CompilerConfiguration configuration) throws CompilerException {
        if (compilerOutputStyle != CompilerOutputStyle.ONE_OUTPUT_FILE_PER_INPUT_FILE) {
            throw new RuntimeException("This compiler implementation doesn't have one output file per input file.");
        }

        return outputFileEnding;
    }

    public String getOutputFile(CompilerConfiguration configuration) throws CompilerException {
        if (compilerOutputStyle != CompilerOutputStyle.ONE_OUTPUT_FILE_FOR_ALL_INPUT_FILES) {
            throw new RuntimeException("This compiler implementation doesn't have one output file for all files.");
        }

        return outputFile;
    }

    public boolean canUpdateTarget(CompilerConfiguration configuration) throws CompilerException {
        return true;
    }
}
