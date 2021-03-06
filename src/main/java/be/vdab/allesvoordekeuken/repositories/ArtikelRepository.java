package be.vdab.allesvoordekeuken.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import be.vdab.allesvoordekeuken.entities.Artikel;

public interface ArtikelRepository {
	
	Optional<Artikel> read(long id);
	void create(Artikel artikel);
	List<Artikel> findByNameContains(String woord);
	int algemenePrijsVerhoging(BigDecimal percentage);
		
	}

