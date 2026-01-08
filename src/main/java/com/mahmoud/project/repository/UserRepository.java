package com.mahmoud.project.repository;

import com.mahmoud.project.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"profile"})
    @Query("SELECT u FROM User u")
    List<User> findAllWithProfile();

    @EntityGraph(attributePaths = {"profile"})
    @Query("SELECT u FROM User u")
    List<User> findAllWithProfile(Sort sort);

    @Query("""
            SELECT u FROM User u
            JOIN FETCH u.profile
            WHERE u.id = :id""")
    Optional<User> findByIdWithProfile(Long id);
}
