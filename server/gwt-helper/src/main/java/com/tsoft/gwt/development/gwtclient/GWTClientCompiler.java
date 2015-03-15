package com.tsoft.gwt.development.gwtclient;

import com.tsoft.gwt.development.compiler.JavaCompiler;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.codehaus.plexus.compiler.CompilerConfiguration;
import org.codehaus.plexus.compiler.CompilerError;
import org.codehaus.plexus.compiler.CompilerException;
import org.codehaus.plexus.compiler.CompilerOutputStyle;
import org.codehaus.plexus.compiler.util.scan.InclusionScanException;
import org.codehaus.plexus.compiler.util.scan.mapping.SingleTargetSourceMapping;
import org.codehaus.plexus.compiler.util.scan.mapping.SourceMapping;
import org.codehaus.plexus.compiler.util.scan.mapping.SuffixMapping;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.compiler.util.scan.SimpleSourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.SourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.StaleSourceScanner;

public class GWTClientCompiler {
    private static final Logger logger = Logger.getLogger(GWTClientCompiler.class.getName());

    // Indicates whether the build will continue even if there are compilation errors; defaults to true.
    private boolean failOnError = true;

    // Set to true to include debugging information in the compiled class files.
    private boolean debug = true;

    // Set to true to show messages about what the compiler is doing.
    private boolean verbose;

    // Sets whether to show source locations where deprecated APIs are used.
    private boolean showDeprecation;

    // Set to true to optimize the compiled code using the compiler's optimization methods.
    private boolean optimize;

    // Set to true to show compilation warnings.
    private boolean showWarnings;

    // The -source argument for the Java compiler.
    protected String source;

    // The -target argument for the Java compiler.
    protected String target;

    // The -encoding argument for the Java compiler.
    private String encoding;

    // Sets the granularity in milliseconds of the last modification
    private int staleMillis;

    // Version of the compiler to use, ex. "1.3", "1.5", if fork is set to true.
    private String compilerVersion;

    // Allows running the compiler in a separate process.
    private boolean fork;

    // Initial size, in megabytes, of the memory allocation pool, ex. "64", "64m"
    // if fork is set to true.
    private String meminitial;

    // Sets the maximum size, in megabytes, of the memory allocation pool, ex. "128", "128m"
    // if fork is set to true.
    private String maxmem;

    // Sets the executable of the compiler to use when fork is true.
    private String executable;

    // Sets the arguments to be passed to the compiler (prepending a dash) if fork is set to true.
    // This is because the list of valid arguments passed to a Java compiler varies based on the compiler version.
    protected Map compilerArguments;

    // Sets the unformatted argument string to be passed to the compiler if fork is set to true.
    // This is because the list of valid arguments passed to a Java compiler varies based on the compiler version.
    protected String compilerArgument;

    // Sets the name of the output file when compiling a set of sources to a single file.
    private String outputFileName;

    // The directory to run the compiler from if fork is true.
    private File basedir;

    // The target directory of the compiler if fork is true.
    private File buildDirectory;

    // A list of inclusion filters for the compiler.
    private Set includes = new HashSet();

    // A list of exclusion filters for the compiler.
    private Set excludes = new HashSet();

    private List classpathElements  = new ArrayList();

    private File outputDirectory;

    private List compileSourceRoots = new ArrayList();

    public void execute() throws CompilerException {
        JavaCompiler compiler = new JavaCompiler();

        CompilerConfiguration compilerConfiguration = prepareConfiguration();
        if (compilerConfiguration == null) {
            return;
        }

        if (!prepareSourceFiles(compiler, compilerConfiguration)) {
            return;
        }

        logConfiguration(compiler, compilerConfiguration);

        if (StringUtils.isEmpty(compilerConfiguration.getSourceEncoding())) {
            logger.info("File encoding has not been set, using platform encoding " +
                ReaderFactory.FILE_ENCODING + ", i.e. build is platform dependent!");
        }

        List messages = compiler.compile(compilerConfiguration);
        boolean compilationError = isMessagesContainsError(messages);

        if (compilationError && failOnError) {
            logger.info("-------------------------------------------------------------");
            logger.severe("COMPILATION ERROR : ");
            logger.info("-------------------------------------------------------------");
            for (Iterator i = messages.iterator(); i.hasNext();) {
                CompilerError message = (CompilerError) i.next();
                logger.severe(message.toString());
            }
            logger.info(messages.size() + ((messages.size() > 1) ? " errors " : "error"));
            logger.info("-------------------------------------------------------------");

            throw new CompilerException("Compilation Error");
        } else {
            for (Iterator i = messages.iterator(); i.hasNext();) {
                CompilerError message = (CompilerError) i.next();
                logger.warning(message.toString());
            }
        }
    }
    
    private boolean isMessagesContainsError(List messages) {
        if (messages == null) {
            return false;
        }
        
        for (Iterator i = messages.iterator(); i.hasNext();) {
            CompilerError message = (CompilerError) i.next();

            if (message.isError()) {
                return true;
            }
        }
        return false;
    }

    private CompilerConfiguration prepareConfiguration() {
        List compileSourceRoots = removeEmptyCompileSourceRoots(getCompileSourceRoots());
        if (compileSourceRoots.isEmpty()) {
            logger.info("No sources to compile");
            return null;
        }

        logger.info("Source directories: " + compileSourceRoots.toString().replace(',', '\n'));
        logger.info("Classpath: " + getClasspathElements().toString().replace(',', '\n'));
        logger.info("Output directory: " + getOutputDirectory());

        CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
        compilerConfiguration.setOutputLocation(getOutputDirectory().getAbsolutePath());
        compilerConfiguration.setClasspathEntries(getClasspathElements());
        compilerConfiguration.setSourceLocations(compileSourceRoots);
        compilerConfiguration.setOptimize(optimize);
        compilerConfiguration.setDebug(debug);
        compilerConfiguration.setVerbose(verbose);
        compilerConfiguration.setShowWarnings(showWarnings);
        compilerConfiguration.setShowDeprecation(showDeprecation);
        compilerConfiguration.setSourceVersion(getSource());
        compilerConfiguration.setTargetVersion(getTarget());
        compilerConfiguration.setSourceEncoding(encoding);

        Map effectiveCompilerArguments = getCompilerArguments();
        String effectiveCompilerArgument = getCompilerArgument();
        if ((effectiveCompilerArguments != null) || (effectiveCompilerArgument != null)) {
            LinkedHashMap cplrArgsCopy = new LinkedHashMap();
            if (effectiveCompilerArguments != null) {
                for (Iterator i = effectiveCompilerArguments.entrySet().iterator(); i.hasNext();) {
                    Map.Entry me = (Map.Entry) i.next();
                    String key = (String) me.getKey();
                    String value = (String) me.getValue();
                    if (!key.startsWith("-")) {
                        key = "-" + key;
                    }
                    cplrArgsCopy.put(key, value);
                }
            }
            if (!StringUtils.isEmpty(effectiveCompilerArgument)) {
                cplrArgsCopy.put(effectiveCompilerArgument, null);
            }
            compilerConfiguration.setCustomCompilerArguments(cplrArgsCopy);
        }

        compilerConfiguration.setFork(fork);
        if (fork) {
            if (!StringUtils.isEmpty(meminitial)) {
                String value = getMemoryValue(meminitial);
                if (value != null) {
                    compilerConfiguration.setMeminitial(value);
                } else {
                    logger.info("Invalid value for meminitial '" + meminitial + "'. Ignoring this option.");
                }
            }

            if (!StringUtils.isEmpty(maxmem)) {
                String value = getMemoryValue(maxmem);
                if (value != null) {
                    compilerConfiguration.setMaxmem(value);
                } else {
                    logger.info("Invalid value for maxmem '" + maxmem + "'. Ignoring this option.");
                }
            }
        }

        compilerConfiguration.setExecutable(executable);
        compilerConfiguration.setWorkingDirectory(basedir);
        compilerConfiguration.setCompilerVersion(compilerVersion);
        compilerConfiguration.setBuildDirectory(buildDirectory);
        compilerConfiguration.setOutputFileName(outputFileName);

        return compilerConfiguration;
    }

    private boolean prepareSourceFiles(JavaCompiler compiler, CompilerConfiguration compilerConfiguration) throws CompilerException {
        Set staleSources;
        boolean canUpdateTarget;
        staleSources = computeStaleSources(compilerConfiguration, compiler, getSourceInclusionScanner(staleMillis));
        canUpdateTarget = compiler.canUpdateTarget(compilerConfiguration);
        if (compiler.getCompilerOutputStyle().equals(CompilerOutputStyle.ONE_OUTPUT_FILE_FOR_ALL_INPUT_FILES) && !canUpdateTarget) {
            String inputFileEnding = compiler.getInputFileEnding(compilerConfiguration);
            Set sources = computeStaleSources(compilerConfiguration, compiler, getSourceInclusionScanner(inputFileEnding));

            compilerConfiguration.setSourceFiles(sources);
        } else {
            compilerConfiguration.setSourceFiles(staleSources);
        }

        if (staleSources.isEmpty()) {
            logger.info("Nothing to compile - all classes are up to date");
            return false;
        }

        return true;
    }

    private void logConfiguration(JavaCompiler compiler, CompilerConfiguration compilerConfiguration) throws CompilerException {
        logger.fine("Classpath:");
        for (Iterator it = getClasspathElements().iterator(); it.hasNext();) {
            String s = (String) it.next();
            logger.fine(" " + s);
        }

        logger.fine("Source roots:");
        for (Iterator it = getCompileSourceRoots().iterator(); it.hasNext();) {
            String root = (String) it.next();
            logger.fine(" " + root);
        }

        if (fork) {
            if (compilerConfiguration.getExecutable() != null) {
                logger.fine("Excutable: ");
                logger.fine(" " + compilerConfiguration.getExecutable());
            }

            String[] cl = compiler.createCommandLine(compilerConfiguration);
            if (cl != null && cl.length > 0) {
                StringBuffer sb = new StringBuffer();
                sb.append(cl[0]);
                for (int i = 1; i < cl.length; i++) {
                    sb.append(" ");
                    sb.append(cl[i]);
                }

                logger.fine("Command line options:");
                logger.fine(sb.toString());
            }
        }
    }

    private String getMemoryValue(String setting) {
        String value = null;

        // Allow '128' or '128m'
        if (isDigits(setting)) {
            value = setting + "m";
        } else {
            if ((isDigits(setting.substring(0, setting.length() - 1)))
                    && (setting.toLowerCase().endsWith("m"))) {
                value = setting;
            }
        }
        return value;
    }

    private boolean isDigits(String string) {
        for (int i = 0; i < string.length(); i++) {
            if (!Character.isDigit(string.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private Set computeStaleSources(CompilerConfiguration compilerConfiguration, JavaCompiler compiler, SourceInclusionScanner scanner) throws CompilerException {
        CompilerOutputStyle outputStyle = compiler.getCompilerOutputStyle();

        SourceMapping mapping;
        File outputDirectory;
        if (outputStyle == CompilerOutputStyle.ONE_OUTPUT_FILE_PER_INPUT_FILE) {
            mapping = new SuffixMapping(compiler.getInputFileEnding(compilerConfiguration), compiler.getOutputFileEnding(compilerConfiguration));
            outputDirectory = getOutputDirectory();
        } else if (outputStyle == CompilerOutputStyle.ONE_OUTPUT_FILE_FOR_ALL_INPUT_FILES) {
            mapping = new SingleTargetSourceMapping(compiler.getInputFileEnding(compilerConfiguration), compiler.getOutputFile(compilerConfiguration));
            outputDirectory = buildDirectory;
        } else {
            throw new RuntimeException("Unknown compiler output style: '" + outputStyle + "'.");
        }
        scanner.addSourceMapping(mapping);

        Set staleSources = new HashSet();
        for (Iterator it = getCompileSourceRoots().iterator(); it.hasNext();) {
            String sourceRoot = (String) it.next();

            File rootFile = new File(sourceRoot);

            if (!rootFile.isDirectory()) {
                continue;
            }

            try {
                staleSources.addAll(scanner.getIncludedSources(rootFile, outputDirectory));
            } catch (InclusionScanException e) {
                throw new RuntimeException("Error scanning source root: \'" + sourceRoot + "\' " + "for stale files to recompile.", e);
            }
        }

        return staleSources;
    }

    private static List removeEmptyCompileSourceRoots(List compileSourceRootsList) {
        List newCompileSourceRootsList = new ArrayList();
        if (compileSourceRootsList != null) {
            // copy as I may be modifying it
            for (Iterator i = compileSourceRootsList.iterator(); i.hasNext();) {
                String srcDir = (String) i.next();
                if (!newCompileSourceRootsList.contains(srcDir) && new File(srcDir).exists()) {
                    newCompileSourceRootsList.add(srcDir);
                }
            }
        }
        return newCompileSourceRootsList;
    }

    private SourceInclusionScanner getSourceInclusionScanner(int staleMillis) {
        SourceInclusionScanner scanner = null;
        if (includes.isEmpty() && excludes.isEmpty()) {
            scanner = new StaleSourceScanner(staleMillis);
        } else {
            if (includes.isEmpty()) {
                includes.add("**/*.java");
            }
            scanner = new StaleSourceScanner(staleMillis, includes, excludes);
        }

        return scanner;
    }

    private SourceInclusionScanner getSourceInclusionScanner(String inputFileEnding) {
        SourceInclusionScanner scanner = null;
        if (includes.isEmpty() && excludes.isEmpty()) {
            includes = Collections.singleton( "**/*." + inputFileEnding );
            scanner = new SimpleSourceInclusionScanner(includes, Collections.EMPTY_SET);
        } else {
            if (includes.isEmpty()) {
                includes.add("**/*." + inputFileEnding);
            }
            scanner = new SimpleSourceInclusionScanner(includes, excludes);
        }

        return scanner;
    }

    public List getClasspathElements() {
        return classpathElements;
    }

    public void setClasspathElements(List classpathElements) {
        this.classpathElements = classpathElements;
    }

    private List getCompileSourceRoots() {
        return compileSourceRoots;
    }

    public void setCompileSourceRoots(List compileSourceRoots) {
        this.compileSourceRoots = compileSourceRoots;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getCompilerArgument() {
        return compilerArgument;
    }

    public void setCompilerArgument(String compilerArgument) {
        this.compilerArgument = compilerArgument;
    }

    public Map getCompilerArguments() {
        return compilerArguments;
    }

    public void setCompilerArguments(Map compilerArguments) {
        this.compilerArguments.putAll(compilerArguments);
    }
}
