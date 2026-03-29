package com.kim.flashcard;

import com.kim.flashcard.Model.CardStack;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface CardStackRepository extends JpaRepository<CardStack, Long> {
    List<CardStack> findAllByOrderByLastUsedDesc();
}