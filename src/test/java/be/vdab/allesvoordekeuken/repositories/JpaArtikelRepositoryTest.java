package be.vdab.allesvoordekeuken.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
	@Autowired
	private JpaArtikelRepository repository;
	private long idVanTestMixer()  {
		return super.jdbcTemplate.queryForObject("select id from artikels where naam = 'mixer'", Long.class);
	}
	@Test
	public void read() {
		Artikel artikel = repository.read(idVanTestMixer()).get();
		assertEquals("mixer", artikel.getNaam());
	}
	@Test
	public void readOnbestaandArtikel() {
		assertFalse(repository.read(-1).isPresent());
	}
}
