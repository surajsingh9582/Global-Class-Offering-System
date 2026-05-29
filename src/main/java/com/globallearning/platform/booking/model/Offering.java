package com.globallearning.platform.booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "offering")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Offering {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;
    private Integer capacity=30;
    private Integer bookedSeats=0;
    private Integer version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher",nullable = false)
    private Teacher teacher;

    @OneToMany(mappedBy = "offering", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessions=new ArrayList<>();

    public void addSession(Session session){
        sessions.add(session);
        session.setOffering(this);
    }
}
