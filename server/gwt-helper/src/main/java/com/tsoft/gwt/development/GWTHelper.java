package com.tsoft.gwt.development;

import com.tsoft.gwt.development.gwtclient.GWTClientConfiguration;
import com.tsoft.gwt.development.gwtclient.GWTClientHelper;
import com.tsoft.util.sax.AbstractElementListener;
import com.tsoft.util.sax.ElementStack;
import com.tsoft.util.sax.XMLParser;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.codehaus.plexus.util.DirectoryScanner;
import org.datanucleus.enhancer.DataNucleusEnhancer;
import org.xml.sax.SAXException;

public class GWTHelper {
    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 6) {
            System.out.println("Usage:\n" +
                "<path to sources>\n" +
                "<path to folder for generated files and the resulting jar>\n" +
                "<class name of the GWTClient marker>\n" +
                "<path to root for .gwt.xml file>\n" +
                "<class name of the .gwt.xml file>\n" +
                "Patterns of files' names"
            );
            return;
        }

        // parse input arguments
        GWTClientConfiguration clientConfiguration = new GWTClientConfiguration();
        clientConfiguration.setSourceRoot(args[0]);
        clientConfiguration.setWorkingDirectory(args[1]);
        clientConfiguration.setGeneratorWorkingFolder("gwt-helper");
        clientConfiguration.setTargetRoot(args[1] + "/" + clientConfiguration.getGeneratorWorkingFolder());
        clientConfiguration.setGwtClientAnnotation(args[2]);
        clientConfiguration.setSourceGwtRoot(args[3]);
        clientConfiguration.setSourceGwtFileName(args[4]);
        clientConfiguration.setOutputJarFileName(args[5]);

        Map<String, List<String>> keyValues = Utils.parseKeyValues(Arrays.copyOfRange(args, 6, args.length));

        List<String> asyncFileList = keyValues.get("-async");
        clientConfiguration.setInterfaceFilePatterns(asyncFileList.toArray(new String[asyncFileList.size()]));

        List<String> clientFileList = keyValues.get("-client");
        clientConfiguration.setGwtFilePatterns(clientFileList.toArray(new String[clientFileList.size()]));

        GWTClientHelper clientHelper = new GWTClientHelper();
        clientHelper.process(clientConfiguration);

        // enhance JPA classes with Datanucleus Enhancer
        List<String> jpaClassList = keyValues.get("-datanucleus-enhancer-classes");
        if (jpaClassList.size() == 1 && jpaClassList.get(0).endsWith("/META-INF/persistence.xml")) {
            String xmlFileName = jpaClassList.get(0);
            jpaClassList = getJPAClassFilesFromPersistenceXmlFile(xmlFileName);
        }

        File classRootFile = new File(clientConfiguration.getWorkingDirectory() + "/classes");
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(classRootFile);
        scanner.setIncludes(jpaClassList.toArray(new String[jpaClassList.size()]));
        scanner.scan();

        String[] classFileNames = scanner.getIncludedFiles();
        for (int i = 0; i < classFileNames.length; i ++) {
            classFileNames[i] = classRootFile.getPath() + "/" + classFileNames[i];
        }

        DataNucleusEnhancer enhancer = new DataNucleusEnhancer("JPA", "ASM");
        List<String> enhancerProperties = keyValues.get("-datanucleus-enhancer-properties");
        if (enhancerProperties != null) {
            for (String property : enhancerProperties) {
                int n = property.indexOf(':');
                String propertyName = property.substring(0, n).trim();
                String propertyValue = property.substring(n + 1).trim();
                enhancer.getProperties().put(propertyName, propertyValue);
            }
        }
        enhancer.addFiles(classFileNames);
        enhancer.enhance();
    }

    private static List<String> getJPAClassFilesFromPersistenceXmlFile(String xmlFileName) throws ParserConfigurationException, SAXException, IOException {
        List<String> list = new ArrayList<String>();

        XMLParser parser = new XMLParser(new PersistentXmlElementListener(list));
        parser.parse(xmlFileName);

        return list;
    }

    private static class PersistentXmlElementListener extends AbstractElementListener {
        private List<String> list;

        public PersistentXmlElementListener(List<String> list) {
            this.list = list;
        }

        @Override
        public void processValue(String value, ElementStack stack) {
            if (stack.endsWith("class", "persistence-unit")) {
                String classFileName = value.replace('.', '/') + ".class";
                list.add(classFileName);
            }
        }
    }
}
