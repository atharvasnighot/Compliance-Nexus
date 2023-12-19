package com.nexus.backend.specification;

import com.nexus.backend.entity.Act;
import com.nexus.backend.entity.Tender;
import com.nexus.backend.entity.preferences.Category;
import com.nexus.backend.entity.preferences.Industry;
import com.nexus.backend.entity.preferences.Ministry;
import com.nexus.backend.entity.preferences.State;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class TenderSpecification {

    public static Specification<Tender> titleOrDescriptionContains(String searchString) {

        return (root, query, builder) -> {
            Join<Act, Ministry> ministryJoin = root.join("ministry", JoinType.LEFT);
            Join<Act, Industry> industryJoin = root.join("industry", JoinType.LEFT);
            Join<Act, Category> categoryJoin = root.join("category", JoinType.LEFT);
            Join<Act, State> stateJoin = root.join("state", JoinType.LEFT);

            return builder.or(
                    builder.like(root.get("title"), "%" + searchString + "%"),
                    builder.like(root.get("description"), "%" + searchString + "%"),
                    builder.like(ministryJoin.get("name"), "%" + searchString + "%"),
                    builder.like(industryJoin.get("name"), "%" + searchString + "%"),
                    builder.like(categoryJoin.get("name"), "%" + searchString + "%"),
                    builder.like(stateJoin.get("name"), "%" + searchString + "%")
            );
        };
    }

}
