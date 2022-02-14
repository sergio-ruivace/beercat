package br.com.sergioruivace.beercat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.sergioruivace.beercat.model.Manufacturer;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {

	@Query("SELECT m FROM Manufacturer m"
			+ " WHERE m.name LIKE %?1%"
            + " OR m.nationality LIKE %?1%")
	Page<Manufacturer> findAll(String search, Pageable pageable);

}
