package com.nexus.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "tender")
@Entity
public class TenderCompliance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JsonBackReference
    private Tender tender;

    private String title;
    private String description;
    private String reference;
    private String penalty;
    private String form;
}
