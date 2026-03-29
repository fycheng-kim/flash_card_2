package com.kim.flashcard.Model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CardStack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDateTime lastUsed;

    @OneToMany(mappedBy = "stack", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Tells Jackson this is the "parent" side of the JSON
    private List<Card> cards = new ArrayList<>();

    public CardStack() {}

    private CardStack(CardStack.Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.lastUsed = builder.lastUsed;
        if (builder.cards != null) {
            this.cards = builder.cards;
        }
    }

    public static Builder builder() {
        return new CardStack.Builder();
    }

    public static class Builder {
        private Long id;
        private String name;
        private LocalDateTime lastUsed;
        private List<Card> cards;

        public Builder id(Long id) {
            this.id = id;
            return this; // Returns itself to allow chaining
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder lastUsed(LocalDateTime lastUsed) {
            this.lastUsed = lastUsed;
            return this;
        }

        public Builder cards(List<Card> cards) {
            this.cards = cards;
            return this;
        }

        public Builder addCard(Card card) {
            if (this.cards == null) {
                this.cards = new ArrayList<>();
            }
            this.cards.add(card);
            return this;
        }

        // The final step that creates the actual Card object
        public CardStack build() {
            return new CardStack(this);
        }
    }

    // Setters

    public void setLastUsed(LocalDateTime lastUsed) {
        this.lastUsed = lastUsed;
    }

    public void addCard(Card card) {
        if (card == null) {
            return;
        }

        if (!card.getStack().equals(this)) {
            card.getStack().getCards().remove(card);
        }
        this.cards.add(card);
    }

    public void addCards(List<Card> cards) {
        if (cards == null) {
            return;
        }
        for (Card card : cards) {
            this.addCard(card);
        }
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public LocalDateTime getLastUsed() { return lastUsed; }
    public List<Card> getCards() { return cards; }
}