package net.uchoice.exf.core.util;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

public class AppClassLoader extends URLClassLoader {

    public AppClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public AppClassLoader(URL[] urls) {
        super(urls);
    }

    public AppClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }


    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return loadClass(name, false);
    }


    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {

        Class<?> c = findLoadedClass(name);

        /*if(c == null) {
            //处理需要强制从父加载器加载的类
            //String classPackage = name.substring(0, name.lastIndexOf('.'));//类的包路径
            if(TMFSettingParams.canNotAutoLoadClasses.contains(classPackage)) {
                if (getParent() != null) {
                    c = getParent().loadClass(name);
                }
            }
        }*/

        if (c == null) {
            try {
                c = findClass(name);
            } catch (ClassNotFoundException cnfe) {
                // ignore
            }
        }

        if (c == null) {
            if (getParent() != null) {
                c = getParent().loadClass(name);
            } else {
                c = getSystemClassLoader().loadClass(name);
            }
        }

        if (resolve) {
            resolveClass(c);
        }

        return c;
    }

    public URL getResource(String name) {
        URL url = findResource(name);

        if (url == null) {
            url = getParent().getResource(name);
        }
        return url;
    }
}
