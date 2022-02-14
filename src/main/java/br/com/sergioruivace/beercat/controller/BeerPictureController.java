package br.com.sergioruivace.beercat.controller;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.sergioruivace.beercat.model.Beer;
import br.com.sergioruivace.beercat.model.BeerPicture;
import br.com.sergioruivace.beercat.repository.BeerPictureRepository;
import br.com.sergioruivace.beercat.repository.BeerRepository;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/beerPictures")
@Tag(name = "BeerPicture", description = "The beer picture API")
public class BeerPictureController {
	
	@Autowired
	private BeerPictureRepository reposistory;
	
	@Autowired
	private BeerRepository beerRepository;
	
    @PostMapping
    public ResponseEntity<BeerPicture> upload(@RequestParam Long beerId, @RequestParam("picture") MultipartFile picture, UriComponentsBuilder uriBuilder) throws IOException
    {
		Optional<Beer> optional = beerRepository.findById(beerId);
		if (optional.isPresent()) {
			Beer beer = optional.get();
			BeerPicture beerPicture = new BeerPicture(null, picture.getOriginalFilename(), picture.getContentType(), picture.getBytes());			
			reposistory.save(beerPicture);			
			URI uri = uriBuilder.path("/beerPictures/{id}/download").buildAndExpand(beerPicture.getId()).toUri();
			beer.setPictureUrl(uri.toString());
			beerRepository.save(beer);
			
			return ResponseEntity.created(uri).body(beerPicture);	
		}
		return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id)
    {
    	Optional<BeerPicture> optional = reposistory.findById(id);
		if (optional.isPresent()) {
			BeerPicture uploadedPicture = optional.get();
			
			 return ResponseEntity.ok()
		                .contentType(MediaType.parseMediaType(uploadedPicture.getType()))
		                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename= "+uploadedPicture.getName())
		                .body(new ByteArrayResource(uploadedPicture.getData()));
		}
          
		return ResponseEntity.notFound().build();
    }

}
