package it.unibs;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Vector;

public class Utility {
	private static Scanner lettore = creaScanner();
	
	private final static String ERRORE_FORMATO = "Attenzione: il dato inserito non e' nel formato corretto.";
	
	private static Scanner creaScanner ()
	  {
	   Scanner creato = new Scanner(System.in);
	   creato.useDelimiter(System.getProperty("line.separator"));
	   return creato;
	  }
	

	  public static int leggiIntero (String messaggio)
	  {
	   boolean finito = false;
	   int valoreLetto = 0;
	   do
	    {
	     System.out.println(messaggio+"--->");
	     try
	      {
	       valoreLetto = lettore.nextInt();
	       finito = true;
	      }
	     catch (InputMismatchException e)
	      {
	       System.out.println(ERRORE_FORMATO);
	       String daButtare = lettore.next(); 
	      }
	    } while (!finito);
	   return valoreLetto;
	  }
	  
	  public static Integer leggiInteroOpzionale (String messaggio)
	  {
	   boolean finito = false;
	   Integer valoreLetto = null;
	   do
	    {
	     System.out.println(messaggio+"--->");
	     try
	      {
	    	 if (lettore.hasNextInt())
		      {
		       valoreLetto = lettore.nextInt();
		       finito = true;
		      } else if(lettore.next().equals("")){
		    	  valoreLetto=null;
		    	  finito=true;
		      }
	      }
	     catch (InputMismatchException e)
	      {
	       System.out.println(ERRORE_FORMATO);
	       String daButtare = lettore.next();
	      }
	     if(!finito) System.out.println("Inserimento errato, riprovare.");
	    } while (!finito);
	   return valoreLetto;
	  }
	  
	public static int scegli(String titoloMain, String[] vociMain, String query, int dim)
	{
		System.out.println("________________________________________________");
		System.out.println(titoloMain);
		System.out.println("________________________________________________");
		for(int i=0;i<dim;i++) {
			System.out.println(i+") "+vociMain[i]);
		}
		Integer a = leggiIntero(query);
		return a;
	}
	
		  
	public static String leggiStringa() 
	{
		String lettura = null;
		try {
			
			Scanner in= new Scanner(System.in);
			lettura=in.nextLine();
			if(lettura.isEmpty()) lettura=null;
		}catch(InputMismatchException e)
		{
			System.out.println("Errore di inserimento!");
		}
		return lettura;
	}
	
	public static String leggiStringa(String messaggio) 
	{
		String lettura = null;
		try {
			System.out.println(messaggio + " -->");
			Scanner in= new Scanner(System.in);
			lettura=in.nextLine();
			if(lettura.isEmpty()) lettura=null;
		}catch(InputMismatchException e)
		{
			System.out.println("Errore di inserimento!");
		}
		return lettura;
	}
	
	public static int sceltaDaLista(String messaggio, int dim) {
		boolean fine = false;
		int a;
		do {
			a = leggiIntero(messaggio);
			if(a>dim){
				System.out.println("Inserimento non valido!");
			}else{
				fine=true;
			}
		}while(!fine);
		return a;
	}

	public static void stampaVettoreNumerato(Vector<String> daStampare) {
		for (int i = 0; i < daStampare.size(); i++) {
			System.out.println(i+1 + ") " + daStampare.get(i));
		}
	}

}
