package com.esb.apiary.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Flora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Flora name is required")
    @Column(unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private Season bloomingSeason;

    @ManyToMany(mappedBy = "flora")
    @JsonIgnore
    private Set<Apiary> apiaries;
}