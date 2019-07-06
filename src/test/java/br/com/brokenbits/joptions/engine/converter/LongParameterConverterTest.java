package br.com.brokenbits.joptions.engine.converter;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import br.com.brokenbits.joptions.annotations.OptionParameter;

public class LongParameterConverterTest {
	
	public void annotationDefault(
			@OptionParameter
			long i) {
	}
	
	public void annotationMinOnly(
			@OptionParameter(minValue = "-1")
			long i) {
	}
	
	public void annotationMinInvalid(
			@OptionParameter(minValue = "-")
			long i) {
	}

	public void annotationMaxOnly(
			@OptionParameter(maxValue = "1")
			long i) {
	}
	
	public void annotationMaxInvalid(
			@OptionParameter(maxValue = "d")
			long i) {
	}
	
	public void annotationOK(
			@OptionParameter(
					minValue = "1",
					maxValue = "2")
			long i) {
	}
	
	public void annotationEqual(
			@OptionParameter(
					minValue = "1",
					maxValue = "1")
			long i) {
	}
	
	public void annotationInverted(
			@OptionParameter(
					minValue = "2",
					maxValue = "1")
			long i) {
	}
	
	public void annotationBase16(
			@OptionParameter(
					numberBase = 16)
			long i) {
	}
	
	public void annotationBaseInvalidLow(
			@OptionParameter(
					numberBase = 1)
			long i) {
	}
	
	public void annotationBaseInvalidHigh(
			@OptionParameter(
					numberBase = 37)
			long i) {
	}
	
	private LongParameterConverter createAndInit(String methodName) throws Exception {
		LongParameterConverter c = new LongParameterConverter();
		
		Method m = this.getClass().getMethod(methodName, Long.TYPE);
		OptionParameter p = m.getParameters()[0].getAnnotation(OptionParameter.class);
		assertNotNull(p);
		c.init(p);
		return c;
	}
	
	
	@Test
	public void testLongParameterConverter() {
		LongParameterConverter c = new LongParameterConverter();
		
		assertEquals(Long.MIN_VALUE, c.minValue);
		assertEquals(Long.MAX_VALUE, c.maxValue);
	}

	@Test
	public void testInit() throws Exception {
		LongParameterConverter c = createAndInit("annotationDefault");

		assertEquals(Long.MIN_VALUE, c.minValue);
		assertEquals(Long.MAX_VALUE, c.maxValue);
	}
	
	@Test
	public void testInitMinOnly() throws Exception {
		LongParameterConverter c = createAndInit("annotationMinOnly");

		assertEquals(-1, c.minValue);
		assertEquals(Long.MAX_VALUE, c.maxValue);
	}
	
	@Test
	public void testInitMaxOnly() throws Exception {
		LongParameterConverter c = createAndInit("annotationMaxOnly");

		assertEquals(Long.MIN_VALUE, c.minValue);
		assertEquals(1, c.maxValue);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInitMinInvalid() throws Exception {
		@SuppressWarnings("unused")
		LongParameterConverter c = createAndInit("annotationMinInvalid");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInitMaxInvalid() throws Exception {
		@SuppressWarnings("unused")
		LongParameterConverter c = createAndInit("annotationMaxInvalid");
	}
	
	@Test
	public void testInitOK() throws Exception {

		LongParameterConverter c = createAndInit("annotationOK");
		
		assertEquals(1, c.minValue);
		assertEquals(2, c.maxValue);
		
		Long v;
		v = (Long)c.convert("1", Long.class);
		assertEquals(Long.valueOf(1), v);
		v = (Long)c.convert("2", Long.class);
		assertEquals(Long.valueOf(2), v);
		
		try {
			c.convert("0", Long.class);
			fail();
		} catch(ValueOutOfRangeException e) {}
		
		try {
			c.convert("3", Long.class);
			fail();
		} catch(ValueOutOfRangeException e) {}
	}

	@Test
	public void testInitEqual() throws Exception {
		LongParameterConverter c = createAndInit("annotationEqual");
		
		assertEquals(1, c.minValue);
		assertEquals(1, c.maxValue);
		
		Long v;
		v = (Long)c.convert("1", Long.class);
		assertEquals(Long.valueOf(1), v);
		
		try {
			c.convert("0", Long.class);
			fail();
		} catch(ValueOutOfRangeException e) {}
		
		try {
			c.convert("2", Long.class);
			fail();
		} catch(ValueOutOfRangeException e) {}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInitInverted() throws Exception {
		@SuppressWarnings("unused")
		LongParameterConverter c = createAndInit("annotationInverted");		
	}
	
	@Test
	public void testInitBase16() throws Exception {
		LongParameterConverter c = createAndInit("annotationBase16");

		Long v;
		v = (Long)c.convert("F", Long.class);
		assertEquals(Long.valueOf(15), v);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInitInvalidLow() throws Exception {
		@SuppressWarnings("unused")
		LongParameterConverter c = createAndInit("annotationBaseInvalidLow");		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInitInvalidHigh() throws Exception {
		@SuppressWarnings("unused")
		LongParameterConverter c = createAndInit("annotationBaseInvalidHigh");
	}
	
	@Test
	public void testConvertDefault() {
		LongParameterConverter c = new LongParameterConverter();
	
		Object v;
		
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
		LongParameterConverter c = new LongParameterConverter();
		
		assertTrue(c.isCompatible(String.class));
		assertTrue(c.isCompatible(Long.class));
		assertTrue(c.isCompatible(Long.TYPE));
		assertTrue(c.isCompatible(Float.class));
		assertTrue(c.isCompatible(Float.TYPE));
		assertTrue(c.isCompatible(Double.class));
		assertTrue(c.isCompatible(Double.TYPE));
		
		// Some random options
		assertFalse(c.isCompatible(this.getClass()));
		assertFalse(c.isCompatible(LongParameterConverter.class));
	}
}
