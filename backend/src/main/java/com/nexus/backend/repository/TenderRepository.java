package com.nexus.backend.repository;

import com.nexus.backend.entity.Tender;
import com.nexus.backend.entity.preferences.Category;
import com.nexus.backend.entity.preferences.Industry;
import com.nexus.backend.entity.preferences.Ministry;
import com.nexus.backend.entity.preferences.State;
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

        List<Tender> findByMinistryAndIndustryAndCategoryAndState(Ministry ministry, Industry industry, Category category, State state);
}
