/**
 * Copyright 2010 the original author or authors.
 * 
 * This file is part of Zksample2. http://zksample2.sourceforge.net/
 *
 * Zksample2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Zksample2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Zksample2.  If not, see <http://www.gnu.org/licenses/gpl.html>.
 */
package de.forsthaus.backend.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;

/**
 * @author bbruhns
 * 
 */
public final class CustomDataAccessUtils {
	private final static class Array2ListTransformer<T> {
		private final BeanSetterClass[] beanSetter;
		private final ObjectFactory<T> factory;

		Array2ListTransformer(Class<T> beanClass, String... args) {
			factory = new ObjectFactory<T>(beanClass);
			beanSetter = new BeanSetterClass[args.length];
			for (int i = 0; i < args.length; i++) {
				String beanName = convertToSetterName(args[i]);
				beanSetter[i] = new BeanSetterClass(beanClass, beanName);
			}
		}

		Array2ListTransformer(Class<T> beanClass, String[] args, Class<?>[] argsClass) {
			if (!ArrayUtils.isSameLength(args, argsClass)) {
				throw new RuntimeException("?");
			}
			factory = new ObjectFactory<T>(beanClass);
			beanSetter = new BeanSetterClass[args.length];
			for (int i = 0; i < args.length; i++) {
				String beanName = convertToSetterName(args[i]);
				beanSetter[i] = new BeanSetterClass(beanClass, beanName, argsClass[i]);
			}
		}

		List<T> transfer2Bean(Collection<?> col) {
			List<T> result = new ArrayList<T>(col.size());
			for (Object row1 : col) {
				T bean = factory.create();
				Object[] row = (Object[]) row1;
				for (int j = 0; j < row.length; j++) {
					beanSetter[j].invocSetter(bean, row[j]);
				}
				result.add(bean);
			}
			return result;
		}

		List<T> transfer2Bean(Object[] array) {
			return transfer2Bean(Arrays.asList(array));
		}
	}

	private final static class BeanSetterClass {
		final private Method method;

		BeanSetterClass(Class<?> beanClass, String beanName) {
			Method[] methods = beanClass.getMethods();
			Method methodTmp = null;
			for (Method method1 : methods) {
				if (method1.getName().equals(beanName)) {
					methodTmp = method1;
					break;
				}
			}
			if (methodTmp == null) {
				throw new RuntimeException("Methode " + beanName + " nicht gefunden!");
			}
			method = methodTmp;
		}

		BeanSetterClass(Class<?> beanClass, String beanName, Class<?> clazz) {
			try {
				method = beanClass.getMethod(beanName, clazz);
			} catch (SecurityException e) {
				throw new RuntimeException(e);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
		}

		void invocSetter(Object obj, Object value) {
			try {
				method.invoke(obj, value);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private final static class ObjectFactory<T> {
		private final Class<T> clazz;

		ObjectFactory(Class<T> clazz) {
			super();
			this.clazz = clazz;
		}

		T create() {
			try {
				return clazz.newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

	}

	static String convertToSetterName(String propertyName) {
		return "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
	}

	/**
	 * 
	 * @param col
	 *            of Type Collection<Object[]>
	 * @param beanClass
	 * @param args
	 *            Names of des Property in the <code>beanClass</code>
	 * @return
	 */
	public static <T> List<T> transfer2Bean(Collection<?> col, Class<T> beanClass, String... args) {
		if (CollectionUtils.isEmpty(col)) {
			return Collections.emptyList();
		}

		return new Array2ListTransformer<T>(beanClass, args).transfer2Bean(col);
	}

	/**
	 * 
	 * @param col
	 *            from Type Collection<Object[]>
	 * @param beanClass
	 * @param args
	 *            Names of des Property in the <code>beanClass</code>
	 * @param argsClass
	 *            Typs of the <code>args</code>
	 * @return
	 */
	public static <T> List<T> transfer2Bean(Collection<?> col, Class<T> beanClass, String[] args, Class<?>[] argsClass) {
		if (CollectionUtils.isEmpty(col)) {
			return Collections.emptyList();
		}

		return new Array2ListTransformer<T>(beanClass, args, argsClass).transfer2Bean(col);
	}

	/**
	 * 
	 * @param array
	 *            from Type Object[][]
	 * @param beanClass
	 * @param args
	 *            Names of des Property in the <code>beanClass</code>
	 * @return
	 */
	public static <T> List<T> transfer2Bean(Object[] array, Class<T> beanClass, String... args) {
		if (array == null || array.length == 0) {
			return Collections.emptyList();
		}

		return new Array2ListTransformer<T>(beanClass, args).transfer2Bean(array);
	}

	/**
	 * 
	 * @param array
	 *            from Type Object[][]
	 * @param beanClass
	 * @param args
	 *            Names of des Property in the <code>beanClass</code>
	 * @param argsClass
	 *            Typs of the <code>args</code>
	 * @return
	 */
	public static <T> List<T> transfer2Bean(Object[] array, Class<T> beanClass, String[] args, Class<?>[] argsClass) {
		if (array == null || array.length == 0) {
			return Collections.emptyList();
		}

		return new Array2ListTransformer<T>(beanClass, args).transfer2Bean(array);
	}

}
