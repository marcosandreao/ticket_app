package br.com.uarini.ticket.util;

import android.content.res.Resources;
import br.com.uarini.ticket.R;

public class Util {

	public static final int QUANTIDADE_INICIO = 5;
	public static final int QUANTIDADE_MEDIA = 15;
	public static final int QUANTIDADE_GRANDE = 30;
	
	public static final String TA = "TA";
	public static final String TK = "TK";
	public static final String TR = "TR";
	
	public static String formatCardNumber(final String cardNumber) {
		if (cardNumber.length() < 16) {
			return cardNumber;
		}

		final String formated = String.format("%s %s %s %s",
				cardNumber.substring(0, 4), cardNumber.substring(4, 8),
				cardNumber.substring(8, 12), cardNumber.substring(12, 16));
		return formated;
	}
	
	public static String formatCardNumber(Resources res, int tipo, final String cardNumber) {
		if ( cardNumber == null ) {
			return "---- ---- ----- ----";
		}
		if (cardNumber.length() < 4) {
			return cardNumber;
		}
		final int value = getNomeBin(tipo);
		if ( value != -1 ) {
			return res.getString(value) + " (" + cardNumber.substring(cardNumber.length() - 4, cardNumber.length()) + ")";
		}
		
		if (cardNumber.length() < 16) {
			return cardNumber;
		}
		
		final String formated = String.format("%s %s %s %s",
				cardNumber.substring(0, 4), cardNumber.substring(4, 8),
				cardNumber.substring(8, 12), cardNumber.substring(12, 16));
		return formated;
	}
	public static final String getBin(int tipo) {
		String result = "";
		switch (tipo) {
		case 0:
			result = TA;
			break;
		case 1:
			result = TK;
			break;
		case 2:
			result = TR;
			break;
		}
		return result;
	}
	public static  final int getNomeBin(int tipo) {
		int result = -1;
		switch (tipo) {
		case 0:
			result =  R.string.ta;
			break;
		case 1:
			result =  R.string.tk;
			break;
		case 2:
			result =  R.string.tr;
			break;
		}
		
		return result;
	}
	
	public static final int getIndiceCartaoBin(String bin) {
		if ( bin == null ) {
			return 10;
		}
		if ( bin.equalsIgnoreCase(TA) ) {
			return 0;
		} 
		if ( bin.equalsIgnoreCase(TK) ) {
			return 1;
		}
		if ( bin.equalsIgnoreCase(TR) ) {
			return 2;
		}
		return 10;
		
	}
}
