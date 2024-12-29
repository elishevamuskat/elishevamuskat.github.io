package com.example.weddingSitter.services;

import com.example.weddingSitter.entities.EventTable;
import com.example.weddingSitter.entities.User;
import com.example.weddingSitter.entities.Wedding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventTableRepository extends JpaRepository<EventTable, Long> {
    Optional<EventTable> findById(Long table_id);

    List<EventTable> findAllByWeddingId(Long weddingId);

    Optional<EventTable> findByWedding_IdAndName(Long weddingId, String table_name);

    @Query("SELECT t FROM EventTable t " +
            "JOIN t.wedding w " +
            "JOIN w.user u " +
            "WHERE u.username = :username " +
            "AND w.name = :weddingName " +
            "AND t.name = :tableName")
    EventTable findByUserUsernameAndWeddingNameAndTableName(
            @Param("username") String username,
            @Param("weddingName") String weddingName,
            @Param("tableName") String tableName
    );
}
