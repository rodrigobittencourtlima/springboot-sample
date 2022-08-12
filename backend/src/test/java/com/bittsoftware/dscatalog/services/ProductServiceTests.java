package com.bittsoftware.dscatalog.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.bittsoftware.dscatalog.dto.ProductDTO;
import com.bittsoftware.dscatalog.entities.Category;
import com.bittsoftware.dscatalog.entities.Product;
import com.bittsoftware.dscatalog.repositories.CategoryRepository;
import com.bittsoftware.dscatalog.repositories.ProductRepository;
import com.bittsoftware.dscatalog.services.exceptions.DatabaseException;
import com.bittsoftware.dscatalog.services.exceptions.ResourceNotFoundException;
import com.bittsoftware.dscatalog.tests.Factory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;

	@Mock
	private CategoryRepository categoryRepository;

	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private PageImpl<Product> page;
	private Product product;
	private Category category;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		product = Factory.createProduct();
		category = Factory.createCategory();
		page = new PageImpl<>(List.of(product));

		doNothing().when(repository).deleteById(existingId);
		doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

		when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
		when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		when(repository.findById(existingId)).thenReturn(Optional.of(product));
		when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

		when(repository.getReferenceById(existingId)).thenReturn(product);
		when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

		when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
		when(categoryRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
	}

	@Test
	public void deleteShouldNotThrowExceptionWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> service.delete(existingId));
		verify(repository, times(1)).deleteById(existingId);
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistingId));
		verify(repository, times(1)).deleteById(nonExistingId);
	}

	@Test
	public void deleteShouldThrowDatabaseExceptionWhenIntegrityViolationOccurs() {
		Assertions.assertThrows(DatabaseException.class, () -> service.delete(dependentId));
		verify(repository, times(1)).deleteById(dependentId);
	}

	@Test
	public void findAllPagedShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 10);

		Page<ProductDTO> result = service.findAllPaged(pageable);

		Assertions.assertNotNull(result);
		verify(repository).findAll(pageable);
	}

	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		Assertions.assertNotNull(service.findById(existingId));
		verify(repository).findById(existingId);
	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> service.findById(nonExistingId));
		verify(repository).findById(nonExistingId);
	}

	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {
		Assertions.assertNotNull(service.update(existingId, Factory.createProductDTO()));
		verify(repository).save(product);
	}

	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class,
				() -> service.update(nonExistingId, Factory.createProductDTO()));
		verify(repository, never()).save(product);
	}

}
