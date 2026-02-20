package org.univ_paris8.iut.montreuil.dev_avance.repository;

import org.springframework.data.jpa.domain.Specification;
import org.univ_paris8.iut.montreuil.dev_avance.entity.Annonce;
import org.univ_paris8.iut.montreuil.dev_avance.entity.Annonce.Status;

import jakarta.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AnnonceSpecifications {

    public static Specification<Annonce> withFilters(String q, String status, Long categoryId, Long authorId,
            Timestamp fromDate, Timestamp toDate) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (q != null && !q.trim().isEmpty()) {
                String likePattern = "%" + q.toLowerCase() + "%";
                Predicate titleLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), likePattern);
                Predicate descLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern);
                predicates.add(criteriaBuilder.or(titleLike, descLike));
            }

            if (status != null && !status.trim().isEmpty()) {
                try {
                    predicates.add(criteriaBuilder.equal(root.get("status"), Status.valueOf(status)));
                } catch (IllegalArgumentException e) {
                    // Ignore invalid status or handle error
                }
            }

            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId));
            }

            if (authorId != null) {
                predicates.add(criteriaBuilder.equal(root.get("author").get("id"), authorId));
            }

            if (fromDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), fromDate));
            }

            if (toDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), toDate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
