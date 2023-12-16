package com.nexus.backend.repository;

import com.nexus.backend.entity.Act;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActRepository extends JpaRepository<Act, Integer>, JpaSpecificationExecutor<Act> {

    List<Act> findAllByOrderByDateDesc();

    List<Act> findAllByUploaderId(Integer uploaderId);

    List<Act> findAll(Specification<Act> spec, Sort sort);
}
