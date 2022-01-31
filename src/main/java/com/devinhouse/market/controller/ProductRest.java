package com.devinhouse.market.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devinhouse.market.model.transport.ProductDTO;
import com.devinhouse.market.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductRest {

	private final ProductService productService;

	public ProductRest(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping("/create")
	public ResponseEntity<HttpStatus> create(@RequestBody ProductDTO productDTO) {
		return productService.create(productDTO);
	};
	
	@PutMapping("/update/{identifier}")
	public ResponseEntity<HttpStatus> update(@RequestBody ProductDTO productDTO, @PathVariable String identifier) {
		return productService.update(productDTO, identifier);
	};
	
	@GetMapping("/list")
	public List<ProductDTO> listAll(){
		return this.productService.listaAll();
	}
	
	@DeleteMapping("/delete/{identifier}")
	public ResponseEntity<HttpStatus> delete(@PathVariable String identifier) {
	return this.productService.delete(identifier);
	}

}
