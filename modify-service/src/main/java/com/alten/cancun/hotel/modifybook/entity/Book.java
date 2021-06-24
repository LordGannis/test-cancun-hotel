package com.alten.cancun.hotel.modifybook.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_book")
@NoArgsConstructor
@Getter
@Setter
public class Book {

    @Id
    @SequenceGenerator(name = "tb_book_seq", allocationSize = 1, sequenceName = "tb_book_seq")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "tb_book_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "startdate")
    private LocalDate startDate;

    @Column(name = "enddate")
    private LocalDate endDate;

    @Column(name = "creationdate")
    private LocalDateTime creationDate;

    @Column(name = "modificationdate")
    private LocalDateTime modificationDate;

    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
