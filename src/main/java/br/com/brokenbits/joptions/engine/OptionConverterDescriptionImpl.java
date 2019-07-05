package br.com.brokenbits.joptions.engine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import br.com.brokenbits.joptions.engine.converter.OptionConverter;

public class OptionConverterDescriptionImpl {

	private Method method;
	
	private OptionConverter converter;
	
	public OptionConverterDescriptionImpl(Method method) {
		// TODO Auto-generated constructor stub
	}

	
	public boolean hasParameter() {
		return this.method.getParameterCount() > 0;
	}
	
	public void reportOption(Object target) {
		try {
			method.invoke(target);
		} catch (Exception e) {
			throw new IllegalArgumentException(
					String.format("Unable to invoke the setter %1$s.%2$s().",
							method.getDeclaringClass().getName(), method.getName()), e);
		}
	}
	
	public void reportOption(Object target, String value) {
		Object v = this.converter.convert(value);
		try {
			method.invoke(target, v);
		} catch (Exception e) {
			throw new IllegalArgumentException(
					String.format("Unable to invoke the setter %1$s.%2$s().",
							method.getDeclaringClass().getName(), method.getName()), e);
		}
	}
	
	public static OptionConverterDescriptionImpl parse(Method method) {
		return null;
	}
}
