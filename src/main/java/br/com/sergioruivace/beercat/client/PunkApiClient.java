package br.com.sergioruivace.beercat.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.sergioruivace.beercat.dto.ExternalManufacturerDTO;

@FeignClient(name = "punkapi", url = "https://api.punkapi.com/v2")
public interface PunkApiClient {
	
	@RequestMapping(path = "/beers", method = RequestMethod.GET)
	List<ExternalManufacturerDTO> list(@RequestParam(value = "page", required = false) int page, 
			@RequestParam(value = "per_page", required = false) int per_page, 
			@RequestParam(value = "beer_name", required = false) String beer_name);
	
	@RequestMapping(path = "/beers/{id}", method = RequestMethod.GET)
	ExternalManufacturerDTO detail(@PathVariable("id") int id);

}
