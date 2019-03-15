package it.unibs;

import java.io.Serializable;
import java.util.Vector;

public class SpazioPersonale implements Serializable {
	private Vector<Categoria> eventi;
	private Vector<String> notifiche;
	
	
	public SpazioPersonale() {
		this.eventi = new Vector<Categoria>();
		this.notifiche = new Vector<String>();
	}
	
	
	/**
	 * @param evento da aggiungere
	 */
	public void addEvento(Categoria evento) {
		eventi.add(evento);
	}
	
	public Vector<Categoria> getEventi(){
		return eventi;
	}
	
	/**
	 * @return false se non c'è l'evento nel vector
	 */
	public boolean deleteEvento(Categoria evento) {
		return eventi.remove(evento);
	}
	
	
	/**
	 * @param notifica da aggiungere
	 */
	public void addNotifica(String notifica) {
		notifiche.add(notifica);
	}
	
	/**
	 * @return false se non c'è la notifica nel vector
	 */
	public boolean deleteNotifica(String notifica) {
		return notifiche.remove(notifica);
	}
	
	public String deleteNotifica(int pos) {
		return notifiche.remove(pos);
	}
	
	public boolean noNotifiche() {
		return notifiche.isEmpty();
	}
	
	
	public void stampaNotifiche() {
		System.out.println("NOTIFICHE:");
		int i=0;
		for (String notifica : notifiche) {
			System.out.println(i+1 + ") " + notifica);
			i++;
		}
	}
	
	public void stampaIMieiEventi() {
		System.out.println("I MIEI EVENTI:");
		for(int i=0; i< eventi.size(); i++) { 
			System.out.println(eventi.get(i).getNome() + (i+1));
			System.out.println(eventi.get(i).getDescrizioneCampi());
		}
	}

	public int getNumeroNotifiche() {
		return notifiche.size();
	}

	public boolean hasEventi() {
		return eventi!=null;
		
	}

	public boolean isPartecipante(Categoria evento) {
		for(Categoria c: eventi)
			if(evento.equals(c)) return true;
		return false;
	}
}
