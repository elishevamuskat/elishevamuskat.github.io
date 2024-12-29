package com.example.weddingSitter.services;

import com.example.weddingSitter.entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    Optional<Seat> findById(Integer guest_id);

    List<Seat> findAllByTableId(Long tableId);

    Optional<Seat> findByTable_IdAndName(Long tableId, String guestName);

    Optional<Seat> findByTable_IdAndNum(Long tableId, Long seatNum);

}
