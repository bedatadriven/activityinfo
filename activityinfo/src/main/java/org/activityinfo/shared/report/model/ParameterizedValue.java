package org.activityinfo.shared.report.model;

import java.io.Serializable;
import java.util.Map;

public class ParameterizedValue<T> implements Serializable {

	private T literal;
	private String parameterName;


	public ParameterizedValue(){

	}

	public static <T> ParameterizedValue<T> literal(T literalValue) {

		ParameterizedValue<T> value = new ParameterizedValue<T>();
		value.setLiteral(literalValue);

		return value;
	}

	public static <T> ParameterizedValue<T> forParam(String paramName) {
		ParameterizedValue<T> value = new ParameterizedValue<T>();
		value.setParameterName(paramName);

		return value;
	}


	public T getLiteral() {
		return literal;
	}


	public void setLiteral(T literal) {
		this.literal = literal;
	}


	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public T resolve(Map<String, Object> parameters) {
		if(literal != null) {
			return literal;
		}

		if(parameterName == null)
			throw new RuntimeException("Parameterized value has no literal nor parameter name specified");

		T value = (T)parameters.get(parameterName);

		if(value == null) {
			throw new RuntimeException("Missing parameter value: " + parameterName);
		}

		return value;
	}
}