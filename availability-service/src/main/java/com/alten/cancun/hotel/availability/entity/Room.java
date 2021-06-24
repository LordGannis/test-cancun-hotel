package com.alten.cancun.hotel.availability.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_room")
@NoArgsConstructor
@Getter
@Setter
public class Room {

    @Id
    @SequenceGenerator(name = "tb_room_seq", allocationSize = 1, sequenceName = "tb_room_seq")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "tb_room_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(length = 150)
    private String name;

    @Column(name = "creationdate")
    private LocalDateTime creationDate;

}
