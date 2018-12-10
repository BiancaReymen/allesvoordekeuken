package be.vdab.allesvoordekeuken.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import be.vdab.allesvoordekeuken.entities.Artikel;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql("/insertArtikel.sql")
@Import(JpaArtikelRepository.class)
public class JpaArtikelRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
	private static final String ARTIKEL = "artikels";
	private Artikel artikel;
	@Autowired
	private JpaArtikelRepository repository;
	private long idVanTestArtikel()  {
		return super.jdbcTemplate.queryForObject("select id from artikels where naam = 'test'", Long.class);
	}
	@Before
	public void before() {
		artikel = new Artikel("test",BigDecimal.TEN, BigDecimal.valueOf(100));
	}
	@Test
	public void read() {
		Artikel artikel = repository.read(idVanTestArtikel()).get();
		assertEquals("test", artikel.getNaam());
	}
	@Test
	public void readOnbestaandArtikel() {
		assertFalse(repository.read(-1).isPresent());
	}
	@Test
	public void create() {
		int aantalArtikels = super.countRowsInTable(ARTIKEL);
		repository.create(artikel);
		assertEquals(aantalArtikels + 1 , super.countRowsInTable(ARTIKEL));
		assertNotEquals(0, artikel.getId());
		assertEquals(1, super.countRowsInTableWhere(ARTIKEL, "id=" + artikel.getId()));
		
	}
	@Test
	public void findByName(){
		String woord = "es";
		List<Artikel> artikels = repository.findByNameContains(woord);
		long aantalArtikels = super.jdbcTemplate.queryForObject(
				"select count(*) from artikels where naam like '%es%'", Long.class);
		assertEquals(aantalArtikels, artikels.size());
		String vorigeNaam = "";
		for (Artikel artikel : artikels) {
			String naam =  artikel.getNaam();
			assertTrue(naam.toLowerCase().contains("es"));
			assertTrue(naam.compareToIgnoreCase(vorigeNaam) >= 0);
			vorigeNaam = naam;
		}
	}	
	@Test
	public void algemenePrijsVerhoging() {
		int aantalAangepast = repository.algemenePrijsVerhoging(BigDecimal.TEN);
		assertEquals(super.countRowsInTable(ARTIKEL), aantalAangepast);
		BigDecimal nieuwePrijs = super.jdbcTemplate.queryForObject(
				"select verkoopprijs from artikels where id=?", BigDecimal.class, idVanTestArtikel());
		assertEquals(0, BigDecimal.valueOf(132).compareTo(nieuwePrijs));		
	}
									
	
		
}

