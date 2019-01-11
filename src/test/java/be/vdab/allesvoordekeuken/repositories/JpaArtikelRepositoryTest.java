package be.vdab.allesvoordekeuken.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;

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
import be.vdab.allesvoordekeuken.entities.ArtikelGroep;
import be.vdab.allesvoordekeuken.entities.FoodArtikel;
import be.vdab.allesvoordekeuken.entities.NonFoodArtikel;
import be.vdab.allesvoordekeuken.valueobjects.Korting;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql("/insertArtikelGroep.sql")
@Sql("/insertArtikel.sql")
@Import(JpaArtikelRepository.class)
public class JpaArtikelRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
	private static final String ARTIKEL = "artikels";
	//private Artikel artikel;
	private FoodArtikel foodArtikel;
	private NonFoodArtikel nonFoodArtikel;
	@Autowired
	private JpaArtikelRepository repository;

	private ArtikelGroep groep;
	@Autowired
	private EntityManager manager;

	@Before
	public void before() {
		groep = new ArtikelGroep("test");
		foodArtikel = new FoodArtikel("testFood", BigDecimal.valueOf(1), BigDecimal.valueOf(10), 7, groep);
		nonFoodArtikel = new NonFoodArtikel("testNonFood", BigDecimal.valueOf(1), BigDecimal.valueOf(10), 30, groep);
	}

	private long idVanTestFoodArtikel() {
		return super.jdbcTemplate.queryForObject("select id from artikels where naam = 'testfood'", Long.class);
	}

	private long idVanTestNonFoodArtikel() {
		return super.jdbcTemplate.queryForObject("select id from artikels where naam = 'testnonfood'", Long.class);
	}

	@Test
	public void readFoodArtikel() {
		FoodArtikel artikel = (FoodArtikel) repository.read(idVanTestFoodArtikel()).get();
		assertEquals("testfood", artikel.getNaam());
	}

	@Test
	public void readNonFoodArtikel() {
		NonFoodArtikel artikel = (NonFoodArtikel) repository.read(idVanTestNonFoodArtikel()).get();
		assertEquals("testnonfood", artikel.getNaam());
	}

	@Test
	public void readOnbestaandArtikel() {
		assertFalse(repository.read(-1).isPresent());
	}

	@Test
	public void createFoodArtikel() {
		manager.persist(groep);
		int aantalFoodArtikels = super.countRowsInTableWhere(ARTIKEL, "soort='F'");
		repository.create(foodArtikel);
		assertEquals(aantalFoodArtikels + 1, super.countRowsInTableWhere(ARTIKEL, "soort='F'"));
		assertEquals(1, super.countRowsInTableWhere(ARTIKEL, "id=" + foodArtikel.getId()));
	}

	@Test
	public void createNonFoodArtikel() {
		manager.persist(groep);
		int aantalNonFoodArtikels = super.countRowsInTableWhere(ARTIKEL, "soort='NF'");
		repository.create(nonFoodArtikel);
		assertEquals(aantalNonFoodArtikels + 1, super.countRowsInTableWhere(ARTIKEL, "soort='NF'"));
		assertEquals(1, super.countRowsInTableWhere(ARTIKEL, "id=" + nonFoodArtikel.getId()));
	}

	@Test
	public void findByNameContains() {
		String woord = "es";
		List<Artikel> artikels = repository.findByNameContains(woord);
		manager.clear();
		long aantalArtikels = super.jdbcTemplate.queryForObject("select count(*) from artikels where naam like '%es%'",
				Long.class);
		assertEquals(aantalArtikels, artikels.size());
		String vorigeNaam = "";
		for (Artikel artikel : artikels) {
			String naam = artikel.getNaam();
			assertTrue(naam.toLowerCase().contains("es"));
			assertTrue(naam.compareToIgnoreCase(vorigeNaam) >= 0);
			vorigeNaam = naam;
			System.out.println(artikel.getNaam() + ' ' + artikel.getArtikelGroep().getNaam());
		}
	}

	@Test
	public void algemenePrijsVerhoging() {
		int aantalAangepast = repository.algemenePrijsVerhoging(BigDecimal.TEN);
		assertEquals(super.countRowsInTable(ARTIKEL), aantalAangepast);
		BigDecimal nieuwePrijs = super.jdbcTemplate.queryForObject("select verkoopprijs from artikels where id=?",
				BigDecimal.class, idVanTestFoodArtikel());
		assertEquals(0, BigDecimal.valueOf(132).compareTo(nieuwePrijs));
	}

	@Test
	public void kortingenLezen() {
		Artikel artikel = repository.read(idVanTestFoodArtikel()).get();
		assertEquals(1, artikel.getKortingen().size());
		assertTrue(artikel.getKortingen().contains(new Korting(20, BigDecimal.valueOf(5))));

	}

	@Test
	public void artikelGroepLazyLoaded() {
		Artikel artikel = repository.read(idVanTestFoodArtikel()).get();
		assertEquals("test", artikel.getArtikelGroep().getNaam());
	}
	
}
