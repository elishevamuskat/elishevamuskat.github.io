package com.example.weddingSitter.services;

import com.example.weddingSitter.entities.User;
import com.example.weddingSitter.entities.Wedding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WeddingRepository extends JpaRepository<Wedding, Long> {
    Optional<Wedding> findById(Integer id);
    Optional<Wedding> findByName(String name);
    Optional<Wedding> findByUser_UsernameAndName(String username, String name);

    List<Wedding> findAllByUser_Username(String username);
    List<Wedding> findAllByUserId(Integer userId);

}
