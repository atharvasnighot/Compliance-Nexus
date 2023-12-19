package com.nexus.backend.repository;

import com.nexus.backend.entity.Tender;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TenderRepository extends JpaRepository<Tender, Integer>, JpaSpecificationExecutor<Tender> {

        List<Tender> findAllByOrderByDateDesc();

        List<Tender> findAllByUploaderId(Integer uploaderId);

        List<Tender> findAll(Specification<Tender> spec, Sort sort);
}
