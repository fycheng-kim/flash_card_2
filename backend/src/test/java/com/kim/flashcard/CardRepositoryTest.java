package com.kim.flashcard;

import com.kim.flashcard.Model.Card;
import com.kim.flashcard.Model.CardStack;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CardRepositoryTest extends BaseJpaRepositoryTest {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private TestEntityManager entityManager; // Helps setup test data

    @Test
    void should_find_cards_by_CardStackId() {
        // GIVEN
        int numOfCards = 2;
        CardStack cardStack = TestDataFactory.createStackWithCards("test2", numOfCards);
        entityManager.persist(cardStack);
        entityManager.flush();

        // WHEN
        List<Card> foundCards = cardRepository
                .findByStack(cardStack);

        // THEN
        assertThat(foundCards).hasSize(numOfCards);
    }
}