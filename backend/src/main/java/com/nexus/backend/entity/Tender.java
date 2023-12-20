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
@Entity
public class Tender {

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

    @OneToMany(mappedBy = "tender")
    @JsonManagedReference
    private List<TenderCompliance> complianceSet;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Tender{")
                .append("id=").append(id)
                .append(", totalCompliance=").append(totalCompliance)
                .append(", title='").append(title).append('\'')
                .append(", description='").append(description).append('\'')
                .append(", date=").append(date)
                .append(", uploaderId=").append(uploaderId);

        if (complianceSet != null) {
            stringBuilder.append(", complianceSet=[");
            for (TenderCompliance compliance : complianceSet) {
                stringBuilder.append("{")
                        .append("complianceId=").append(compliance.getId())
                        .append(", description='").append(compliance.getDescription()).append('\'')
                        .append("}, ");
            }
            stringBuilder.append("]");
        }


        stringBuilder.append('}');
        return stringBuilder.toString();
    }

}
