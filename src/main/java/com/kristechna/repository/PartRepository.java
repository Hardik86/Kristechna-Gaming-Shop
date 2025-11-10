package com.kristechna.repository;

import com.kristechna.model.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PartRepository extends JpaRepository<Part, Long> {
    @Query("SELECT p FROM Part p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Part> findByNameContainingIgnoreCase(@Param("name") String name);

    List<Part> findAllByOrderByNameAsc();
}
