package net.uchoice.exf.core.util;

import javax.annotation.Nonnull;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import static org.apache.commons.lang.Validate.notEmpty;

/**
 * Spring Bean 工具
 *
 */
public class SpringApplicationContextHolder implements ApplicationContextAware {

    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringApplicationContextHolder.class);

    /**
     * spring application context.
     */
    private static ApplicationContext context;

    @SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringApplicationContextHolder.context = context;
    }

    /**
     * get spring bean via name.
     *
     * @param beanName the bean's name.
     * @param <T>      the generic of the spring bean.
     * @return the found spring bean.
     */
    @SuppressWarnings("unchecked")
	public static <T> T getSpringBean(String beanName) {
        notEmpty(beanName, "bean name is required");
        if (null == context) {
            LOGGER.warn("spring application context is not injected");
            return null;
        }
        return (T) context.getBean(beanName);
    }
    
    public static <T> T getSpringBean(String beanName, Class<T> beanClass) {
        notEmpty(beanName, "bean name is required");
        if (null == context) {
            LOGGER.warn("spring application context is not injected");
            return null;
        }
        return context.getBean(beanName, beanClass);
    }

    /**
     * get the spring bean via the Class.
     *
     * @param beanClass the bean class.
     * @param <T>       the generic type of the bean.
     * @return the found bean.
     */
    public static <T> T getSpringBean(@Nonnull Class<T> beanClass) {
        String beanName = StringUtils.uncapitalize(beanClass.getSimpleName());
        return getSpringBean(beanName);
    }

    /**
     * get spring bean names.
     *
     * @return
     */
    public static String[] getBeanDefinitionNames() {
        return context.getBeanDefinitionNames();
    }

	public static ApplicationContext getContext() {
		return context;
	}

	public static void setContext(ApplicationContext context) {
		SpringApplicationContextHolder.context = context;
	}
}
