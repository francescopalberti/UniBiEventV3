package it.unibs;

import java.io.Serializable;

public class Campo<T> implements Serializable {
	
	private String nome;
	private String descrizione;
	private boolean obbligatorietà;
	private T valore;
	
	public Campo(String _nome, String _descrizione, boolean _obbligatorietà){
		
		nome=_nome;
		descrizione=_descrizione;
		obbligatorietà=_obbligatorietà;
		valore=null;
		
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public boolean isObbligatorio() {
		return obbligatorietà;
	}

	public void setObbligatorietà(boolean obbligatorietà) {
		this.obbligatorietà = obbligatorietà;
	}

	public T getValore() {
		return valore;
	}

	public void setValore(T valore) {
		this.valore = valore;
	}
	
	public String toStringValore() {
		return nome + ": " + valore;
	}
	
	public String toString() {
		String S;
		if (obbligatorietà) {
			S = nome + "* : " + descrizione;
			}
		else {
			S = nome + " : " + descrizione;
		}
		return S;
	}
}