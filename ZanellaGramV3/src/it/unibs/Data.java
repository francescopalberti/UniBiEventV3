package it.unibs;

import java.io.Serializable;

public class Data implements Serializable {
	private Integer giorno, mese, anno;

public Data(Integer gg, Integer mm, Integer yy) {
		this.giorno = gg;
		this.mese = mm;
		this.anno = yy;
	}
	
	/**
	 * @return the giorno
	 */
	public Integer getGiorno() {
		return giorno;
	}

	/**
	 * @return the mese
	 */
	public Integer getMese() {
		return mese;
	}

	/**
	 * @return the anno
	 */
	public Integer getAnno() {
		return anno;
	}

	//Con date uguali ritorna FALSE
	public Boolean isPrecedente(Data unaData) {
		if (anno<unaData.getAnno()) return true;
		else if (anno>unaData.getAnno()) return false;
			if (mese<unaData.getMese()) return true;
			else if (mese>unaData.getMese()) return false;
				if (giorno<unaData.getGiorno()) return true;
				else if(giorno>unaData.getGiorno()) return false;
		
		return false;
	}
	
	//Con date uguali ritorna TRUE
	public Boolean isPrecedenteOppureUguale(Data unaData) {
		if (anno<unaData.getAnno()) return true;
		else if (anno>unaData.getAnno()) return false;
			if (mese<unaData.getMese()) return true;
			else if (mese>unaData.getMese()) return false;
				if (giorno<=unaData.getGiorno()) return true;
				else if(giorno>unaData.getGiorno()) return false;
		
		return false;
	}

	@Override
	public String toString() {
		return giorno + "/" + mese + "/" + anno;
	}
	
	public boolean controlloData() {
		if(anno==null && mese==null && giorno==null) return true;
		if(anno==null || mese==null || giorno==null) return false;
		switch (mese) {
			case 2:
				if(giorno>29) return false;
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				if(giorno>30) return false;
				break;
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				if (giorno>31) return false;
				break;
			default:
				return false;
			
		}
		return true;
	}
	
	public boolean isEmpty(){
		return giorno==null || mese==null || anno==null;
	}
	
}
