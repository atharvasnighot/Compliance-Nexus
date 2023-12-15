package com.nexus.backend.repository;

import com.nexus.backend.entity.Updates;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UpdatesRepository extends JpaRepository<Updates, Integer> {

    List<Updates> findAllByOrderByDateDesc();
}
