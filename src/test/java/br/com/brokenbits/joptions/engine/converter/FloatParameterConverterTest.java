package br.com.brokenbits.joptions.engine.converter;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import br.com.brokenbits.joptions.annotations.OptionParameter;

public class FloatParameterConverterTest {
	
	public void annotationDefault(
			@OptionParameter
			float i) {
	}
	
	public void annotationMinOnly(
			@OptionParameter(minValue = "-1")
			float i) {
	}
	
	public void annotationMinInvalid(
			@OptionParameter(minValue = "-")
			float i) {
	}

	public void annotationMaxOnly(
			@OptionParameter(maxValue = "1")
			float i) {
	}
	
	public void annotationMaxInvalid(
			@OptionParameter(maxValue = "d")
			float i) {
	}
	
	public void annotationOK(
			@OptionParameter(
					minValue = "1",
					maxValue = "2")
			float i) {
	}
	
	public void annotationEqual(
			@OptionParameter(
					minValue = "1",
					maxValue = "1")
			float i) {
	}
	
	public void annotationInverted(
			@OptionParameter(
					minValue = "2",
					maxValue = "1")
			float i) {
	}
	
	private FloatParameterConverter createAndInit(String methodName) throws Exception {
		FloatParameterConverter c = new FloatParameterConverter();
		
		Method m = this.getClass().getMethod(methodName, Float.TYPE);
		OptionParameter p = m.getParameters()[0].getAnnotation(OptionParameter.class);
		assertNotNull(p);
		c.init(p);
		return c;
	}
	
	
	@Test
	public void testFloatParameterConverter() {
		FloatParameterConverter c = new FloatParameterConverter();
		
		assertEquals(Float.MIN_VALUE, c.minValue, 0.0f);
		assertEquals(Float.MAX_VALUE, c.maxValue, 0.0f);
	}

	@Test
	public void testInit() throws Exception {
		FloatParameterConverter c = createAndInit("annotationDefault");

		assertEquals(Float.MIN_VALUE, c.minValue, 0.0f);
		assertEquals(Float.MAX_VALUE, c.maxValue, 0.0f);
	}
	
	@Test
	public void testInitMinOnly() throws Exception {
		FloatParameterConverter c = createAndInit("annotationMinOnly");

		assertEquals(-1, c.minValue, 0.0f);
		assertEquals(Float.MAX_VALUE, c.maxValue, 0.0f);
	}
	
	@Test
	public void testInitMaxOnly() throws Exception {
		FloatParameterConverter c = createAndInit("annotationMaxOnly");

		assertEquals(Float.MIN_VALUE, c.minValue, 0.0f);
		assertEquals(1, c.maxValue, 0.0f);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInitMinInvalid() throws Exception {
		@SuppressWarnings("unused")
		FloatParameterConverter c = createAndInit("annotationMinInvalid");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInitMaxInvalid() throws Exception {
		@SuppressWarnings("unused")
		FloatParameterConverter c = createAndInit("annotationMaxInvalid");
	}
	
	@Test
	public void testInitOK() throws Exception {

		FloatParameterConverter c = createAndInit("annotationOK");
		
		assertEquals(1, c.minValue, 0.0f);
		assertEquals(2, c.maxValue, 0.0f);
		
		Float v;
		v = (Float)c.convert("1", Float.class);
		assertEquals(Float.valueOf(1), v);
		v = (Float)c.convert("2", Float.class);
		assertEquals(Float.valueOf(2), v);
		
		try {
			c.convert("0", Float.class);
			fail();
		} catch(ValueOutOfRangeException e) {}
		
		try {
			c.convert("3", Float.class);
			fail();
		} catch(ValueOutOfRangeException e) {}
	}

	@Test
	public void testInitEqual() throws Exception {
		FloatParameterConverter c = createAndInit("annotationEqual");
		
		assertEquals(1, c.minValue, 0.0f);
		assertEquals(1, c.maxValue, 0.0f);
		
		Float v;
		v = (Float)c.convert("1", Float.class);
		assertEquals(Float.valueOf(1), v);
		
		try {
			c.convert("0", Float.class);
			fail();
		} catch(ValueOutOfRangeException e) {}
		
		try {
			c.convert("2", Float.class);
			fail();
		} catch(ValueOutOfRangeException e) {}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInitInverted() throws Exception {
		@SuppressWarnings("unused")
		FloatParameterConverter c = createAndInit("annotationInverted");		
	}
	
	@Test
	public void testConvertDefault() {
		FloatParameterConverter c = new FloatParameterConverter();
	
		Object v;
		
		v = c.convert("1", Float.class);
		assertEquals(Float.class, v.getClass());
		assertEquals(Float.valueOf(1), v);
		
		v = c.convert("1", Float.TYPE);
		assertEquals(Float.class, v.getClass());
		assertEquals(Float.valueOf(1), v);
		
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
		FloatParameterConverter c = new FloatParameterConverter();
		
		assertTrue(c.isCompatible(String.class));
		assertTrue(c.isCompatible(Float.class));
		assertTrue(c.isCompatible(Float.TYPE));
		assertTrue(c.isCompatible(Double.class));
		assertTrue(c.isCompatible(Double.TYPE));
		
		// Some random options
		assertFalse(c.isCompatible(this.getClass()));
		assertFalse(c.isCompatible(FloatParameterConverter.class));
	}
}
