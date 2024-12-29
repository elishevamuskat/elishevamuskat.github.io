package com.example.weddingSitter.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;


@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    @JsonBackReference
    private EventTable table;


    private String tableRef;


    @Column(nullable = true)
    private String name;

    @Column(nullable = false)
    private Long num;

//    @Column
//    private String email;

//    @Column
//    @Enumerated(EnumType.STRING)
//    private RSVPStatus rsvpStatus;

//    @Column
//    private String notes;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public EventTable getTable() { return table; }
    public void setTable(EventTable table) { this.table = table; }

    public String getGuestName() { return name; }
    public void setGuestName(String name) { this.name = name; }

    public Long getSeatNum() { return num; }
    public void setSeatNum(Long seatNum) { this.num = seatNum; }

    public void setTableRef(String tableName) {this.tableRef = tableName; }
    public String getTableRef() {
        return tableRef;
    }
//    public String getEmail() { return email; }
//    public void setEmail(String email) { this.email = email; }
//
//    public RSVPStatus getRsvpStatus() { return rsvpStatus; }
//    public void setRsvpStatus(RSVPStatus rsvpStatus) { this.rsvpStatus = rsvpStatus; }
//
//    public String getNotes() { return notes; }
//    public void setNotes(String notes) { this.notes = notes; }
}
