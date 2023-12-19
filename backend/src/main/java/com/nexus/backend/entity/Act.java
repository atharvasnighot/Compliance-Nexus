package com.nexus.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nexus.backend.entity.preferences.Category;
import com.nexus.backend.entity.preferences.Industry;
import com.nexus.backend.entity.preferences.Ministry;
import com.nexus.backend.entity.preferences.State;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Act {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private int totalCompliance;
    private String title;
    private String description;
    private LocalDateTime date;
    private Integer uploaderId;

    @ManyToOne
    //@JoinColumn(name = "ministry_id")
    private Ministry ministry;

    @ManyToOne
    //@JoinColumn(name = "industry_id")
    private Industry industry;

    @ManyToOne
    //@JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    //@JoinColumn(name = "state_id")
    private State state;

    @OneToMany(mappedBy = "act")
    @JsonManagedReference
    private List<Compliance> complianceSet;

    @Transient
    private String ministryName;

    @Transient
    private String industryName;

    @Transient
    private String categoryName;

    @Transient
    private String stateName;

}
