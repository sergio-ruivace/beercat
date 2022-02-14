package br.com.sergioruivace.beercat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.sergioruivace.beercat.model.Beer;

public interface BeerRepository extends JpaRepository<Beer, Long>{
	
	@Query("SELECT b FROM Beer b JOIN b.manufacturer m"
			+ " WHERE b.name LIKE %?1%"
            + " OR b.description LIKE %?1%"
            + " OR CONCAT(b.type, '') LIKE %?1%"
            + " OR CONCAT(b.graduation, '') LIKE %?1%"
            + " OR m.name LIKE %?1%"
            + " OR m.nationality LIKE %?1%")
    Page<Beer> findAll(String search, Pageable pageable);

	void deleteByManufacturerId(Long id);

}
