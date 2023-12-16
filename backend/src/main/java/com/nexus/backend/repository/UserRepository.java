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

//    @Query("SELECT DISTINCT u FROM User u " +
//            "WHERE " +
//            "(:ministryId IS NULL OR u.ministry.id = :ministryId) OR " +
//            "(:industryId IS NULL OR u.industry.id = :industryId) OR " +
//            "(:categoryId IS NULL OR u.category.id = :categoryId) OR " +
//            "(:stateId IS NULL OR u.state.id = :stateId)")
//    List<User> findByPreferences(Integer ministryId, Integer industryId, Integer categoryId, Integer stateId);

    @Query("SELECT DISTINCT u FROM User u " +
            "WHERE " +
            "(COALESCE(:ministryId, u.ministry.id) = u.ministry.id) AND " +
            "(COALESCE(:industryId, u.industry.id) = u.industry.id) AND " +
            "(COALESCE(:categoryId, u.category.id) = u.category.id) AND " +
            "(COALESCE(:stateId, u.state.id) = u.state.id)")
    List<User> findByPreferences(Integer ministryId, Integer industryId, Integer categoryId, Integer stateId);


}
