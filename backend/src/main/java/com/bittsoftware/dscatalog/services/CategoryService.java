package com.bittsoftware.dscatalog.services;

import java.util.List;

import com.bittsoftware.dscatalog.entities.Category;
import com.bittsoftware.dscatalog.repositories.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	public List<Category> findAll() {
		return repository.findAll();
	}

}
