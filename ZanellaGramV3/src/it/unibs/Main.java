package it.unibs;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Main {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"),Locale.ITALY);
		Data dataOdierna = new Data(calendar.getTime().getDay(), calendar.getTime().getMonth(), calendar.getTime().getYear());
		Ora oraAttuale = new Ora(calendar.getTime().getHours(), calendar.getTime().getMinutes());
		Application anApplication = new Application(dataOdierna, oraAttuale);
		anApplication.runApplication();

	}
}
