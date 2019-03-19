package it.unibs;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

public class Categoria implements Serializable {
	private static final int NUMERO_CAMPI=14;
	
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
	
	private static final String lineSeparator="\n";
	
	private String nome;
	private String descrizione;
	private Boolean chiuso;
	private Boolean fallito;
	private Boolean concluso;
	private Boolean ritirato;
	private Campo[] campiBase;
	private int partecipantiAttuali;
	private Vector<SpazioPersonale> listaPartecipanti;
	private SpazioPersonale creatore;
	
	
	public Categoria(String _nome, String _descrizione, Campo[] _campiBase, SpazioPersonale _creatore) {
		campiBase = new Campo[NUMERO_CAMPI];
		campiBase = _campiBase;
		nome=_nome;
		descrizione=_descrizione;
		partecipantiAttuali=1;
		chiuso=false;
		fallito=false;
		concluso=false;
		ritirato=false;
		creatore=_creatore;
	}

	public String getNome() {
		return nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	
	public Campo[] getCampiBase() {
		return campiBase;
	}

	/*public void setCampiBase(Campo[] campiBase) {
		this.campiBase = campiBase;
	}*/
	
	public String toString() {
		
		String S=nome + ": " + descrizione;
		
		return S;
	}
	
	public void aggiungiPartecipante(SpazioPersonale partecipante) {
		partecipantiAttuali++;
		listaPartecipanti.add(partecipante);	
	}
	
	public void removePartecipante(SpazioPersonale rimosso) {
		partecipantiAttuali--;
		listaPartecipanti.remove(rimosso);
	}

	private void controlloChiusura(Data dataOdierna) {
		Integer numeroPartecipanti=(Integer)campiBase[NUMERO_PARTECIPANTI].getValore();
		Integer tolleranza=(Integer)campiBase[TOLLERANZA_PARTECIPANTI].getValore();
		Data dataScadenza = (Data) campiBase[TERMINE_ISCRIZIONI].getValore();
		boolean scaduto=dataScadenza.isPrecedente(dataOdierna);
		boolean condizione1=partecipantiAttuali>=numeroPartecipanti &&  partecipantiAttuali<=numeroPartecipanti+tolleranza && scaduto;
		Data dataRitiro = (Data) campiBase[TERMINE_RITIRO_ISCRIZIONE].getValore();
		boolean ritirabile = dataRitiro.isPrecedente(dataOdierna);
		boolean condizione2= !scaduto && ritirabile && (partecipantiAttuali==numeroPartecipanti+tolleranza);
		
		if (condizione1 || condizione2) {
			chiuso=true;	
			for (SpazioPersonale profilo : listaPartecipanti) {
				profilo.addNotifica(infoChiusura());
			}
		}
		
	}
	
	public boolean aggiornaStato(Data dataOdierna) {
		controlloChiusura(dataOdierna);
		
		Data dataScadenza = (Data) campiBase[TERMINE_ISCRIZIONI].getValore();
		if (dataScadenza.isPrecedente(dataOdierna) && (partecipantiAttuali < (int) campiBase[NUMERO_PARTECIPANTI].getValore())) {
			fallito=true;
			for (SpazioPersonale profilo : listaPartecipanti) {
				profilo.addNotifica(infoFallimento());
			}
		}
		
		Data dataConclusiva;
		if (campiBase[DATA_CONCLUSIVA].getValore()!=null) {
			dataConclusiva = (Data) campiBase[DATA_CONCLUSIVA].getValore();
		} else {
			dataConclusiva = (Data) campiBase[DATA].getValore();
		}
		
		if (dataConclusiva.isPrecedente(dataOdierna) && !fallito) {
			concluso=true;
		}
		return concluso || fallito || chiuso || ritirato;
	}
	
	private String infoFallimento() {
		StringBuffer s = new StringBuffer();
		s.append("L'evento "+ campiBase[TITOLO].getValore() +" è fallito. ");
		s.append(lineSeparator);
		return s.toString();
	}
	
	public String infoRitiro() {
		StringBuffer s = new StringBuffer();
		s.append("L'evento "+ campiBase[TITOLO].getValore() +" è stato ritirato. ");
		s.append(lineSeparator);
		return s.toString();
	}

	public String infoChiusura() {
		StringBuffer s = new StringBuffer();
		s.append("L'evento "+ campiBase[TITOLO].getValore() +" si svolgerà.");
		s.append(lineSeparator);
		s.append("Data: "+ campiBase[DATA].getValore());
		s.append(lineSeparator);
		s.append("Ora: "+ campiBase[ORA].getValore());
		s.append(lineSeparator);
		s.append("Luogo: "+ campiBase[LUOGO].getValore());
		s.append(lineSeparator);
		s.append("Importo dovuto: "+ campiBase[QUOTA].getValore());
		s.append(lineSeparator);
		return s.toString();	
	}
	
	public boolean isAperto() {
		return (!chiuso && !fallito && !concluso && !ritirato);
	}
	
	public boolean isRitirabile(Data dataOdierna){
		Data termineIscrizioni = (Data)campiBase[TERMINE_ISCRIZIONI].getValore();
		Data termineRitiroIscrizioni = (Data)campiBase[TERMINE_RITIRO_ISCRIZIONE].getValore();
		if(termineRitiroIscrizioni==null)
			return dataOdierna.isPrecedente(termineIscrizioni);
		else return dataOdierna.isPrecedente(termineRitiroIscrizioni);
	}
	
	public void ritiraEvento() {
		ritirato=true;
		for (SpazioPersonale profilo : listaPartecipanti) {
			profilo.addNotifica(infoRitiro());
		}
	}
	
	public String getDescrizioneCampi() {
		StringBuffer s = new StringBuffer();
		for(int i=0; i < campiBase.length; i++) { 
			s.append("   " + campiBase[i].toStringValore());
			s.append(lineSeparator);
		}
		return s.toString();
		
	}
}
