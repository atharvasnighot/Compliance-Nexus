package com.nexus.backend.repository;

import com.nexus.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    User findByEmail(String email);

    @Query("SELECT u FROM User u " +
            "WHERE (:ministryId IS NULL OR u.ministry.id = :ministryId) " +
            "AND (:industryId IS NULL OR u.industry.id = :industryId) " +
            "AND (:categoryId IS NULL OR u.category.id = :categoryId) " +
            "AND (:stateId IS NULL OR u.state.id = :stateId)")
    List<User> findByPreferences(Integer ministryId, Integer industryId, Integer categoryId, Integer stateId);

}
