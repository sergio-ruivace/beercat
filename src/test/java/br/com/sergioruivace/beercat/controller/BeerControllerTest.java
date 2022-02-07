package br.com.sergioruivace.beercat.controller;


import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

@WebMvcTest(BeerController.class)
@AutoConfigureMockMvc
public class BeerControllerTest {
	
	@Autowired
    private MockMvc mvc;

	@Test
	void getBeers() throws Exception {
		RequestBuilder request = get("/beers");
		mvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("nome")));    
        
	}
}
