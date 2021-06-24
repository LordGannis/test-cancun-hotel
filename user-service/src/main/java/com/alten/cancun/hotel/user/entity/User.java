package com.alten.cancun.hotel.user.entity;

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
@Table(name = "tb_user")
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @SequenceGenerator(name = "tb_user_seq", allocationSize = 1, sequenceName = "tb_user_seq")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "tb_user_seq")
    @Column(name = "id", nullable = false)
    Long id;

    @Column(length = 150)
    String name;

    @Column(length = 50)
    String login;

    @Column(name = "creationdate")
    LocalDateTime creationDate;

}
