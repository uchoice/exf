package net.uchoice.exf.core.util;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public abstract class ClassLoaderUtil {

    public static URL[] getResources(String resourceName) {
        LinkedList<URL> urls  = new LinkedList<URL>();
        boolean found = false;

        // 首先试着从当前线程的ClassLoader中查找。
        found = getResources(urls, resourceName, getContextClassLoader(), false);

        // 如果没找到，试着从装入自己的ClassLoader中查找。
        if (!found) {
            getResources(urls, resourceName, ClassLoaderUtil.class.getClassLoader(), false);
        }

        // 最后的尝试: 在系统ClassLoader中查找(JDK1.2以上)，
        // 或者在JDK的内部ClassLoader中查找(JDK1.2以下)。
        if (!found) {
            getResources(urls, resourceName, null, true);
        }

        // 返回不重复的列表。
        return getDistinctURLs(urls);
    }

    public static URL[] getResources(String resourceName, Class<?> referrer) {
        ClassLoader classLoader = getReferrerClassLoader(referrer);
        LinkedList<URL>  urls = new LinkedList<URL>();

        getResources(urls, resourceName, classLoader, classLoader == null);

        // 返回不重复的列表。
        return getDistinctURLs(urls);
    }

    public static URL[] getResources(String resourceName, ClassLoader classLoader) {
        LinkedList<URL> urls = new LinkedList<URL>();

        getResources(urls, resourceName, classLoader, classLoader == null);

        // 返回不重复的列表。
        return getDistinctURLs(urls);
    }

    private static boolean getResources(List<URL> urlSet, String resourceName, ClassLoader classLoader,
                                        boolean sysClassLoader) {
        if (resourceName == null) {
            return false;
        }

        Enumeration<URL> i = null;

        try {
            if (classLoader != null) {
                i = classLoader.getResources(resourceName);
            } else if (sysClassLoader) {
                i = ClassLoader.getSystemResources(resourceName);
            }
        } catch (IOException e) {
        }

        if ((i != null) && i.hasMoreElements()) {
            while (i.hasMoreElements()) {
                urlSet.add(i.nextElement());
            }

            return true;
        }

        return false;
    }

    private static URL[] getDistinctURLs(LinkedList<URL> urls) {
        if ((urls == null) || (urls.size() == 0)) {
            return new URL[0];
        }

        Set<URL> urlSet = new HashSet<URL>(urls.size());

        for (Iterator<URL> i = urls.iterator(); i.hasNext();) {
            URL url = (URL) i.next();

            if (urlSet.contains(url)) {
                i.remove();
            } else {
                urlSet.add(url);
            }
        }

        return (URL[]) urls.toArray(new URL[urls.size()]);
    }

    public static URL getResource(String resourceName) {
        if (resourceName == null) {
            return null;
        }

        ClassLoader classLoader = null;
        URL         url = null;

        // 首先试着从当前线程的ClassLoader中查找。
        classLoader = getContextClassLoader();

        if (classLoader != null) {
            url = classLoader.getResource(resourceName);

            if (url != null) {
                return url;
            }
        }

        // 如果没找到，试着从装入自己的ClassLoader中查找。
        classLoader = ClassLoaderUtil.class.getClassLoader();

        if (classLoader != null) {
            url = classLoader.getResource(resourceName);

            if (url != null) {
                return url;
            }
        }

        // 最后的尝试: 在系统ClassLoader中查找(JDK1.2以上)，
        // 或者在JDK的内部ClassLoader中查找(JDK1.2以下)。
        return ClassLoader.getSystemResource(resourceName);
    }

    public static URL getResource(String resourceName, Class<?> referrer) {
        if (resourceName == null) {
            return null;
        }

        ClassLoader classLoader = getReferrerClassLoader(referrer);

        return (classLoader == null)
                ? ClassLoaderUtil.class.getClassLoader().getResource(resourceName)
                : classLoader.getResource(resourceName);
    }

    /**
     * 从指定的<code>ClassLoader</code>取得resource URL。
     *
     * @param resourceName 要查找的资源名，就是以&quot;/&quot;分隔的标识符字符串
     * @param classLoader
     *        在指定classLoader中查找，如果为<code>null</code>，表示在<code>ClassLoaderUtil</code>的class
     *        loader中找。
     *
     * @return resource URL，如果没找到，则返回<code>null</code>
     */
    public static URL getResource(String resourceName, ClassLoader classLoader) {
        if (resourceName == null) {
            return null;
        }

        return (classLoader == null)
                ? ClassLoaderUtil.class.getClassLoader().getResource(resourceName)
                : classLoader.getResource(resourceName);
    }

    private static ClassLoader getReferrerClassLoader(Class<?> referrer) {
        ClassLoader classLoader = null;

        if (referrer != null) {
            classLoader = referrer.getClassLoader();

            // classLoader为null，说明referrer类是由bootstrap classloader装载的，
            // 例如：java.lang.String
            if (classLoader == null) {
                classLoader = ClassLoader.getSystemClassLoader();
            }
        }

        return classLoader;
    }

    public static ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static boolean isSubClassOf(Class<?> subClass, Class<?> targetSuperClass) {
        if (subClass.isAssignableFrom(targetSuperClass)) {
            return true;
        }

        Class<?> superClass = subClass.getSuperclass();
        while (null != superClass) {
            if (superClass.equals(Object.class)) {
                return false;
            }
            if (superClass.isAssignableFrom(targetSuperClass)) {
                return true;
            }
            superClass = superClass.getSuperclass();
        }

        return false;
    }

}
