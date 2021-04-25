package com.spring.boot.controller;

import java.io.IOException;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.spring.boot.config.UploadFileResponse;
import com.spring.boot.entities.Address;
import com.spring.boot.service.AddressService;
import com.spring.boot.service.FileStorageService;

@RestController
@RequestMapping("/address")
public class AddressController {
	static {
		System.out.println("inside Address controller");
	}
	 private static final Logger logger = LoggerFactory.getLogger(AddressController.class);
	
	@Autowired
	AddressService service;
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@PostMapping("/jpa/multimedia/")
	public ResponseEntity<List> employeeWithImage(
			@ModelAttribute Address adr,
			@RequestParam("file") MultipartFile file) {
		System.out.println("Adress INFO" +adr);
		System.out.println("City Image : "+file);
		//FILE UPload Cha
		String fileName = fileStorageService.storeFile(file);

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/address/downloadFile/")
                .path(fileName)
                .toUriString();
		adr.setDbPath(fileDownloadUri);
		UploadFileResponse resp = new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());

		List responesObjects = new ArrayList();
		responesObjects.add(resp); // uploaded file cha response
		
		Address saveAdr = service.save(adr);
		
		responesObjects.add(saveAdr);
		return new ResponseEntity<List>(responesObjects,HttpStatus.CREATED);
		
	}
	
	 @GetMapping("/downloadFile/{fileName:.+}") // download sathi
	    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, 
	    		HttpServletRequest request) {
	        // Load file as Resource
	        Resource resource = fileStorageService.loadFileAsResource(fileName);

	        // Try to determine file's content type
	        String contentType = null;
	        try {
	            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
	        } catch (IOException ex) {
	            logger.info("Could not determine file type.");
	        }

	        // Fallback to the default content type if type could not be determined
	        if(contentType == null) {
	            contentType = "application/octet-stream";
	        }

	        return ResponseEntity.ok()
	                .contentType(MediaType.parseMediaType(contentType))
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
	                .body(resource);
	    }
	
	@GetMapping("/{aid}")
	public ResponseEntity getAdr(@PathVariable("aid")int id) {
		Optional<Address> en = service.findById(id);
		Address entity = en.get();
		if(en.isPresent()) {
			return new ResponseEntity<Address>(entity,HttpStatus.OK);
		}else {
			return new ResponseEntity("No Record Present",HttpStatus.OK);
		}
	}
	
	@GetMapping("/")
	public ResponseEntity <List<Address>> getAllAdr() {
		List<Address> allAdr = service.findAll();
		return new ResponseEntity <List<Address>>(allAdr,HttpStatus.OK);
	}
	@PostMapping("/")
	public ResponseEntity<Address> addAdr(@RequestBody Address adr) {
		Address address = service.save(adr);
		return new ResponseEntity<Address>(address,HttpStatus.CREATED);
		
	}
	
	@DeleteMapping("/{aid}")
	public ResponseEntity deleteAdr(@PathVariable("aid")int id) {
		Optional<Address> en = service.findById(id);
		Address entity = en.get();
		service.delete(entity);
		return new ResponseEntity("Record Deleted",HttpStatus.OK);
	}
	@PutMapping("/{aid}")
	public ResponseEntity updateAdr(@PathVariable("aid")int id, @RequestBody Address adr) {
		Optional<Address> en = service.findById(id);
		Address entity = en.get();
	if(en.isPresent()) {
		entity.setId(adr.getId());
		entity.setCity(adr.getCity());
		service.save(entity);
		return new ResponseEntity<Address>(entity,HttpStatus.CREATED);
	}else {
		return new ResponseEntity("No Record Present",HttpStatus.OK);
	}
			
	}

}
