package com.ahmedsr.task.repository;

import com.ahmedsr.task.model.Comment;
import com.ahmedsr.task.model.CommentPage;
import com.ahmedsr.task.model.CommentSearchCriteria;
import com.ahmedsr.task.utils.Constants;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class CommentCriteriaRepository {
    // == Fields ==
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    // == Constructor ==
    public CommentCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    // == Public methods ==


    public Page<Comment> findAllWithFilters(CommentPage commentPage,
                                            CommentSearchCriteria commentSearchCriteria){
        CriteriaQuery<Comment> criteriaQuery = criteriaBuilder.createQuery(Comment.class);
        Root<Comment> commentRoot = criteriaQuery.from(Comment.class);

        Predicate predicate = getPredicate(commentSearchCriteria, commentRoot);
        criteriaQuery.where(predicate);
        setOrder(commentPage, criteriaQuery, commentRoot);

        TypedQuery<Comment> typedQuery = entityManager.createQuery(criteriaQuery);
        TypedQuery<Comment[]> typedQuery2 = entityManager.createQuery(Constants.FIND_COMMENTS, Comment[].class);
        typedQuery.setFirstResult(commentPage.getPageNumber() * commentPage.getPageSize());
        typedQuery.setMaxResults(commentPage.getPageSize());


        Pageable pageable = getPageable(commentPage);

        long commentsCount = getCommentCount(predicate);

        return new PageImpl<>(typedQuery.getResultList(), pageable, commentsCount);
    }



    private Predicate getPredicate(CommentSearchCriteria commentSearchCriteria, Root<Comment> commentRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if(Objects.nonNull(commentSearchCriteria.getName())){
            predicates.add(
                    criteriaBuilder.like(commentRoot.get("name"),
                            "%" + commentSearchCriteria.getName() + "%")
            );
        }
        if(Objects.nonNull(commentSearchCriteria.getEmail())){
            predicates.add(
                    criteriaBuilder.like(commentRoot.get("email"),
                            "%" + commentSearchCriteria.getEmail() + "%")
            );
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(CommentPage commentPage, CriteriaQuery<Comment> criteriaQuery, Root<Comment> commentRoot) {
        if(commentPage.getSortDirection().equals(Sort.Direction.ASC)){
            criteriaQuery.orderBy(criteriaBuilder.asc(commentRoot.get(commentPage.getSortedBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(commentRoot.get(commentPage.getSortedBy())));
        }
    }


    private Pageable getPageable(CommentPage commentPage) {
        Sort sort = Sort.by(commentPage.getSortDirection(), commentPage.getSortedBy());
        return PageRequest.of(commentPage.getPageNumber(),commentPage.getPageSize(), sort);
    }

    private long getCommentCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Comment> countRoot = countQuery.from(Comment.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
