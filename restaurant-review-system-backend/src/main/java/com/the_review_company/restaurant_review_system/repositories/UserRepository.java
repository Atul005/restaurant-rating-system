package com.the_review_company.restaurant_review_system.repositories;
import com.the_review_company.restaurant_review_system.domain.entities.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends ElasticsearchRepository<User, String> {

}
