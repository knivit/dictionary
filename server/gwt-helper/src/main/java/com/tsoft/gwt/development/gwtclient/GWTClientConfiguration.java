package com.tsoft.gwt.development.gwtclient;

public class GWTClientConfiguration {
    private String sourceRoot;
    private String targetRoot;
    private String workingDirectory;
    private String gwtClientAnnotation;
    private String sourceGwtRoot;
    private String sourceGwtFileName;
    private String generatorWorkingFolder;
    private String outputJarFileName;
    private String[] interfaceFilePatterns;
    private String[] gwtFilePatterns;

    public String getGwtClientAnnotation() {
        return gwtClientAnnotation;
    }

    public void setGwtClientAnnotation(String gwtClientAnnotation) {
        this.gwtClientAnnotation = gwtClientAnnotation;
    }

    public String[] getGwtFilePatterns() {
        return gwtFilePatterns;
    }

    public void setGwtFilePatterns(String[] gwtFilePatterns) {
        this.gwtFilePatterns = gwtFilePatterns;
    }

    public String[] getInterfaceFilePatterns() {
        return interfaceFilePatterns;
    }

    public void setInterfaceFilePatterns(String[] interfaceFilePatterns) {
        this.interfaceFilePatterns = interfaceFilePatterns;
    }

    public String getOutputJarFileName() {
        return outputJarFileName;
    }

    public void setOutputJarFileName(String outputJarFileName) {
        this.outputJarFileName = outputJarFileName;
    }

    public String getSourceGwtFileName() {
        return sourceGwtFileName;
    }

    public void setSourceGwtFileName(String sourceGwtFileName) {
        this.sourceGwtFileName = sourceGwtFileName;
    }

    public String getSourceGwtRoot() {
        return sourceGwtRoot;
    }

    public void setSourceGwtRoot(String sourceGwtRoot) {
        this.sourceGwtRoot = sourceGwtRoot;
    }

    public String getSourceRoot() {
        return sourceRoot;
    }

    public void setSourceRoot(String sourceRoot) {
        this.sourceRoot = sourceRoot;
    }

    public String getTargetRoot() {
        return targetRoot;
    }

    public void setTargetRoot(String targetRoot) {
        this.targetRoot = targetRoot;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public String getGeneratorWorkingFolder() {
        return generatorWorkingFolder;
    }

    public void setGeneratorWorkingFolder(String generatorWorkingFolder) {
        this.generatorWorkingFolder = generatorWorkingFolder;
    }
}
