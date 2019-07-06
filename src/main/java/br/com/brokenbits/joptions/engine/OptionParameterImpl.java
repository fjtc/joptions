package br.com.brokenbits.joptions.engine;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import br.com.brokenbits.joptions.annotations.OptionParameter;
import br.com.brokenbits.joptions.engine.converter.ParameterConverter;

public class OptionParameterImpl {

	private Method method;
	
	private ParameterConverter converter;
	
	private Class<?> type;
	
	public OptionParameterImpl(Method method) {

		Parameter p = method.getParameters()[0];
		OptionParameter annotation = p.getAnnotation(OptionParameter.class);
		
		
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
		Object v = this.converter.convert(value, type);
		try {
			method.invoke(target, v);
		} catch (Exception e) {
			throw new IllegalArgumentException(
					String.format("Unable to invoke the setter %1$s.%2$s().",
							method.getDeclaringClass().getName(), method.getName()), e);
		}
	}
	
	public static OptionParameterImpl parse(Method method) {
		
		if (method.getParameterCount() == 0) {
			return null;
		} else if (method.getParameterCount() > 1) {
			throw new IllegalArgumentException(
					String.format("The method %1$s.%2$s() has more than 1 parameter.",
							method.getDeclaringClass().getName(), method.getName()));
		} else {
			return new OptionParameterImpl(method);
		}
	}
}
