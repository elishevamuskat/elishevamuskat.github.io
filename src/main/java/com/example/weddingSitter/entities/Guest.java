package com.example.weddingSitter.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;


@Entity
@Table(name = "guests")
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    @JsonBackReference
    private EventTable table;

    @Column(nullable = false)
    private String name;

    @Column
    private String email;

    @Column
    @Enumerated(EnumType.STRING)
    private RSVPStatus rsvpStatus;

    @Column
    private String notes;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public EventTable getTable() { return table; }
    public void setTable(EventTable table) { this.table = table; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public RSVPStatus getRsvpStatus() { return rsvpStatus; }
    public void setRsvpStatus(RSVPStatus rsvpStatus) { this.rsvpStatus = rsvpStatus; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
