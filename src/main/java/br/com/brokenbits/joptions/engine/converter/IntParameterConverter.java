/*
 * BSD 3-Clause License
 * 
 * Copyright (c) 2019, Fabio Jun Takada Chino
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package br.com.brokenbits.joptions.engine.converter;

import br.com.brokenbits.joptions.annotations.OptionParameter;

public class IntParameterConverter implements ParameterConverter {

	protected int minValue = Integer.MIN_VALUE;
	
	protected int maxValue = Integer.MAX_VALUE;
	
	protected int base = 10;
	
	public IntParameterConverter() {
	}

	@Override
	public Object convert(String value, Class<?> type) throws IllegalArgumentException {
		int v;
		
		try {
			v = Integer.parseInt(value, this.base);
		} catch (Exception e) {
			throw new InvalidIntegerValueException(e);
		}
		
		if ((v < this.minValue) || (v > this.maxValue)) {
			throw new ValueOutOfRangeException();
		}

		if (Integer.TYPE.equals(type) || Integer.class.equals(type)) {
			return Integer.valueOf(v);
		} else if (Long.TYPE.equals(type) || Long.class.equals(type)) {
			return Long.valueOf(v);
		} else if (Float.TYPE.equals(type) || Float.class.equals(type)) {
			return Float.valueOf(v);
		} else if (Double.TYPE.equals(type) || Double.class.equals(type)) {
			return Double.valueOf(v);
		} else if (String.class.equals(type)) {
			return value;
		} else {
			throw new InvalidConverterParameterException(String.format("Type is not supported.", type.getName()));
		}
	}

	@Override
	public void init(OptionParameter p) throws IllegalArgumentException {
		String v;
		
		v = p.minValue();
		if ((v != null) && (!v.isBlank())) {
			try {
				this.minValue = Integer.parseInt(v);
			} catch (Exception e) {
				throw new InvalidConverterParameterException("Invalid minValue.");
			}
		}
		v = p.maxValue();
		if ((v != null) && (!v.isBlank())) {
			try {
				this.maxValue = Integer.parseInt(v);
			} catch (Exception e) {
				throw new InvalidConverterParameterException("Invalid maxValue.");
			}
		}
		if (this.minValue > this.maxValue) {
			throw new InvalidConverterParameterException("minValue is larger than maxValue.");
		}
		
		this.base = p.numberBase();
		if ((base < Character.MIN_RADIX) || (base > Character.MAX_RADIX)) {
			throw new InvalidConverterParameterException("Invalid number base.");
		}
	}

	@Override
	public boolean isCompatible(Class<?> type) {
		return 
				type.equals(String.class) ||
				type.equals(Integer.class) || 
				type.equals(Integer.TYPE) ||
				type.equals(Long.class) ||
				type.equals(Long.TYPE) ||
				type.equals(Float.class) ||
				type.equals(Float.TYPE) ||
				type.equals(Double.class) || 
				type.equals(Double.TYPE);
	}
}
