package com.tsoft.gwt.development.compiler;

import java.net.URLClassLoader;
import java.net.URL;

public class IsolatedClassLoader extends URLClassLoader {
    private ClassLoader parentClassLoader = ClassLoader.getSystemClassLoader();

    public IsolatedClassLoader() {
        super(new URL[0], null);
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }

    @Override
    public synchronized Class loadClass(String className) throws ClassNotFoundException {
        Class c = findLoadedClass(className);
        ClassNotFoundException ex = null;

        if (c == null) {
            try {
                c = findClass(className);
            } catch (ClassNotFoundException e) {
                ex = e;
                if (parentClassLoader != null) {
                    c = parentClassLoader.loadClass(className);
                }
            }
        }

        if (c == null) {
            throw ex;
        }

        return c;
    }
}
