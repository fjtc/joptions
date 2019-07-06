package br.com.brokenbits.joptions.engine;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import br.com.brokenbits.joptions.annotations.OptionDescriptor;

public class OptionDescriptorImpl {

	private final String name;
	private final String description;
	private final boolean required;
	private final int maxRepetitions;
	private final Set<String> conflictsWithList;
	private final OptionParameterImpl converter;
	
	private int count = 0;
	
	protected OptionDescriptorImpl(Method method) {
		
		OptionDescriptor annotation = method.getAnnotation(OptionDescriptor.class);
		if (annotation == null) {
			throw new IllegalArgumentException(String.format("The method %1$s.%2$s is not annotated with OptionDescriptor.",
					method.getDeclaringClass().getName(), method.getName()));
		}
		this.name = annotation.name();
		this.description = annotation.description();
		this.maxRepetitions = annotation.maxRepetitions();
		this.required = annotation.required();
		this.conflictsWithList = new HashSet<String>();
		for (String v: annotation.conflictsWith()) {
			this.conflictsWithList.add(v);
		}
		this.converter = OptionParameterImpl.parse(method);
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public boolean isRequired() {
		return required;
	}

	public int getMaxOccurencies() {
		return maxRepetitions;
	}

	public boolean registerOccurency() {
		this.count++;
		if (this.maxRepetitions > 0) {
			return this.count <= this.maxRepetitions;
		} else {
			return true;
		}
	}
	
	public boolean checkConflict(String other) {
		return conflictsWithList.contains(other);
	}
	
	/**
	 * Resets the number of invocations of this option.
	 */
	public void reset() {
		this.count = 0;
	}
	
	/**
	 * Parses the given method to see if it defines an option or not.
	 * 
	 * @param method The method to be verified.
	 * @return The OptionDescriptorImpl that represents the option or null if the method is
	 * not suitable to be an option.
	 */
	public static OptionDescriptorImpl parse(Method method) {
		
		OptionDescriptor desc = method.getAnnotation(OptionDescriptor.class);
		if (desc == null) {
			return null;
		} else {
			return new OptionDescriptorImpl(method);
		}
	}
}
