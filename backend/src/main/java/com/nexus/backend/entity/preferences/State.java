package com.nexus.backend.entity.preferences;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class State {

    @Id
    private Integer id;

    private String name;
}
