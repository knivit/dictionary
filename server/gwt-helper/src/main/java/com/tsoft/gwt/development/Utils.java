package com.tsoft.gwt.development;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils;

public final class Utils {
    private Utils() { }

    /**
     * Get the path of an executable tool: try to find it depending the OS or the <code>jdk.home</code>
     * system property or the <code>JAVA_HOME</code> environment variable.
     */
    public static String getJdkToolExecutable(String toolName) throws IOException {
        String toolCommand = toolName + (Os.isFamily(Os.FAMILY_WINDOWS) ? ".exe" : "");
        String javaHome = System.getProperty("jdk.home");

        File toolExe;
        if (Os.isName("AIX")) {
            toolExe = new File(javaHome + File.separator + ".." + File.separator + "sh", toolCommand);
        } else if (Os.isName("Mac OS X")) {
            toolExe = new File(javaHome + File.separator + "bin", toolCommand);
        } else {
            toolExe = new File(javaHome + File.separator + ".." + File.separator + "bin", toolCommand);
        }

        if (!toolExe.isFile()) {
            Properties env = CommandLineUtils.getSystemEnvVars();
            javaHome = env.getProperty("JAVA_HOME");
            if (StringUtils.isEmpty(javaHome)) {
                throw new IOException("The environment variable JAVA_HOME is not correctly set.");
            }

            if (!new File(javaHome).isDirectory()) {
                throw new IOException("The environment variable JAVA_HOME=" + javaHome + " doesn't exist or is not a valid directory.");
            }

            toolExe = new File(env.getProperty("JAVA_HOME") + File.separator + "bin", toolCommand);
        }

        if (!toolExe.isFile()) {
            throw new IOException("The javadoc executable '" + toolExe + "' doesn't exist or is not a file. Verify the JAVA_HOME environment variable.");
        }

        return toolExe.getAbsolutePath();
    }

    public static Map<String, List<String>> parseKeyValues(String[] args) {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        if (args == null || args.length == 0) {
            return map;
        }

        int index = 0;
        String key = null;
        List<String> list = null;
        while (index < args.length) {
            if (args[index].startsWith("-")) {
                if (key != null) {
                    map.put(key, list);
                }
                key = args[index];
                list = new ArrayList<String>();
            } else {
                if (key != null) {
                    list.add(args[index]);
                } else {
                    throw new IllegalArgumentException("args' list should start with '-key' value");
                }
            }

            index ++;
        }

        if (key != null) {
            map.put(key, list);
        }

        return map;
    }
}
