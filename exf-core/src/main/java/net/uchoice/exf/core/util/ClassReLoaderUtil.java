package net.uchoice.exf.core.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassReLoaderUtil {

    private static final Map<String, AppClassLoader> CLASSLOADER_CACHE = new ConcurrentHashMap<String, AppClassLoader>();
    private static final Map<String, ApplicationContext> CONTEXT_CACHE = new ConcurrentHashMap<String, ApplicationContext>();

    public static AppClassLoader getAppClassLoader(String appJarName) throws Exception {
        return CLASSLOADER_CACHE.get(appJarName);
    }

    public static ApplicationContext getAppApplicationContext(String appJarName) throws Exception {
        return CONTEXT_CACHE.get(appJarName);
    }

    public static void removeAppClassLoader(String appJarName) throws Exception {
        CLASSLOADER_CACHE.remove(appJarName);
    }

    public static void removeAppApplicationContext(String appJarName) throws Exception {
        CONTEXT_CACHE.remove(appJarName);
    }

    public static void clearAppClassLoader() throws Exception {
        CLASSLOADER_CACHE.clear();
    }

    public static void clearAppApplicationContext() throws Exception {
        CONTEXT_CACHE.clear();
    }

    public static void classOnChange(String appJarName, String jarFilePath) throws Exception {
        File f = new File(jarFilePath);
        if (!f.exists()) {
            throw new Exception("jar包:" + jarFilePath + ",不存在");
        }

        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        AppClassLoader appClassLoader = null;
        {
            URL jarUrl = new URL("jar", "","file:" + f.getAbsolutePath()+"!/");
            appClassLoader = new AppClassLoader(new URL[]{jarUrl}, oldClassLoader);
            AppClassLoader old = CLASSLOADER_CACHE.put(appJarName, appClassLoader);
            if (old != null) {
                old.close();
            }
        }
    }

    @SuppressWarnings("resource")
	public static void onChange(String appJarName, String jarFilePath, ApplicationContext parentContext) throws Exception {
        //classloader
        File f = new File(jarFilePath);
        if (!f.exists()) {
            throw new Exception("jar包:" + jarFilePath + ",不存在");
        }

        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        AppClassLoader appClassLoader = null;
        {
            URL jarUrl = new URL("jar", "","file:" + f.getAbsolutePath()+"!/");
            appClassLoader = new AppClassLoader(new URL[]{jarUrl}, oldClassLoader);
            AppClassLoader old = CLASSLOADER_CACHE.put(appJarName, appClassLoader);
            if (old != null) {
                old.close();
            }
        }

        //spring
        {
            List<String> list = new ArrayList<String>();
            JarFile jarFile = new JarFile(jarFilePath);
            Enumeration<JarEntry> entrys = jarFile.entries();
            while (entrys.hasMoreElements()) {
                JarEntry jarEntry = entrys.nextElement();
                if (jarEntry.getName().endsWith("xml") && jarEntry.getName().indexOf("spring") != -1) {
                    list.add(new URL("jar:" + f.toURI().toURL().toString() + "!/"+ jarEntry.getName()).toString());
                }
            }

            try {
                Thread.currentThread().setContextClassLoader(appClassLoader);

                FileSystemXmlApplicationContext applicationContext = new FileSystemXmlApplicationContext(list.toArray(new String[]{}), parentContext);
                ApplicationContext old = CONTEXT_CACHE.put(appJarName, applicationContext);
                if (old != null) {
                    ((FileSystemXmlApplicationContext) old).close();
                }
            } catch (Exception ex) {
                throw ex;
            } finally {
                Thread.currentThread().setContextClassLoader(oldClassLoader);
            }
        }

    }
}
