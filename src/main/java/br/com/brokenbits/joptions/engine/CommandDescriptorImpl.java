package br.com.brokenbits.joptions.engine;

import br.com.brokenbits.joptions.annotations.CommandDescriptor;

public class CommandDescriptorImpl {

	private final String name;
	
	private final String description;
	
	private final String summary;
	
	public CommandDescriptorImpl(String name, CommandDescriptor annotation) {
		this.name = name;
		this.description = annotation.description();
		this.summary = annotation.summary();
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getSummary() {
		return summary;
	}
}
