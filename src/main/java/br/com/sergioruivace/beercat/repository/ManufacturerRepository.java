package br.com.sergioruivace.beercat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sergioruivace.beercat.model.Manufacturer;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {

}
