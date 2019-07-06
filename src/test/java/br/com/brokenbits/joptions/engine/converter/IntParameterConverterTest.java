package br.com.brokenbits.joptions.engine.converter;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import br.com.brokenbits.joptions.annotations.OptionParameter;

public class IntParameterConverterTest {
	
	public void annotationDefault(
			@OptionParameter
			int i) {
	}
	
	public void annotationMinOnly(
			@OptionParameter(minValue = "-1")
			int i) {
	}
	
	public void annotationMinInvalid(
			@OptionParameter(minValue = "-")
			int i) {
	}

	public void annotationMaxOnly(
			@OptionParameter(maxValue = "1")
			int i) {
	}
	
	public void annotationMaxInvalid(
			@OptionParameter(maxValue = "d")
			int i) {
	}
	
	public void annotationOK(
			@OptionParameter(
					minValue = "1",
					maxValue = "2")
			int i) {
	}
	
	public void annotationEqual(
			@OptionParameter(
					minValue = "1",
					maxValue = "1")
			int i) {
	}
	
	public void annotationInverted(
			@OptionParameter(
					minValue = "2",
					maxValue = "1")
			int i) {
	}
	
	public void annotationBase16(
			@OptionParameter(
					numberBase = 16)
			int i) {
	}
	
	public void annotationBaseInvalidLow(
			@OptionParameter(
					numberBase = 1)
			int i) {
	}
	
	public void annotationBaseInvalidHigh(
			@OptionParameter(
					numberBase = 37)
			int i) {
	}
	
	private IntParameterConverter createAndInit(String methodName) throws Exception {
		IntParameterConverter c = new IntParameterConverter();
		
		Method m = this.getClass().getMethod(methodName, Integer.TYPE);
		OptionParameter p = m.getParameters()[0].getAnnotation(OptionParameter.class);
		assertNotNull(p);
		c.init(p);
		return c;
	}
	
	
	@Test
	public void testIntParameterConverter() {
		IntParameterConverter c = new IntParameterConverter();
		
		assertEquals(Integer.MIN_VALUE, c.minValue);
		assertEquals(Integer.MAX_VALUE, c.maxValue);
	}

	@Test
	public void testInit() throws Exception {
		IntParameterConverter c = createAndInit("annotationDefault");

		assertEquals(Integer.MIN_VALUE, c.minValue);
		assertEquals(Integer.MAX_VALUE, c.maxValue);
	}
	
	@Test
	public void testInitMinOnly() throws Exception {
		IntParameterConverter c = createAndInit("annotationMinOnly");

		assertEquals(-1, c.minValue);
		assertEquals(Integer.MAX_VALUE, c.maxValue);
	}
	
	@Test
	public void testInitMaxOnly() throws Exception {
		IntParameterConverter c = createAndInit("annotationMaxOnly");

		assertEquals(Integer.MIN_VALUE, c.minValue);
		assertEquals(1, c.maxValue);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInitMinInvalid() throws Exception {
		@SuppressWarnings("unused")
		IntParameterConverter c = createAndInit("annotationMinInvalid");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInitMaxInvalid() throws Exception {
		@SuppressWarnings("unused")
		IntParameterConverter c = createAndInit("annotationMaxInvalid");
	}
	
	@Test
	public void testInitOK() throws Exception {

		IntParameterConverter c = createAndInit("annotationOK");
		
		assertEquals(1, c.minValue);
		assertEquals(2, c.maxValue);
		
		Integer v;
		v = (Integer)c.convert("1", Integer.class);
		assertEquals(Integer.valueOf(1), v);
		v = (Integer)c.convert("2", Integer.class);
		assertEquals(Integer.valueOf(2), v);
		
		try {
			c.convert("0", Integer.class);
			fail();
		} catch(ValueOutOfRangeException e) {}
		
		try {
			c.convert("3", Integer.class);
			fail();
		} catch(ValueOutOfRangeException e) {}
	}

	@Test
	public void testInitEqual() throws Exception {
		IntParameterConverter c = createAndInit("annotationEqual");
		
		assertEquals(1, c.minValue);
		assertEquals(1, c.maxValue);
		
		Integer v;
		v = (Integer)c.convert("1", Integer.class);
		assertEquals(Integer.valueOf(1), v);
		
		try {
			c.convert("0", Integer.class);
			fail();
		} catch(ValueOutOfRangeException e) {}
		
		try {
			c.convert("2", Integer.class);
			fail();
		} catch(ValueOutOfRangeException e) {}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInitInverted() throws Exception {
		@SuppressWarnings("unused")
		IntParameterConverter c = createAndInit("annotationInverted");		
	}
	
	@Test
	public void testInitBase16() throws Exception {
		IntParameterConverter c = createAndInit("annotationBase16");

		Integer v;
		v = (Integer)c.convert("F", Integer.class);
		assertEquals(Integer.valueOf(15), v);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInitInvalidLow() throws Exception {
		@SuppressWarnings("unused")
		IntParameterConverter c = createAndInit("annotationBaseInvalidLow");		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInitInvalidHigh() throws Exception {
		@SuppressWarnings("unused")
		IntParameterConverter c = createAndInit("annotationBaseInvalidHigh");
	}
	
	@Test
	public void testConvertDefault() {
		IntParameterConverter c = new IntParameterConverter();
	
		Object v;
		
		v = c.convert("1", Integer.class);
		assertEquals(Integer.class, v.getClass());
		assertEquals(Integer.valueOf(1), v);
		
		v = c.convert("1", Integer.TYPE);
		assertEquals(Integer.class, v.getClass());
		assertEquals(Integer.valueOf(1), v);
		
		v = c.convert("1", Long.class);
		assertEquals(Long.class, v.getClass());
		assertEquals(Long.valueOf(1), v);
		
		v = c.convert("1", Long.TYPE);
		assertEquals(Long.class, v.getClass());
		assertEquals(Long.valueOf(1), v);
		
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
		IntParameterConverter c = new IntParameterConverter();
		
		assertTrue(c.isCompatible(String.class));
		assertTrue(c.isCompatible(Integer.class));
		assertTrue(c.isCompatible(Integer.TYPE));
		assertTrue(c.isCompatible(Long.class));
		assertTrue(c.isCompatible(Long.TYPE));
		assertTrue(c.isCompatible(Float.class));
		assertTrue(c.isCompatible(Float.TYPE));
		assertTrue(c.isCompatible(Double.class));
		assertTrue(c.isCompatible(Double.TYPE));
		
		// Some random options
		assertFalse(c.isCompatible(this.getClass()));
		assertFalse(c.isCompatible(IntParameterConverter.class));
	}
}
