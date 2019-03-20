package it.unibs;

import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;


public class Application {
	
	public static String pathProfilo = "C:\\Users\\zenry\\git\\ZanellaGramV3\\ZanellaGramV3\\data\\profilo.dat";
	public static String pathPartite = "C:\\Users\\zenry\\git\\ZanellaGramV3\\ZanellaGramV3\\data\\partite.dat";
	
	private static final int NUMERO_CAMPI=16;
	
	private static final int TITOLO=0;
	private static final int NUMERO_PARTECIPANTI=1;
	private static final int TERMINE_ISCRIZIONI=2;
	private static final int LUOGO=3;
	private static final int DATA=4;
	private static final int ORA=5;
	private static final int DURATA=6;
	private static final int QUOTA=7;
	private static final int COMPRESO_IN_QUOTA=8;
	private static final int DATA_CONCLUSIVA=9;
	private static final int ORA_CONCLUSIVA=10;
	private static final int NOTE=11;
	private static final int TOLLERANZA_PARTECIPANTI=12;
	private static final int TERMINE_RITIRO_ISCRIZIONE=13;
	
	
	private static final int GENERE=14;
	private static final int FASCIA_DI_ETA=15;
	
	private String[] categorie = {"Partite di calcio"};
	private Data dataOdierna;
	private Ora oraAttuale;
	private SpazioPersonale mioProfilo;
	private String titoloMain = "HOME";
	private Vector<PartitaDiCalcio> listaPartite;
	private String[] vociMain = {"Esci e salva","Vedi eventi", "Crea evento", "Vedi profilo"};
	private String[] vociSpazioPersonale = {"Esci","Vedi eventi che ho creato","Vedi eventi a cui sono iscritto","Vedi notifiche"};
	
	private Campo[] campi;
	
	public Application(Data dataOdierna, Ora oraAttuale) throws ClassNotFoundException, IOException {
		initObjects();
		this.dataOdierna=dataOdierna;
		this.oraAttuale=oraAttuale;
	}

	@SuppressWarnings("unchecked")
	private void initObjects() throws ClassNotFoundException, IOException {
		campi = new Campo[NUMERO_CAMPI];
		assegnaPartitaDiCalcio(campi);
		
		//caricamento oggetti
		if(new File(pathProfilo).exists())mioProfilo=(SpazioPersonale)caricaOggetto(pathProfilo, SpazioPersonale.class);
		else mioProfilo = new SpazioPersonale();
		
		if(new File(pathPartite).exists())listaPartite=(Vector<PartitaDiCalcio>)caricaOggetto(pathPartite, PartitaDiCalcio.class);
		else listaPartite = new Vector<PartitaDiCalcio>();
	}
	
	@SuppressWarnings("unchecked")
	public Object caricaOggetto(String path, Class c) throws ClassNotFoundException, IOException
	{
		FileInputStream in = new FileInputStream(new File(path));
		ObjectInputStream objectIn=new ObjectInputStream(in);
		Object result=new Object();
		
		if(c==PartitaDiCalcio.class) {
			result = (Vector<Categoria>) objectIn.readObject();
			objectIn.close();
		}
		else if(c==SpazioPersonale.class){
			result = (SpazioPersonale) objectIn.readObject();
			objectIn.close();
		}
		return result;
	}
	
	
	
	public void runApplication() throws IOException {
		controlloEventi();
		boolean fine=false;
		while(!fine)
		{	
			int i = Utility.scegli(titoloMain,vociMain,"Seleziona una voce",4);
			switch(i) {
				case 0: {fine=true;
					esciEsalva();}
					break;
				case 1:vediCategorie();
					break;
				case 2:creaEvento();
					break;
				case 3: visualizzaSpazioPersonale();
					break;
				default: System.out.println("Scelta non valida!");
					break;
				
			}
		}
	}


	private void controlloEventi() {
		for (Categoria evento : listaPartite) {
			if(evento.aggiornaStato(dataOdierna)) listaPartite.remove(evento);
		}
	}

	private void creaEvento() {
		stampaCategorie();
		int scelta= Utility.sceltaDaLista("Seleziona categoria (0 per tornare alla home)",categorie.length);
		switch(scelta)
		{
			case 1: compilaEvento(PartitaDiCalcio.class);
				break;
			case 0: return;
		}
	}
	
	public void compilaEvento(Class c) {
		if(c==PartitaDiCalcio.class) {
			Campo[] campi =new Campo [16];
			assegnaPartitaDiCalcio(campi);
			compilazioneCampiGenerici(campi);
			for (int i = 14; i < 16; i++) {
				System.out.print(campi[i].toString());
				switch (i)
				{	
				   case GENERE:
					   campi[i].setValore(Utility.leggiStringa(""));
				      break;
				   case FASCIA_DI_ETA:
					   FasciaDiEta fascia = new FasciaDiEta(Utility.leggiIntero("\nEtà min"), Utility.leggiIntero("Età max"));
					   campi[i].setValore(fascia);
					      break;
				}
			}
			if(controlloCompilazione(campi)){
				PartitaDiCalcio unaPartita = new PartitaDiCalcio(Arrays.copyOfRange(campi, 0, 14), Arrays.copyOfRange(campi, 14, 16),mioProfilo);
				listaPartite.add(unaPartita);
				mioProfilo.addEventoCreato(unaPartita);
			} else {
				System.out.println("Non hai compilato alcuni campi obbligatori");
			}
		}
		
	}
	
	public void compilazioneCampiGenerici(Campo [] campi){
		for (int i = 0; i < 14; i++) {
			System.out.print(campi[i].toString());
			switch (i)
			{
			   case NUMERO_PARTECIPANTI:
			   case QUOTA:
			   case TOLLERANZA_PARTECIPANTI:
			      campi[i].setValore(Utility.leggiIntero(""));
			      break;
			   case TITOLO:
			   case LUOGO:
			   case COMPRESO_IN_QUOTA:
			   case NOTE:
				   campi[i].setValore(Utility.leggiStringa(""));
			      break;
			   case TERMINE_ISCRIZIONI:
			   case DATA:
			   case DATA_CONCLUSIVA:
			   case TERMINE_RITIRO_ISCRIZIONE:
				   Boolean formatoDataErrato=false;
				   Data date;
				   do {
				   date = new Data(Utility.leggiIntero("\nGiorno"), Utility.leggiIntero("Mese"), Utility.leggiIntero("Anno"));
				   formatoDataErrato=!date.controlloData();
				   if (formatoDataErrato) System.out.println("Hai inserito una data nel formato errato!");
				   } while(formatoDataErrato);
				   campi[i].setValore(date);
				      break;
			   case ORA:
			   case DURATA:
			   case ORA_CONCLUSIVA:
				   Boolean formatoOraErrato=false;
				   Ora orario;
				   do {
					   orario = new Ora(Utility.leggiIntero("\nOra"), Utility.leggiIntero("Minuti"));
					   formatoOraErrato=!orario.controlloOra();
				   if (formatoOraErrato) System.out.println("Hai inserito un orario nel formato errato!");
				   } while(formatoOraErrato);
				   campi[i].setValore(orario);
				      break;
			}
		}
		
	}
	
	public Boolean controlloCompilazione(Campo [] campi) {
		for (int i = 0; i < NUMERO_CAMPI; i++) {
			if(campi[i].isObbligatorio()) {
				if(campi[i].getValore()==null || campi[i].getValore()=="") return false;
			}
		}
		return true;
		
	}

	public void vediCategorie()
	{
		stampaCategorie();
		int scelta= Utility.sceltaDaLista("Seleziona categoria (0 per tornare alla home)",categorie.length);
		switch(scelta)
		{
			case 1: vediEventi(getEventiDisponibili(PartitaDiCalcio.class));
					scegliEvento(getEventiDisponibili(PartitaDiCalcio.class));
				break;
			case 0: return;
		}
	}
	
	

	public void stampaCategorie()
	{
		for (int i = 0; i < categorie.length; i++) {
			System.out.println(i+1 + ") " + categorie[i]);
		}
	}
	
	private Vector<Categoria> getEventiDisponibili(Class c){
		Vector<Categoria> disponibili = new Vector<Categoria>();
		if(c==PartitaDiCalcio.class) {
			for(Categoria p:listaPartite) 
				if(!mioProfilo.isPartecipante(p) && p.isAperto()) disponibili.add(p);		
		}
		return disponibili;
	}
	
	public void vediEventi(Vector<Categoria> disponibili)
	{
		for(int i=0; i<disponibili.size(); i++) { 
			System.out.println(disponibili.get(i).getNome() + " " + (i+1));
			System.out.println(disponibili.get(i).getDescrizioneCampi());
		}
		
	}
	
	public void scegliEvento(Vector<Categoria> disponibili) {
		int a = Utility.sceltaDaLista("Seleziona partita a cui vuoi aderire (0 per uscire):", disponibili.size());
		
			if(a==0) return;
			else{
				partecipaEvento(disponibili.get(a));
			}	
		
	}
	
	private void visualizzaSpazioPersonale() {
		boolean fine=false;
		do {
			int i = Utility.scegli("SPAZIO PERSONALE",vociSpazioPersonale,"Seleziona una voce",4);
			switch(i) {
				case 0:fine=true;
					break;
				case 1:
					gestioneEventiCreati();
					fine=true;
					break;
				case 2:
					gestioneEventiPrenotati();
					fine=true;
					break;
				case 3:
					gestioneNotifiche();
					fine=true;
					break;
				default: System.out.println("Scelta non valida!");
					break;
			
				}
		}while(!fine);
	}
		
	private void gestioneEventiCreati() {
		int a;
		do {
			if(mioProfilo.hasEventiCreati()) { 
				mioProfilo.stampaEventiCreati();
				a = Utility.sceltaDaLista("Seleziona evento che vuoi ritirare (0 per uscire):", mioProfilo.getEventiCreati().size());
				if(a==0) return;
				else { 
					Categoria ritirato = mioProfilo.getEventiCreati().get(a-1);
					if(ritirato.isRitirabile(dataOdierna)) {
						mioProfilo.deleteEventoCreato(a-1); 
						ritirato.ritiraEvento();
					}else System.out.println("Non è più possibile ritirare quest'evento");
				}
			}else {
				System.out.println("Non hai creato nessun evento!");
				a=0;
			}
		}while(a!=0);
	}

	private void gestioneEventiPrenotati() {
		int a;
		do {
			if(mioProfilo.hasEventiPrenotati()) { 
				mioProfilo.stampaEventiPrenotati();
				a = Utility.sceltaDaLista("Seleziona evento a cui vuoi disiscriverti (0 per uscire):", mioProfilo.getEventiPrenotati().size());
				if(a==0) return;
				else {
					Categoria rimosso = mioProfilo.getEventiPrenotati().get(a-1);
					if(rimosso.isRitirabile(dataOdierna)){
						mioProfilo.deleteEventoPrenotato(a-1); 				
						rimosso.removePartecipante(mioProfilo);
					}else System.out.println("Non è più possibile disiscriversi da quest'evento");
				}
			}else {
				System.out.println("Non sei iscritto a nessun evento!");
				a=0;
			}
		}while(a!=0);
	}

	public void gestioneNotifiche() {
		int a;
		if(mioProfilo.noNotifiche()) {
			System.out.println("NON hai notifiche da visualizzare");
		}else {
			mioProfilo.stampaNotifiche();
			do {
				a = Utility.sceltaDaLista("Seleziona notifica che vuoi eliminare (0 per uscire):", mioProfilo.getNumeroNotifiche());
				if(a==0) return;
				else{
					mioProfilo.deleteNotifica(a-1); 
				}
			}while(a!=0);
		}
	}
	
	private void partecipaEvento(Categoria evento) {
		evento.aggiungiPartecipante(mioProfilo);
		mioProfilo.addEventoPrenotato(evento);
		controlloEventi();
	}
	
	
	
	public void assegnaEvento(Campo[] campi) 
	{
		campi[TITOLO]= new Campo<String>("Titolo","Titolo dell'evento",false);
		campi[NUMERO_PARTECIPANTI]=new Campo<Integer>("Numero partecipanti","Indica il numero massimo di partecipanti",true);
		campi[TERMINE_ISCRIZIONI]=new Campo<Data>("Data termine iscrizione","Indica la data limite entro cui iscriversi",true);
		campi[LUOGO]=new Campo<String>("Luogo","Indica il luogo dell'evento",true);
		campi[DATA]=new Campo<Data>("Data","Indica la data di svolgimento dell'evento",true);
		campi[ORA]=new Campo<Ora>("Ora","Indica l'ora di inizio dell'evento",true);
		campi[DURATA]=new Campo<Ora>("Durata","Indica la durata dell'evento",false);
		campi[QUOTA]=new Campo<Integer>("Quota iscrizione","Indica la spesa da sostenere per partecipare all'evento",true);
		campi[COMPRESO_IN_QUOTA]=new Campo<String>("Compreso in quota","Indica le voci di spesa comprese nella quota",false);
		campi[DATA_CONCLUSIVA]=new Campo<Data>("Data conclusiva","Indica la data di conclusione dell'evento",false);
		campi[ORA_CONCLUSIVA]=new Campo<Ora>("Ora conclusiva","Indica l'ora conclusiva dell'evento",false);
		campi[NOTE]=new Campo<String>("Note","Informazioni aggiuntive",false);		
		campi[TOLLERANZA_PARTECIPANTI]=new Campo<Integer>("Tolleranza numero di partecipanti","Indica quanti partecipanti siano accettabili in esubero a numero di partecipanti",false);
		campi[TERMINE_RITIRO_ISCRIZIONE]=new Campo<Data>("Termine ultimo di ritiro iscrizione","Indica la data entro cui ogni fruitore può cancellare la sua iscrizione",false);
	}
	
	public void assegnaPartitaDiCalcio(Campo[] campi) {
		
		assegnaEvento(campi);
		campi[GENERE]=new Campo<String>("Genere","Indica il genere dei giocatori",true);
		campi[FASCIA_DI_ETA]=new Campo<FasciaDiEta>("Fascia di età","Indica la fascia di età dei giocatori",true);
		
	}
	
	public void esciEsalva() throws IOException
	{
		System.out.println("Salvataggio...");
		
		ObjectOutputStream writerPartite=new ObjectOutputStream(new FileOutputStream(new File(pathPartite)));
		writerPartite.writeObject(listaPartite);
		writerPartite.close();
		
		ObjectOutputStream writerProfilo=new ObjectOutputStream(new FileOutputStream(new File(pathProfilo)));
		writerProfilo.writeObject(mioProfilo);
		writerProfilo.close();
	}

		
}
