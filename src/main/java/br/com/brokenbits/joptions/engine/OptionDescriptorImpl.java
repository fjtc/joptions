package br.com.brokenbits.joptions.engine;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import br.com.brokenbits.joptions.annotations.OptionDescriptor;

public class OptionDescriptorImpl {

	private final String name;
	private final String description;
	private final boolean required;
	private final int maxOccurencies;
	private final Set<String> conflictsWithList;
	private final OptionConverterDescriptionImpl converter;
	
	private int count = 0;
	
	protected OptionDescriptorImpl(Method method) {
		
		OptionDescriptor annotation = method.getAnnotation(OptionDescriptor.class);
		if (annotation == null) {
			throw new IllegalArgumentException(String.format("The method %1$s.%2$s is not annotated with OptionDescriptor.",
					method.getDeclaringClass().getName(), method.getName()));
		}
		this.name = annotation.name();
		this.description = annotation.description();
		this.maxOccurencies = annotation.maxOccurencies();
		this.required = annotation.required();
		this.conflictsWithList = new HashSet<String>();
		for (String v: annotation.conflictsWith()) {
			this.conflictsWithList.add(v);
		}
		this.converter = OptionConverterDescriptionImpl.parse(method);
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
		return maxOccurencies;
	}

	public boolean registerOccurency() {
		this.count++;
		if (this.maxOccurencies > 0) {
			return this.count <= this.maxOccurencies;
		} else {
			return true;
		}
	}
	
	public boolean checkConflict(String other) {
		return conflictsWithList.contains(other);
	}
	
	public void reset() {
		this.count = 0;
	}
}
