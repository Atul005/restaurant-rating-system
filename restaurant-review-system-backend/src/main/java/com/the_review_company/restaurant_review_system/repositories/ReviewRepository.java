package com.the_review_company.restaurant_review_system.repositories;

import com.the_review_company.restaurant_review_system.domain.entities.Review;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends ElasticsearchRepository<Review, String> {

}
