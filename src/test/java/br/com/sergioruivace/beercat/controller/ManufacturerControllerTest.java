package br.com.sergioruivace.beercat.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.sergioruivace.beercat.BeercatApplication;
import br.com.sergioruivace.beercat.model.Manufacturer;
import br.com.sergioruivace.beercat.repository.ManufacturerRepository;

@SpringBootTest(classes = BeercatApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test.properties")
public class ManufacturerControllerTest {
	
	@Autowired
    private MockMvc mvc;
	
	@Autowired
    private ObjectMapper mapper;
	
	@Autowired
	private ManufacturerRepository repository;	
	
	@Test
	void list() throws Exception {	
		RequestBuilder request = get("/manufacturers")
				.contentType(MediaType.APPLICATION_JSON);
		
		int dataCount = repository.findAll().size();
		
		mvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content", hasSize(dataCount)));		
        
	}
	
	@Test
	void list_wrongField() throws Exception {
		RequestBuilder request = get("/manufacturers").param("sortField", "xxxx")
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().is4xxClientError());    
        
	}
	
	@Test
	void list_wrongDirection() throws Exception {
		RequestBuilder request = get("/manufacturers").param("sortDirection", "xxxx")
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().is4xxClientError());    
        
	}
	
	@Test  @WithUserDetails("user@beercat.com")
	void create() throws Exception {
		Manufacturer manufacturer = new Manufacturer(null, "Ambev", "Brazil");
		
		RequestBuilder request = post("/manufacturers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(manufacturer));
		
		mvc.perform(request)			
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.name", is(manufacturer.getName())));  

        
	}
	
	@Test @WithUserDetails("user@heineken.com")
	void detail() throws Exception {
		RequestBuilder request = get("/manufacturers/1")
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().isOk());  
        
	}
	
	@Test
	void detail_invalidManufacturer() throws Exception {
		Long id = 999l;
	
		RequestBuilder request = get("/manufacturers/" + id)
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().is4xxClientError());  
        
	}
	
	@Test @WithUserDetails("user@beercat.com")
	void update() throws Exception {
		Manufacturer manufacturer = new Manufacturer(1l, "Ambev", "Brazil");
		
		RequestBuilder request = put("/manufacturers/" + manufacturer.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(manufacturer));
		
		mvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name", is(manufacturer.getName())));          
	}
	
	@Test @WithUserDetails("user@erdinger.com")
	void update_OtherManufacturer() throws Exception {
		Manufacturer manufacturer = new Manufacturer(1l, "Ambev 2", "Chile");
		
		RequestBuilder request = put("/manufacturers/" + manufacturer.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(manufacturer));
		
		mvc.perform(request)
			.andExpect(status().is4xxClientError());           
	}
	
	@Test @WithUserDetails("user@beercat.com")
	void update_invalidManufacturer() throws Exception {
		Long id = 999l;
		Manufacturer manufacturer = new Manufacturer(id, "Heineken", "Netherlands");
		
		RequestBuilder request = put("/manufacturers/" + id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(manufacturer));
		
		mvc.perform(request)
			.andExpect(status().is4xxClientError());  
        
	}
	
	@Test @WithUserDetails("user@beercat.com")
	void remove() throws Exception {
		
		RequestBuilder request = delete("/manufacturers/3")
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().isNoContent());    
        
	}
	
	@Test @WithUserDetails("user@beercat.com")
	void remove_invalidManufacturer() throws Exception {
		Long id = 999l;
		
		RequestBuilder request = delete("/manufacturers/" + id)
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().is4xxClientError());   
        
	}
	
	@Test @WithUserDetails("user@heineken.com")
	void remove_selfManufacturer() throws Exception {
		Long id = 1l;
		
		RequestBuilder request = delete("/manufacturers/" + id)
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().is4xxClientError());   
        
	}
}
