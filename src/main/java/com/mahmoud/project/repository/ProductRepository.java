package com.mahmoud.project.repository;

import com.mahmoud.project.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Override
    @Query("""
            SELECT p FROM Product p
            JOIN FETCH p.category""")
    List<Product> findAll();

    @Override
    @Query("""
            SELECT p FROM Product p
            RIGHT JOIN FETCH p.category
            WHERE p.id = :id""")
    Optional<Product> findById(@Param("id") Long id);

    @EntityGraph(attributePaths = {"category"})
    List<Product> findAllByCategoryId(@Param("categoryId") Byte categoryId);
}
