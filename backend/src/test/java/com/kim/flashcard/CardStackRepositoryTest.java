package com.kim.flashcard;

import com.kim.flashcard.Model.CardStack;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

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
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("name").ascending());        // WHEN
        Page<CardStack> foundCardStacks = cardStackRepository
                .findAllByOrderByLastUsedDesc(pageRequest);

        // THEN
        assertThat(foundCardStacks.getContent()).hasSize(2);
        assertThat(foundCardStacks.getContent().get(0).getLastUsed())
                .isAfter(foundCardStacks.getContent().get(1).getLastUsed());
    }

}