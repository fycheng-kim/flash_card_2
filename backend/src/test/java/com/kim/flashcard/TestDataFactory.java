package com.kim.flashcard;

import com.kim.flashcard.Model.Card;
import com.kim.flashcard.Model.CardStack;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TestDataFactory {
    private static final LocalDateTime DEFAULT_TIME = LocalDateTime.now();

    public static CardStack createStack(String name) {
        return createStack(name, DEFAULT_TIME);
    }

    public static CardStack createStack(String name, LocalDateTime lastUsed) {
        LocalDateTime lsu = (lastUsed == null) ? DEFAULT_TIME : lastUsed;
        return CardStack.builder()
                .name(name)
                .lastUsed(lsu)
                .build();
    }

    public static List<CardStack> createStacks(int numOfStack) {
        List<CardStack> cardStacks = new ArrayList<>();
        for (int i = 0; i < numOfStack; i++){
            String cardStackName = "testCardStack_" + i;
            LocalDateTime cardStackLastUsed =  LocalDateTime.now()
                    .plusSeconds(i * 5);
            cardStacks.add(createStack(cardStackName, cardStackLastUsed));
        }
        return cardStacks;
    }

    public static CardStack createStackWithCards(String name, int numOfCards) {
        CardStack stack = createStack(name);
        List<Card> cards = createCards(numOfCards, stack);
        stack.addCards(cards);
        return stack;
    }

    public static List<Card> createCards(int numOfCards, CardStack cardStack) {
        List<Card> cards =  new ArrayList<>();
        for (int i = 1; i<=numOfCards; i++) {
            Card card = Card.builder()
                    .front("Front " + i)
                    .back("Back " + i)
                    .stack(cardStack)
                    .build();
            cards.add(card);
        }
        return cards;
    }

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule()); // Required for LocalDateTime support

    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }

}