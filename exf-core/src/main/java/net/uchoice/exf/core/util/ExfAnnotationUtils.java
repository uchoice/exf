package net.uchoice.exf.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;

import org.springframework.core.annotation.AnnotationUtils;

public class ExfAnnotationUtils extends AnnotationUtils {

    private static final Map<Class<?>, Boolean> annotatedInterfaceCache = new WeakHashMap<>();

    public static <A extends Annotation> A findAnnotation(Method method, Class<A> annotationType) {
        A annotation = getAnnotation(method, annotationType);
        Class<?> clazz = method.getDeclaringClass();
        if (annotation == null) {
            annotation = searchOnInterfaces(method, annotationType, clazz.getInterfaces());
        }
        while (annotation == null) {
            clazz = clazz.getSuperclass();
            if (clazz == null || clazz.equals(Object.class)) {
                break;
            }
            try {
                Method equivalentMethod = clazz.getDeclaredMethod(method.getName(), method.getParameterTypes());
                annotation = getAnnotation(equivalentMethod, annotationType);
            }
            catch (NoSuchMethodException ex) {
                // No equivalent method found
            }
            if (annotation == null) {
                annotation = searchOnInterfaces(method, annotationType, clazz.getInterfaces());
            }
        }
        return annotation;
    }

    private static <A extends Annotation> A searchOnInterfaces(Method method, Class<A> annotationType, Class<?>[] ifcs) {
        A annotation = null;
        for (Class<?> iface : ifcs) {
            if (isInterfaceWithAnnotatedMethods(iface)) {
                try {
                    Method equivalentMethod = iface.getMethod(method.getName(), method.getParameterTypes());
                    annotation = getAnnotation(equivalentMethod, annotationType);
                }
                catch (NoSuchMethodException ex) {
                    // if not find method, may be param is generic.
                    Method[] methods = iface.getMethods();
                    for (Method oneMethod : methods) {
                        if (oneMethod.getName().equals(method.getName())) {
                            annotation = getAnnotation(oneMethod, annotationType);
                            if (annotation != null) {
                                break;
                            }
                        }
                    }
                }
                if (annotation != null) {
                    break;
                }

                Class<?>[] superIfcs = iface.getInterfaces();
                annotation = searchOnInterfaces(method, annotationType, superIfcs);
            }
        }
        return annotation;
    }

    private static boolean isInterfaceWithAnnotatedMethods(Class<?> iface) {
        synchronized (annotatedInterfaceCache) {
            Boolean flag = annotatedInterfaceCache.get(iface);
            if (flag != null) {
                return flag;
            }
            boolean found = false;
            for (Method ifcMethod : iface.getMethods()) {
                if (ifcMethod.getAnnotations().length > 0) {
                    found = true;
                    break;
                }
            }
            annotatedInterfaceCache.put(iface, found);
            return found;
        }
    }
}
