package br.com.uarini.ticket.json;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author marcosandreao@gmail.com
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Card implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8498803925236095056L;

	private boolean valid;

	private Balance balance;

	private Release[] release;

	private Scheduling[] scheduling;

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public Balance getBalance() {
		return balance;
	}

	public void setBalance(Balance balance) {
		this.balance = balance;
	}

	public Release[] getRelease() {
		return release;
	}

	public void setRelease(Release[] release) {
		this.release = release;
	}

	public Scheduling[] getScheduling() {
		return scheduling;
	}

	public void setScheduling(Scheduling[] scheduling) {
		this.scheduling = scheduling;
	}

}
