package com.example.gestionequipe.repository;

import com.example.gestionequipe.model.FeedbackConsultation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackConsultationRepository extends MongoRepository<FeedbackConsultation, String> {

    List<FeedbackConsultation> findByConsultationId(String consultationId);
}
