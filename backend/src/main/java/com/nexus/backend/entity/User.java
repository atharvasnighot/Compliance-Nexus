package com.nexus.backend.entity;

import com.nexus.backend.entity.preferences.Category;
import com.nexus.backend.entity.preferences.Industry;
import com.nexus.backend.entity.preferences.Ministry;
import com.nexus.backend.entity.preferences.State;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String username;
    private String email;
    private String password;
    private Boolean isAdmin;
    private String organization;
    private String contact;

    @ManyToOne
   // @JoinColumn(name = "ministry_id")
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

    @Override
    public int hashCode() {
        return super.hashCode();
    }


}
