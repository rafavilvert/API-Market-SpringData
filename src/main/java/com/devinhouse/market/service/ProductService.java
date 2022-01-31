package com.devinhouse.market.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.devinhouse.market.model.persistence.Category;
import com.devinhouse.market.model.persistence.Product;
import com.devinhouse.market.model.repository.ProductRepository;
import com.devinhouse.market.model.transport.ProductDTO;
import com.devinhouse.market.utils.Utils;

@Service
public class ProductService {

	private final Logger LOG = LogManager.getLogger(ProductService.class);

	private final ProductRepository productRepository;

	private final CategoryService categoryService;

	public ProductService(ProductRepository productRepository, CategoryService categoryService) {
		this.productRepository = productRepository;
		this.categoryService = categoryService;
	}

	public ResponseEntity<HttpStatus> create(ProductDTO produtoDTO) {

		checkProductIsNull(produtoDTO);

		this.LOG.info("Preparando para salvar o produto" + produtoDTO.getName() + "no banco");
		produtoDTO.setIdentifier(Utils.generateUUID());
		Product product = new Product(produtoDTO);

		Category category = this.categoryService.checkIfExist(produtoDTO.getCategoryDTO());
		product.setCategory(category);

		Product saved = this.productRepository.save(product);

		if (saved == null) {
			this.LOG.error("Erro ao salvar o produto no banco");
			return ResponseEntity.badRequest().build();
		}

		this.LOG.info("Produto criado com sucesso!");
		return ResponseEntity.ok().build();
	}

	private void checkProductIsNull(ProductDTO produtoDTO) {

		if (produtoDTO == null) {
			this.LOG.error("Erro parâmetro Produto nulo!");
			throw new IllegalArgumentException("O objeto enviado está nulo");
		}
		this.LOG.info("Validando os parâmetros de requisição");
	}

	
	public ResponseEntity<HttpStatus> update(ProductDTO productDTO, String identifier) {
		checkProductIsNull(productDTO);

		this.LOG.info("Buscando objeto no banco");
		Product product = this.productRepository.findByIdentifier(identifier);

		if (product == null) {
			this.LOG.info("Produto não encontrado no Banco com o Identificador " + identifier);
			return ResponseEntity.notFound().build();
		}

		this.LOG.info("Produto preparando para atualizar!");
		product.setDescription(productDTO.getDescription());
		product.setName(productDTO.getName());
		product.setPrice(productDTO.getPrice());
		
		this.productRepository.save(product);

		this.LOG.info("Produto Atualizado com sucesso");
		return ResponseEntity.ok().build();
	}

	public List<ProductDTO> listaAll() {
		this.LOG.info("Buscando Produtos no Banco");
		List<ProductDTO> productsDTO = new ArrayList<>();
		Iterable<Product> iterable = this.productRepository.findAll();
		iterable.forEach(p -> productsDTO.add(new ProductDTO(p)));
		this.LOG.info("Encontrados " + productsDTO.size() + " produtos no banco");
		return productsDTO;
	}

	public ResponseEntity<HttpStatus> delete(String identifier) {
		if (identifier == null || identifier.isEmpty()) {
			this.LOG.error("O Id está nulo!, infirme o ID");
			throw new IllegalArgumentException("Erro ID vazio");
		}
		this.productRepository.deleteByIdentifier(identifier);
		this.LOG.info("Produto excluído com sucesso!");
		return ResponseEntity.accepted().build();
	}
	
	public Product getByName(String name) {
		return this.productRepository.findByName(name);
	}

}
