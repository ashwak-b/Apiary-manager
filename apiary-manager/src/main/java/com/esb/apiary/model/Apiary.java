package com.esb.apiary.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Apiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Apiary name is required")
    private String name;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "An apiary must belong to a beekeeper")
    @ManyToOne
    @JoinColumn(name = "beekeeper_id")
    private Beekeeper beekeeper;

    @ManyToMany
    @JoinTable(name = "apiary_flora",
        joinColumns = @JoinColumn(name = "apiary_id"),
        inverseJoinColumns = @JoinColumn(name = "flora_id"))
    private Set<Flora> flora = new HashSet<>();

    @OneToMany(mappedBy = "apiary")
    @JsonIgnore
    private List<Hive> hives;
}