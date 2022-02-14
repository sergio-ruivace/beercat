package br.com.sergioruivace.beercat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sergioruivace.beercat.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	void deleteByManufacturerId(Long id);

}
