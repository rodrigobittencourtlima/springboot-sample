package com.bittsoftware.dscatalog.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;

	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		repository.deleteById(1L);
		Assertions.assertFalse(repository.findById(1L).isPresent());
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> repository.deleteById(100L));
	}

}
