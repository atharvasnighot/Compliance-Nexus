package com.nexus.backend.repository;

import com.nexus.backend.entity.TenderCompliance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenderComplianceRepository extends JpaRepository<TenderCompliance, Integer>{

}
