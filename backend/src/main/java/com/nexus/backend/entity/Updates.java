package com.nexus.backend.entity;

import com.nexus.backend.entity.preferences.Category;
import com.nexus.backend.entity.preferences.Industry;
import com.nexus.backend.entity.preferences.Ministry;
import com.nexus.backend.entity.preferences.State;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Updates {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;//
    private String title;
    private String description;
    private LocalDateTime date;//
    private Integer uploaderId;//

    @ManyToOne
    private Ministry ministry;

    @ManyToOne
    private Industry industry;

    @ManyToOne
    private Category category;

    @ManyToOne
    private State state;

}
