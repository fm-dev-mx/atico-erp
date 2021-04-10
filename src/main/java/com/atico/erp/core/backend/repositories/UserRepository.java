package com.atico.erp.core.backend.repositories;

import com.atico.erp.core.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username= :username")
    User getByUsername(@Param("username") String username);


}
