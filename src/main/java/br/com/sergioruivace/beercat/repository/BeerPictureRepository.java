package br.com.sergioruivace.beercat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sergioruivace.beercat.model.BeerPicture;

public interface BeerPictureRepository  extends JpaRepository<BeerPicture, Long>{

}
