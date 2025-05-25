package com.example.userservice.repository;

import com.example.userservice.model.FeedbackConsultation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackConsultationRepository extends MongoRepository<FeedbackConsultation, String> {

    List<FeedbackConsultation> findByConsultationId(String consultationId);
}
