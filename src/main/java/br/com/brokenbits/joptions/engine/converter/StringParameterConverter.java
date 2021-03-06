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

import java.util.regex.Pattern;

import br.com.brokenbits.joptions.annotations.OptionParameter;

public class StringParameterConverter implements ParameterConverter {

	protected int minLength = 0;
	
	protected int maxLength = Integer.MAX_VALUE;
	
	protected Pattern pattern;
	
	@Override
	public Object convert(String value, Class<?> type) throws IllegalArgumentException {

		if (!type.equals(String.class)) {
			throw new IllegalArgumentException("Incompatible type.");
		}
		if (value.length() < this.minLength) {
			throw new ValueTooShortException();
		}
		if (value.length() > this.maxLength) {
			throw new ValueTooLongException();
		}
		if (this.pattern != null) {
			if (!this.pattern.matcher(value).matches()) {
				throw new InvalidStringValueException();
			}
		}
		return value;
	}

	@Override
	public void init(OptionParameter p) throws IllegalArgumentException {
		
		this.minLength = p.minLength();
		if (this.minLength < 0) {
			throw new IllegalArgumentException("minLength cannot be negative.");
		}
		this.maxLength = p.maxLength();
		if (this.minLength > this.maxLength) {
			throw new IllegalArgumentException("minLength is larger than maxLength.");
		}
		
		String v = p.regex();
		if ((v != null) && (!v.isBlank())) {
			this.pattern = Pattern.compile(v);
		}
	}

	@Override
	public boolean isCompatible(Class<?> type) {
		return type.isAssignableFrom(String.class);
	}
}
