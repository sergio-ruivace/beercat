package br.com.sergioruivace.beercat.controller;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.sergioruivace.beercat.model.Manufacturer;
import br.com.sergioruivace.beercat.repository.ManufacturerRepository;

@WebMvcTest(ManufacturerController.class)
@AutoConfigureMockMvc
public class ManufacturerControllerTest {
	
	@Autowired
    private MockMvc mvc;
	
	@Autowired
    private ObjectMapper mapper;
	
	@MockBean
	private ManufacturerRepository repository;
	
	@MockBean
	private PropertyReferenceException propertyException;
	
	@MockBean
	private IllegalArgumentException argumentException;
	
	@Test
	void list() throws Exception {
		Manufacturer manufacturer = new Manufacturer(1l, "Heineken", "Netherlands");
		List<Manufacturer> list = Arrays.asList(manufacturer, manufacturer);
		
		Page<Manufacturer> page = new PageImpl<Manufacturer>(list);
		
		when(repository.findAll(isA(Pageable.class))).thenReturn(page);
		
		RequestBuilder request = get("/manufacturers")
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content", hasSize(2)))
            .andExpect(jsonPath("$.content[0].name", is(manufacturer.getName())));    
        
	}
	
	@Test
	void list_wrongField() throws Exception {
		when(repository.findAll(isA(Pageable.class))).thenThrow(propertyException);		

		RequestBuilder request = get("/manufacturers").param("sortField", "xxxx")
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().is4xxClientError());    
        
	}
	
	@Test
	void list_wrongDirection() throws Exception {
		when(repository.findAll(isA(Pageable.class))).thenThrow(argumentException);		

		RequestBuilder request = get("/manufacturers").param("sortDirection", "xxxx")
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().is4xxClientError());    
        
	}
	
	@Test
	void create() throws Exception {
		Manufacturer manufacturer = new Manufacturer(1l, "Ambev", "Brazil");
		
		when(repository.save(manufacturer)).thenReturn(manufacturer);	
		
		RequestBuilder request = post("/manufacturers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(manufacturer));
		
		mvc.perform(request)			
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.name", is(manufacturer.getName())));  

        
	}
	
	@Test
	void detail() throws Exception {
		Manufacturer manufacturer = new Manufacturer(1l, "Ambev", "Brazil");
		
		when(repository.findById(1l)).thenReturn(Optional.of(manufacturer));
		
		RequestBuilder request = get("/manufacturers/" + manufacturer.getId())
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name", is(manufacturer.getName())));  
        
	}
	
	@Test
	void detail_invalidManufacturer() throws Exception {
		Long id = 999l;
		
		when(repository.findById(id)).thenReturn(Optional.empty());
		
		RequestBuilder request = get("/manufacturers/" + id)
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().is4xxClientError());  
        
	}
	
	@Test
	void update() throws Exception {
		Manufacturer manufacturer = new Manufacturer(1l, "Ambev", "Brazil");
		
		when(repository.findById(1l)).thenReturn(Optional.of(manufacturer));
		when(repository.save(manufacturer)).thenReturn(manufacturer);	
		
		RequestBuilder request = put("/manufacturers/" + manufacturer.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(manufacturer));
		
		mvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name", is(manufacturer.getName())));  
        
	}
	
	@Test
	void update_invalidManufacturer() throws Exception {
		Long id = 999l;
		Manufacturer manufacturer = new Manufacturer(1l, "Heineken", "Netherlands");
		
		when(repository.findById(id)).thenReturn(Optional.empty());
		
		RequestBuilder request = put("/manufacturers/" + id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(manufacturer));
		
		mvc.perform(request)
			.andExpect(status().is4xxClientError());  
        
	}
	
	@Test
	void remove() throws Exception {
		Manufacturer manufacturer = new Manufacturer(1l, "Heineken", "Netherlands");
		
		when(repository.findById(1l)).thenReturn(Optional.of(manufacturer));
		
		RequestBuilder request = delete("/manufacturers/" + manufacturer.getId())
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().isNoContent());    
        
	}
	
	@Test
	void remove_invalidManufacturer() throws Exception {
		Long id = 999l;
		when(repository.findById(id)).thenReturn(Optional.empty());
		
		RequestBuilder request = delete("/manufacturers/" + id)
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().is4xxClientError());    
        
	}
}
