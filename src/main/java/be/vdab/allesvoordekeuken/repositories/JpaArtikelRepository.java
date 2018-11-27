package be.vdab.allesvoordekeuken.repositories;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import be.vdab.allesvoordekeuken.entities.Artikel;

@Repository
public class JpaArtikelRepository implements ArtikelRepository {
	
	private final EntityManager manager;
	JpaArtikelRepository(EntityManager manager){
		this.manager = manager;
	}
	
	@Override
	public Optional<Artikel> read(long id) {
		return Optional.ofNullable(manager.find(Artikel.class, id));
	}
	
	
}
