package br.com.sergioruivace.beercat.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.sergioruivace.beercat.model.Beer;
import br.com.sergioruivace.beercat.model.Manufacturer;
import lombok.Data;

@Data
public class ExternalManufacturerDTO {
	public static String EXTERNAL_PUNKAPI = "punkapi";
	
    private int id;
    private String name;
    private String description;
    private String image_url;
    private float abv;
    
    
    public Beer convertDto() {
    	//I created a in memory manufacturer because it is an external manufacturer
    	Manufacturer manufacturer = new Manufacturer(null, EXTERNAL_PUNKAPI, null); 
    	
    	//The ID is not converted because it can confuse with internal ID.
    	//I did not identify the type in the API
    	return new Beer(null, this.getName(), this.getDescription(), this.getAbv(), null, manufacturer, this.getImage_url());
    }
    
    public static List<Beer> convertList(List<ExternalManufacturerDTO> list) {
    	return list.stream().map(d -> d.convertDto()).collect(Collectors.toList());
    }
}
