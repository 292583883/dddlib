package org.dayatang.ioc.spring.factory;

import org.dayatang.domain.InstanceProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * 实例提供者接口的Spring实现。
 * SpringProvider内部通过Spring IoC的ApplicationContext实现对象创建。
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public class SpringInstanceProvider implements InstanceProvider {

	private ApplicationContext applicationContext;

	/**
	 * 以一批spring配置文件的路径初始化spring实例提供者。
	 * @param locations spring配置文件的路径的集合。spring将从类路径开始获取这批资源文件。
	 */
	public SpringInstanceProvider(String... locations) {
		applicationContext = new ClassPathXmlApplicationContext(locations);
	}
	
	/**
	 * 从ApplicationContext生成SpringProvider
	 * @param applicationContext
	 */
	public SpringInstanceProvider(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * 根据一批Spring配置文件初始化spring实例提供者。
	 * @param annotatedClasses
	 */
	public SpringInstanceProvider(Class<?>... annotatedClasses) {
		applicationContext = new AnnotationConfigApplicationContext(annotatedClasses);
	}
	
	/**
	 * 返回指定类型的实例。
	 * @param beanClass 实例的类型
	 * @return 指定类型的实例。
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getInstance(Class<T> beanClass) {
		return (T) applicationContext.getBean(beanClass);
	}

	@Override
	public <T> T getInstance(Class<T> beanClass, String beanName) {
		return (T) applicationContext.getBean(beanName, beanClass);
	}

    /**
     * 获取指定类型的、含有指定Annotation的对象实例。
     *
     * @param beanClass  实例的类型
     * @param annotation 实现类的annotation
     * @return 指定类型的实例。
     */
    @Override
    public <T> T getInstance(Class<T> beanClass, Annotation annotation) {
        if (annotation == null) {
            return getInstance(beanClass);
        }
        Map<String, T> results = applicationContext.getBeansOfType(beanClass);
        for (Map.Entry<String, T> entry : results.entrySet()) {
            if (applicationContext.findAnnotationOnBean(entry.getKey(), annotation.annotationType()) != null) {
                return entry.getValue();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
	public <T> T getByBeanName(String beanName) {
		return (T) applicationContext.getBean(beanName);
	}
}