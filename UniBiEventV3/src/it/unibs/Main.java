package it.unibs;

import java.io.*;
import java.util.*;

public class Main {
	
	private static final String pathname = "data\\application.dat";
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"),Locale.ITALY);
		Data dataOdierna = new Data((int)calendar.get(Calendar.DATE), ((int)calendar.get(Calendar.MONTH)+1), (int)calendar.get(Calendar.YEAR));
		Ora oraAttuale = new Ora((int)calendar.get(Calendar.HOUR), (int)calendar.get(Calendar.MINUTE));
		Application anApplication;
		
		
		File aFile = new File(pathname);
		if(aFile.exists()) {
			anApplication = (Application) ServizioFile.caricaSingoloOggetto(aFile);
			
		}else {
			anApplication = new Application();
		}
		
		anApplication.runApplication(dataOdierna, oraAttuale);
		
		ServizioFile.salvaSingoloOggetto(aFile, anApplication);
		System.out.println("Salvataggio...");
	}
}
