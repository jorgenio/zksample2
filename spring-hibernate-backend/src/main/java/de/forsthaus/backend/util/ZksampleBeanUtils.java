package de.forsthaus.backend.util;

import java.lang.reflect.InvocationTargetException;

/**
 * EN: Utility class for beans.<br>
 * DE: Utility Klasse zum Manipulieren von Beans.<br>
 * 
 * @author Stephan Gerth
 */
public class ZksampleBeanUtils {

	/**
	 * EN: Clone a bean based on the available property getters and setters,
	 * even if the bean class itself does not implement Cloneable.<br>
	 * DE: Gibt ein geklontes Bean aufgrund seiner vorhandenen Getter/Setter
	 * Werte zurueck.<br>
	 * 
	 * @param bean
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Object cloneBean(Object bean) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
		return org.apache.commons.beanutils.BeanUtils.cloneBean(bean);
	}

}
