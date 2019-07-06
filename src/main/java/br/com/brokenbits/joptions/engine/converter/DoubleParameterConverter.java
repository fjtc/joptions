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

public class DoubleParameterConverter implements ParameterConverter {

	protected double minValue = Double.MIN_VALUE;
	
	protected double maxValue = Double.MAX_VALUE;
	
	public DoubleParameterConverter() {
	}

	@Override
	public Object convert(String value, Class<?> type) throws IllegalArgumentException {
		double v;
		
		try {
			v = Double.parseDouble(value);
		} catch (Exception e) {
			throw new InvalidDoubleValueException(e);
		}
		
		if ((v < this.minValue) || (v > this.maxValue)) {
			throw new ValueOutOfRangeException();
		}

		if (Double.TYPE.equals(type) || Double.class.equals(type)) {
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
				this.minValue = Double.parseDouble(v);
			} catch (Exception e) {
				throw new InvalidConverterParameterException("Invalid minValue.");
			}
		}
		v = p.maxValue();
		if ((v != null) && (!v.isBlank())) {
			try {
				this.maxValue = Double.parseDouble(v);
			} catch (Exception e) {
				throw new InvalidConverterParameterException("Invalid maxValue.");
			}
		}
		if (this.minValue > this.maxValue) {
			throw new InvalidConverterParameterException("minValue is larger than maxValue.");
		}
	}

	@Override
	public boolean isCompatible(Class<?> type) {
		return 
				type.equals(String.class) ||
				type.equals(Float.TYPE) ||
				type.equals(Double.class) || 
				type.equals(Double.TYPE);
	}
}
