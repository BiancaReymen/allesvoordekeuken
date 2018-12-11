package be.vdab.allesvoordekeuken.valueobjects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

public class KortingTest {

	private Korting korting1, nogEensKorting1, korting2;
	
	@Before
	public void before() {
		korting1 = new Korting(5, BigDecimal.valueOf(2,5));
		nogEensKorting1 = new Korting(5, BigDecimal.valueOf(2,5));
		korting2 = new Korting(10, BigDecimal.valueOf(4));
	}
	@Test
	public void kortingenZijnGelijkAlsHunAantallenGelijkZijn(){
		assertEquals(korting1, nogEensKorting1);
	}
	
	@Test
	public void kortingenZijnVerschillendAlsHunAantallenVerschillendZijn(){
		assertNotEquals(korting1, korting2);
	}
	@Test
	public void eenKortingenVerschiltVanNull(){
		assertNotEquals(korting1, null);
	}
	@Test
	public void eenKortingenVerschiltVanEenAnderTypeObject(){
		assertNotEquals(korting1, "");
	}
	@Test
	public void gelijkeKortingenGevenDezelfdeHashCode(){
		assertEquals(korting1.hashCode(), nogEensKorting1.hashCode());
	}
}
