package com.kim.flashcard;

import com.kim.flashcard.Model.Card;
import com.kim.flashcard.Model.CardStack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByStack(CardStack stack);

    List<Card> findByStackId(Long stackId);
}