package com.devinhouse.market.model.repository;

import org.springframework.data.repository.CrudRepository;

import com.devinhouse.market.model.persistence.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {
	
	public Category findByName(String name);

}
