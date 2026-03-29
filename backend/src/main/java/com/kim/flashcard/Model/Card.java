package com.kim.flashcard.Model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String front;
    private String back;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stack_id")
    @JsonBackReference
    private CardStack stack;

    // 1. Default constructor (Required by JPA)
    public Card() {}

    // 2. Private constructor used ONLY by the Builder
    private Card(Builder builder) {
        this.id = builder.id;
        this.front = builder.front;
        this.back = builder.back;
        this.stack = builder.stack;
    }

    // 3. Static method to start the building process
    public static Builder builder() {
        return new Builder();
    }

    // 4. The Builder Class
    public static class Builder {
        private Long id;
        private String front;
        private String back;
        private CardStack stack;

        public Builder id(Long id) {
            this.id = id;
            return this; // Returns itself to allow chaining
        }

        public Builder front(String front) {
            this.front = front;
            return this;
        }

        public Builder back(String back) {
            this.back = back;
            return this;
        }

        public Builder stack(CardStack stack) {
            this.stack = stack;
            return this;
        }

        // The final step that creates the actual Card object
        public Card build() {
            return new Card(this);
        }
    }


    // Getters
    public Long getId() { return id; }
    public String getFront() { return front; }
    public String getBack() { return back; }
    public CardStack getStack() { return stack; }
}