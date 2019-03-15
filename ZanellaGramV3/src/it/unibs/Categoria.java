package it.unibs;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

public class Categoria implements Serializable {
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
	
	private static final String lineSeparator="\n";
	
	private String nome;
	private String descrizione;
	private Boolean chiuso;
	private Boolean fallito;
	private Boolean concluso;
	private Campo[] campiBase;
	private int partecipantiAttuali;
	private Vector<SpazioPersonale> listaPartecipanti;
	
	
	
	
	public Categoria(String _nome, String _descrizione, Campo[] _campiBase) {
		campiBase = new Campo[12];
		campiBase = _campiBase;
		nome=_nome;
		descrizione=_descrizione;
		partecipantiAttuali=1;
		chiuso=false;
		fallito=false;
		concluso=false;
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

	private void controlloChiusura() {
		if (partecipantiAttuali>=(int)campiBase[NUMERO_PARTECIPANTI].getValore()) {
			chiuso=true;	
			for (SpazioPersonale profilo : listaPartecipanti) {
				profilo.addNotifica(infoChiusura());
			}
		}
		
	}
	
	public boolean aggiornaStato(Data dataOdierna) {
		controlloChiusura();
		
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
		return concluso || fallito || chiuso;
	}
	
	private String infoFallimento() {
		StringBuffer s = new StringBuffer();
		s.append("L'evento "+ campiBase[TITOLO].getValore() +" è fallito. ");
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
		return (!chiuso && !fallito && !concluso);
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
