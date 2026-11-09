package com.esb.apiary.model;

import java.time.LocalDate; 

import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Harvest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Harvest date is required")
    @PastOrPresent
    private LocalDate harvestDate;

    @Positive(message = "Quantity must be greater than 0")
    private double quantityKg;

    private String HoneyType;   // e.g. "Thyme", "Eucalyptus", "Orange blossom"

    @NotNull
    @ManyToOne
    @JoinColumn(name = "hive_id")
    private Hive hive;
}