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
	protected static final int QUOTA=7;
	private static final int COMPRESO_IN_QUOTA=8;
	private static final int DATA_CONCLUSIVA=9;
	private static final int ORA_CONCLUSIVA=10;
	private static final int NOTE=11;
	private static final int TOLLERANZA_PARTECIPANTI=12;
	private static final int TERMINE_RITIRO_ISCRIZIONE=13;
	
	private static final String lineSeparator="\n";
	
	private String nome;
	private String descrizione;
	protected boolean chiuso;
	protected boolean fallito;
	protected boolean concluso;
	protected boolean ritirato;
	protected boolean scaduto;
	protected boolean postiEsauriti;
	protected Campo[] campiBase;
	private int partecipantiAttuali;
	private Vector<SpazioPersonale> listaPartecipanti;
	private SpazioPersonale creatore;
	
	
	public Categoria(String _nome, String _descrizione, Campo[] _campiBase, SpazioPersonale _creatore) {
		campiBase = new Campo[NUMERO_CAMPI];
		campiBase = _campiBase;
		nome=_nome;
		descrizione=_descrizione;
		partecipantiAttuali=1;
		listaPartecipanti= new Vector<SpazioPersonale>();
		chiuso=false;
		fallito=false;
		concluso=false;
		ritirato=false;
		creatore=_creatore;
	}

	/**
	 * @return the listaPartecipanti
	 */
	public Vector<SpazioPersonale> getListaPartecipanti() {
		return listaPartecipanti;
	}

	public String getNome() {
		return nome;
	}
	
	public String getStato() {
		if(chiuso) return "Chiuso";
		if(ritirato) return "Ritirato";
		if(concluso) return "Concluso";
		if(fallito) return "Fallito";
		return "Aperto";
	}

	public String getDescrizione() {
		return descrizione;
	}

	
	public Campo[] getCampiBase() {
		return campiBase;
	}

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
	
	public Integer getNumeroMassimoPartecipanti(){
		Integer numeroPartecipanti=(Integer)campiBase[NUMERO_PARTECIPANTI].getValore();
		Integer tolleranza;
		Integer temp=(Integer)campiBase[TOLLERANZA_PARTECIPANTI].getValore();
		if(temp==null)
			tolleranza= new Integer(0);
		else tolleranza=temp;
		return numeroPartecipanti+tolleranza;
	}
	
	public void aggiornaStato(Data dataOdierna) {
		if(isAperto()) {
			checkFallimento(dataOdierna);
			checkConclusione(dataOdierna);
			checkChiusura(dataOdierna);
		}
	}
	
	
	public void checkFallimento(Data dataOdierna) {
		Data dataScadenza = (Data) campiBase[TERMINE_ISCRIZIONI].getValore();	
		if (dataScadenza.isPrecedente(dataOdierna) && (partecipantiAttuali < (int) campiBase[NUMERO_PARTECIPANTI].getValore())) {
			fallito=true;
			for (SpazioPersonale profilo : listaPartecipanti) {
				profilo.addNotifica(infoFallimento());
				
			}
		}
	}
	
	public void checkConclusione(Data dataOdierna) {
		Data dataConclusiva;
		if (campiBase[DATA_CONCLUSIVA].getValore()!=null) {
			dataConclusiva = (Data) campiBase[DATA_CONCLUSIVA].getValore();
		} else {
			dataConclusiva = (Data) campiBase[DATA].getValore();
		}
		
		if (dataConclusiva.isPrecedente(dataOdierna) && !fallito) {
			concluso=true;
		}
	}

	
	protected void checkChiusura(Data dataOdierna) {
		Integer numeroPartecipanti=(Integer)campiBase[NUMERO_PARTECIPANTI].getValore();
		Data termineIscrizioni = (Data) campiBase[TERMINE_ISCRIZIONI].getValore();
		scaduto=termineIscrizioni.isPrecedente(dataOdierna);
		
		boolean nonPiuIscrivibile=partecipantiAttuali>=numeroPartecipanti &&  partecipantiAttuali<=getNumeroMassimoPartecipanti() && scaduto;
		boolean postiEsauriti= !scaduto && !(isRitirabile(dataOdierna)) && (partecipantiAttuali==getNumeroMassimoPartecipanti());
		
		if( nonPiuIscrivibile || postiEsauriti ) {
			chiuso=true;	
			for (SpazioPersonale profilo : listaPartecipanti) {
					profilo.addNotifica(infoChiusura());
			}
		}
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
			s.append(infoPagamento());
		return s.toString();	
	}

	protected String infoPagamento() {
		StringBuffer s = new StringBuffer();
			s.append("Importo iscrizione: "+ campiBase[QUOTA].getValore());
			s.append(lineSeparator);
		return s.toString();
	}
	
	public boolean isAperto() {
		return ((!chiuso) && (!fallito) && (!concluso) && (!ritirato));
	}
	
	public boolean isIscrivibile(){
		return isAperto() && partecipantiAttuali<getNumeroMassimoPartecipanti();
	}
	
	public boolean isRitirabile(Data dataOdierna){
		Data termineIscrizioni = (Data)campiBase[TERMINE_ISCRIZIONI].getValore();
		Data temp=(Data)campiBase[TERMINE_RITIRO_ISCRIZIONE].getValore();
		Data termineRitiroIscrizioni;
		if(temp==null)
			termineRitiroIscrizioni=termineIscrizioni;
		else termineRitiroIscrizioni = temp;
		
		return (dataOdierna.isPrecedenteOppureUguale(termineRitiroIscrizioni));
	}
	
	public void ritiraEvento() {
		System.out.println("Evento ritirato con successo! \n");
		ritirato=true;
		for (SpazioPersonale profilo : listaPartecipanti) {
			profilo.addNotifica(infoRitiro());
		}
	}
	
	public String getCampiCompilati() {
		StringBuffer s = new StringBuffer();
		for(int i=0; i < campiBase.length; i++) { 
			s.append("   " + campiBase[i].toStringValore());
			s.append(lineSeparator);
		}
		return s.toString();
		
	}
}
