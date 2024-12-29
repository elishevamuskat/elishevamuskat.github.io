package com.example.weddingSitter.services;

import com.example.weddingSitter.entities.Guest;
import com.example.weddingSitter.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    Optional<Guest> findById(Integer guest_id);

    List<Guest> findAllByTableId(Integer tableId);

    Optional<Guest> findByTable_IdAndName(Long tableId, String guestName);
}
