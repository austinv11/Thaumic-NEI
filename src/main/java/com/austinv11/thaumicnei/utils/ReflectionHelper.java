package com.austinv11.thaumicnei.utils;

public class ReflectionHelper {

	public static <T> Class<? extends T> getClass(String className, Class<? extends T> extension) throws ClassNotFoundException {
		Class<?> c = Class.forName(className);
		if (c != null) {
			if (extension.isAssignableFrom(c)) {
				return (Class<? extends T>) c;
			}else {
				throw new RuntimeException(className+" doesn't extend "+extension.getName());
			}
		}else {
			throw new RuntimeException("Can't get " + className);
		}
	}
}
