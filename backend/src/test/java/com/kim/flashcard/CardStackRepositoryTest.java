package com.kim.flashcard;

import com.kim.flashcard.Model.CardStack;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CardStackRepositoryTest extends BaseJpaRepositoryTest {

    @Autowired
    private CardStackRepository cardStackRepository;

    @Autowired
    private TestEntityManager entityManager; // Helps setup test data

    @Test
    void should_find_cardStacks_order_lastUsed_desc() {
        // GIVEN
        int numOfCardStacks = 2;
        List<CardStack> testCardStacks = TestDataFactory.createStacks(numOfCardStacks);
        for (int i=0; i < numOfCardStacks; i++) {
            CardStack cardStack = testCardStacks.get(i);
            entityManager.persist(cardStack);
            entityManager.flush();
        }
        // WHEN
        List<CardStack> foundCardStacks = cardStackRepository
                .findAllByOrderByLastUsedDesc();

        // THEN
        assertThat(foundCardStacks).hasSize(2);
        assertThat(foundCardStacks.get(0).getLastUsed())
                .isAfter(foundCardStacks.get(1).getLastUsed());
    }
}