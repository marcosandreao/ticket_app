package br.com.uarini.ticket.json;

import java.io.Serializable;

/**
 * @author marcosandreao@gmail.com
 * 
 */
public abstract class AbstractValue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -276199467669049745L;

	private String date;

	private String value;

	private String description;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
