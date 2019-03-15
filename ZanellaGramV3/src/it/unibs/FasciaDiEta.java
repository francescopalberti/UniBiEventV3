package it.unibs;

import java.io.Serializable;

//
public class FasciaDiEta implements Serializable {
	
	private Integer et‡Minima;
	private Integer et‡Massima;
	
	public FasciaDiEta(Integer min, Integer max) {
		if (min==null||max==null) {
			min=null;
			max=null;
		} else {
			if(min<max){
				this.et‡Minima=min;
			    this.et‡Massima=max;
			}
			else {
				this.et‡Massima=min;
				this.et‡Minima=max;
			}
		}
	}

	public Integer getMin() {
		return et‡Minima;
	}

	public void setMin(Integer et‡Minima) {
		this.et‡Minima = et‡Minima;
	}

	public Integer getMax() {
		return et‡Massima;
	}

	public void setMax(Integer et‡Massima) {
		this.et‡Massima = et‡Massima;
	}
	
	public String toString() {
		return et‡Minima + " - " + et‡Massima;
	}

}
