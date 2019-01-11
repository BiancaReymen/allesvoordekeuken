package be.vdab.allesvoordekeuken.entities;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("NF")
public class NonFoodArtikel extends Artikel {
	private static final long serialVersionUID = 1L;
	
	private int garantie;

	public NonFoodArtikel(int garantie) {
		super();
		this.garantie = garantie;
	}

	public NonFoodArtikel() {
		super();
	}

	public int getGarantie() {
		return garantie;
	}

	public NonFoodArtikel(String naam, BigDecimal aankoopprijs, BigDecimal verkoopprijs, int garantie, ArtikelGroep artikelGroep) {
		super(naam, aankoopprijs, verkoopprijs, artikelGroep);
		this.garantie = garantie;
	}
	
	

}
