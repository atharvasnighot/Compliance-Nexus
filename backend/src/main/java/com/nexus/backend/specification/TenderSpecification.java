package com.nexus.backend.specification;

import com.nexus.backend.entity.Tender;
import com.nexus.backend.entity.preferences.Category;
import com.nexus.backend.entity.preferences.Industry;
import com.nexus.backend.entity.preferences.Ministry;
import com.nexus.backend.entity.preferences.State;
import org.springframework.data.jpa.domain.Specification;

public class TenderSpecification {

    public static Specification<Tender> hasState(State state) {
        return (root, query, builder) -> state == null ? null : builder.equal(root.get("state"), state);
    }

    public static Specification<Tender> hasIndustry(Industry industry) {
        return (root, query, builder) -> industry == null ? null : builder.equal(root.get("industry"), industry);
    }

    public static Specification<Tender> hasMinistry(Ministry ministry) {
        return (root, query, builder) -> ministry == null ? null : builder.equal(root.get("ministry"), ministry);
    }

    public static Specification<Tender> hasCategory(Category category) {
        return (root, query, builder) -> category == null ? null : builder.equal(root.get("category"), category);
    }

    public static Specification<Tender> titleOrDescriptionContains(String searchString) {
        return (root, query, builder) -> searchString == null ? null :
                builder.or(
                        builder.like(builder.lower(root.get("title")), "%" + searchString.toLowerCase() + "%"),
                        builder.like(builder.lower(root.get("description")), "%" + searchString.toLowerCase() + "%")
                );
    }

}
