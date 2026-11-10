package com.esb.apiary.model;

import java.time.LocalDate; 

import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Hive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Hive code is required")
    @Column(unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    private HiveStatus status = HiveStatus.ACTIVE;

    @PastOrPresent(message = "Install date cannot be in the future")
    private LocalDate installDate;

    @NotNull(message = "A hive must belong to an apiary")
    @ManyToOne
    @JoinColumn(name = "apiary_id")
    private Apiary apiary;
}