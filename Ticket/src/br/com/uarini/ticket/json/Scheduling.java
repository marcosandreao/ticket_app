package br.com.uarini.ticket.json;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author marcosandreao@gmail.com
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Scheduling extends AbstractValue {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4724281341189293245L;
	
	private String number;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

}
