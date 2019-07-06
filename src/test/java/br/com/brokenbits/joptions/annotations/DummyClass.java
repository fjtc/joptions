package br.com.brokenbits.joptions.annotations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DummyClass {

	public DummyClass() {
		// TODO Auto-generated constructor stub
	}


	@OptionDescriptor(
			name= "param1",
			description = "Description of the param 1.",
			maxRepetitions = 1,
			required = true,
			conflictsWith = {})
	public void setParam1() {
	}
	
	
	@OptionDescriptor(
			name= "param2",
			description = "Description of the param 2.",
			maxRepetitions = 1,
			required = true,
			conflictsWith = {})
	public void setParam2(
			@OptionParameter()
			int v) {
	}
	
	public static void main(String [] args) {

		System.out.println(InputStream.class.isAssignableFrom(ByteArrayInputStream.class));
		System.out.println(Integer.TYPE.isAssignableFrom(Integer.class));
		System.out.println(Integer.TYPE.isAssignableFrom(Integer.TYPE));
		
		try {
		
			Object o = Integer.TYPE.getConstructor(Integer.TYPE).newInstance(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
