package com.bittsoftware.dscatalog.repositories;

import com.bittsoftware.dscatalog.entities.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
