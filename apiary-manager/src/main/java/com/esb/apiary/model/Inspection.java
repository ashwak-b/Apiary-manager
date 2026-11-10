package com.esb.apiary.model;

import java.time.LocalDate; 

import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Inspection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Inspection date is required")
    @PastOrPresent
    private LocalDate inspectionDate;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Health level is required")
    private HealthLevel healthLevel;

    private String Notes;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "hive_id")
    private Hive hive;
}