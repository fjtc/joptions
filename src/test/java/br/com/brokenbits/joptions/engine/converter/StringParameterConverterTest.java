package br.com.brokenbits.joptions.engine.converter;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import br.com.brokenbits.joptions.annotations.OptionParameter;

public class StringParameterConverterTest {
	
	public void annotationDefault(
			@OptionParameter
			String i) {
	}
	
	public void annotationMinOnly(
			@OptionParameter(
					minLength = 1)
			String i) {
	}
	
	public void annotationMinInvalid(
			@OptionParameter(
					minLength = -1)
			String i) {
	}

	public void annotationMaxOnly(
			@OptionParameter(maxLength = 1)
			String i) {
	}
	
	public void annotationMaxInvalid(
			@OptionParameter(maxLength = -1)
			String i) {
	}
	
	public void annotationOK(
			@OptionParameter(
					minLength = 1,
					maxLength = 2)
			String i) {
	}
	
	public void annotationEqual(
			@OptionParameter(
					minLength = 1,
					maxLength = 1)
			String i) {
	}
	
	public void annotationInverted(
			@OptionParameter(
					minLength = 2,
					maxLength = 1)
			String i) {
	}
	
	public void annotationRegex(
			@OptionParameter(
					regex = "[0-9]+")
			String i) {
	}
	
	public void annotationRegexInvalid(
			@OptionParameter(
					regex = "[0-9")
			String i) {
	}
	
	private StringParameterConverter createAndInit(String methodName) throws Exception {
		StringParameterConverter c = new StringParameterConverter();
		
		Method m = this.getClass().getMethod(methodName, String.class);
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
		StringParameterConverter c = createAndInit("annotationDefault");

		assertEquals(0, c.minLength);
		assertEquals(Integer.MAX_VALUE, c.maxLength);
		assertNull(c.pattern);
	}
	
	@Test
	public void testInitMinOnly() throws Exception {
		StringParameterConverter c = createAndInit("annotationMinOnly");

		assertEquals(1, c.minLength);
		assertEquals(Integer.MAX_VALUE, c.maxLength);
	}
	
	@Test
	public void testInitMaxOnly() throws Exception {
		StringParameterConverter c = createAndInit("annotationMaxOnly");

		assertEquals(0, c.minLength);
		assertEquals(1, c.maxLength);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInitMinInvalid() throws Exception {
		@SuppressWarnings("unused")
		StringParameterConverter c = createAndInit("annotationMinInvalid");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInitMaxInvalid() throws Exception {
		@SuppressWarnings("unused")
		StringParameterConverter c = createAndInit("annotationMaxInvalid");
	}
	
	@Test
	public void testInitOK() throws Exception {

		StringParameterConverter c = createAndInit("annotationOK");
		
		assertEquals(1, c.minLength);
		assertEquals(2, c.maxLength);
		
		Object v;
		v = c.convert("1", String.class);
		assertEquals("1", v);
		v = c.convert("12", String.class);
		assertEquals("12", v);
		
		try {
			c.convert("", String.class);
			fail();
		} catch(ValueTooShortException e) {}
		
		try {
			c.convert("123", String.class);
			fail();
		} catch(ValueTooLongException e) {}
	}

	@Test
	public void testInitEqual() throws Exception {
		StringParameterConverter c = createAndInit("annotationEqual");
		
		assertEquals(1, c.minLength);
		assertEquals(1, c.maxLength);
		
		Object v;
		v = c.convert("1", String.class);
		assertEquals("1", v);
		
		try {
			c.convert("", String.class);
			fail();
		} catch(ValueTooShortException e) {}
		
		try {
			c.convert("12", String.class);
			fail();
		} catch(ValueTooLongException e) {}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInitInverted() throws Exception {
		@SuppressWarnings("unused")
		StringParameterConverter c = createAndInit("annotationInverted");		
	}
	
	@Test
	public void testConvertDefault() {
		StringParameterConverter c = new StringParameterConverter();
	
		Object v;
		
		v = c.convert("1123123123", String.class);
		assertEquals(String.class, v.getClass());
		assertEquals("1123123123", v);
	}
	
	@Test
	public void testInitRegex() throws Exception {
		StringParameterConverter c = createAndInit("annotationRegex");	
	
		Object v;
		
		v = c.convert("1123123123", String.class);
		assertEquals("1123123123", v);
		try {
			c.convert("x", String.class);
			fail();
		} catch (InvalidValueException e) {}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInitRegexFail() throws Exception {
		@SuppressWarnings("unused")
		StringParameterConverter c = createAndInit("annotationRegexInvalid");		
	}

	
	@Test
	public void testIsCompatible() {
		StringParameterConverter c = new StringParameterConverter();
		
		assertTrue(c.isCompatible(String.class));
		assertFalse(c.isCompatible(Long.class));
		assertFalse(c.isCompatible(Long.TYPE));
		assertFalse(c.isCompatible(Float.class));
		assertFalse(c.isCompatible(Float.TYPE));
		assertFalse(c.isCompatible(Double.class));
		assertFalse(c.isCompatible(Double.TYPE));
		
		// Some random options
		assertFalse(c.isCompatible(this.getClass()));
		assertFalse(c.isCompatible(LongParameterConverter.class));
	}
}
