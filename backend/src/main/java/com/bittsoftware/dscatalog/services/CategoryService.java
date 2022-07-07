package com.bittsoftware.dscatalog.services;

import java.util.List;
import java.util.stream.Collectors;

import com.bittsoftware.dscatalog.dto.CategoryDTO;
import com.bittsoftware.dscatalog.repositories.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		return repository.findAll().stream().map(entity -> new CategoryDTO(entity)).collect(Collectors.toList());
	}

}
