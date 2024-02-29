package com.grpc.client.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.google.protobuf.Descriptors;
import com.grpc.client.service.clientService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class clientController {
	
	 private final clientService clientService;

	 @GetMapping("/author/{id}")
	    public Map<Descriptors.FieldDescriptor, Object> getAuthor(@PathVariable String id) {
	        return clientService.getAuthor(Integer.parseInt(id));
	    }
	    
	    @GetMapping("/book/{author_id}")
	    public List<Map<Descriptors.FieldDescriptor, Object>> getBookByAuthor(@PathVariable String author_id) throws InterruptedException {
	        return clientService.getBooksByAuthor(Integer.parseInt(author_id));
	    }

}
