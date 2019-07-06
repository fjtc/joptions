package br.com.brokenbits.joptions.engine.converter;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import br.com.brokenbits.joptions.annotations.OptionParameter;

public class DoubleParameterConverterTest {
	
	public void annotationDefault(
			@OptionParameter
			double i) {
	}
	
	public void annotationMinOnly(
			@OptionParameter(minValue = "-1")
			double i) {
	}
	
	public void annotationMinInvalid(
			@OptionParameter(minValue = "-")
			double i) {
	}

	public void annotationMaxOnly(
			@OptionParameter(maxValue = "1")
			double i) {
	}
	
	public void annotationMaxInvalid(
			@OptionParameter(maxValue = "d")
			double i) {
	}
	
	public void annotationOK(
			@OptionParameter(
					minValue = "1",
					maxValue = "2")
			double i) {
	}
	
	public void annotationEqual(
			@OptionParameter(
					minValue = "1",
					maxValue = "1")
			double i) {
	}
	
	public void annotationInverted(
			@OptionParameter(
					minValue = "2",
					maxValue = "1")
			double i) {
	}
	
	private DoubleParameterConverter createAndInit(String methodName) throws Exception {
		DoubleParameterConverter c = new DoubleParameterConverter();
		
		Method m = this.getClass().getMethod(methodName, Double.TYPE);
		OptionParameter p = m.getParameters()[0].getAnnotation(OptionParameter.class);
		assertNotNull(p);
		c.init(p);
		return c;
	}
	
	
	@Test
	public void testDoubleParameterConverter() {
		DoubleParameterConverter c = new DoubleParameterConverter();
		
		assertEquals(Double.MIN_VALUE, c.minValue, 0.0f);
		assertEquals(Double.MAX_VALUE, c.maxValue, 0.0f);
	}

	@Test
	public void testInit() throws Exception {
		DoubleParameterConverter c = createAndInit("annotationDefault");

		assertEquals(Double.MIN_VALUE, c.minValue, 0.0f);
		assertEquals(Double.MAX_VALUE, c.maxValue, 0.0f);
	}
	
	@Test
	public void testInitMinOnly() throws Exception {
		DoubleParameterConverter c = createAndInit("annotationMinOnly");

		assertEquals(-1, c.minValue, 0.0f);
		assertEquals(Double.MAX_VALUE, c.maxValue, 0.0f);
	}
	
	@Test
	public void testInitMaxOnly() throws Exception {
		DoubleParameterConverter c = createAndInit("annotationMaxOnly");

		assertEquals(Double.MIN_VALUE, c.minValue, 0.0f);
		assertEquals(1, c.maxValue, 0.0f);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInitMinInvalid() throws Exception {
		@SuppressWarnings("unused")
		DoubleParameterConverter c = createAndInit("annotationMinInvalid");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInitMaxInvalid() throws Exception {
		@SuppressWarnings("unused")
		DoubleParameterConverter c = createAndInit("annotationMaxInvalid");
	}
	
	@Test
	public void testInitOK() throws Exception {

		DoubleParameterConverter c = createAndInit("annotationOK");
		
		assertEquals(1, c.minValue, 0.0f);
		assertEquals(2, c.maxValue, 0.0f);
		
		Double v;
		v = (Double)c.convert("1", Double.class);
		assertEquals(Double.valueOf(1), v);
		v = (Double)c.convert("2", Double.class);
		assertEquals(Double.valueOf(2), v);
		
		try {
			c.convert("0", Double.class);
			fail();
		} catch(ValueOutOfRangeException e) {}
		
		try {
			c.convert("3", Double.class);
			fail();
		} catch(ValueOutOfRangeException e) {}
	}

	@Test
	public void testInitEqual() throws Exception {
		DoubleParameterConverter c = createAndInit("annotationEqual");
		
		assertEquals(1, c.minValue, 0.0f);
		assertEquals(1, c.maxValue, 0.0f);
		
		Double v;
		v = (Double)c.convert("1", Double.class);
		assertEquals(Double.valueOf(1), v);
		
		try {
			c.convert("0", Double.class);
			fail();
		} catch(ValueOutOfRangeException e) {}
		
		try {
			c.convert("2", Double.class);
			fail();
		} catch(ValueOutOfRangeException e) {}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInitInverted() throws Exception {
		@SuppressWarnings("unused")
		DoubleParameterConverter c = createAndInit("annotationInverted");		
	}
	
	@Test
	public void testConvertDefault() {
		DoubleParameterConverter c = new DoubleParameterConverter();
	
		Object v;
		
		v = c.convert("1", Double.class);
		assertEquals(Double.class, v.getClass());
		assertEquals(Double.valueOf(1), v);
		
		v = c.convert("1", Double.TYPE);
		assertEquals(Double.class, v.getClass());
		assertEquals(Double.valueOf(1), v);
		
		v = c.convert("1", Double.class);
		assertEquals(Double.class, v.getClass());
		assertEquals(Double.valueOf(1), v);
		
		v = c.convert("1", Double.TYPE);
		assertEquals(Double.class, v.getClass());
		assertEquals(Double.valueOf(1), v);
		
		v = c.convert("1", String.class);
		assertEquals(String.class, v.getClass());
		assertEquals("1", v);
	}
	
	@Test
	public void testIsCompatible() {
		DoubleParameterConverter c = new DoubleParameterConverter();
		
		assertTrue(c.isCompatible(String.class));
		assertTrue(c.isCompatible(Double.class));
		assertTrue(c.isCompatible(Double.TYPE));
		
		// Some random options
		assertFalse(c.isCompatible(this.getClass()));
		assertFalse(c.isCompatible(DoubleParameterConverter.class));
	}
}
