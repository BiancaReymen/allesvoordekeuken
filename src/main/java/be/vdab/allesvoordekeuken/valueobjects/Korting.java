package be.vdab.allesvoordekeuken.valueobjects;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Embeddable;

@Embeddable
public class Korting implements Serializable {

	private final static long serialVersionUID = 1L;
	
	private int vanafAantal;
	private BigDecimal percentage;
	public Korting(int vanafAantal, BigDecimal percentage) {
		super();
		this.vanafAantal = vanafAantal;
		this.percentage = percentage;
	}
	protected Korting() {
		super();
	}
	public int getVanafAantal() {
		return vanafAantal;
	}
	public BigDecimal getPercentage() {
		return percentage;
	}
	@Override
	public boolean equals (Object obj) {
		if (! (obj instanceof Korting)) {
			return false;
		}
		Korting korting = (Korting) obj;
		return vanafAantal == korting.vanafAantal;
	}
	@Override
	public int hashCode() {
		return vanafAantal;
	}
}
