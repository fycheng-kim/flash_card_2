import React, { useState } from 'react';

interface Card {
  id: string;
  term: string;
  definition: string;
}

interface Props {
  cards: Card[];
  onComplete: () => void;
}

export default function StudyView({ cards, onComplete }: Props) {
  const [index, setIndex] = useState(0);
  const [isFlipped, setIsFlipped] = useState(false);

  if (index >= cards.length) {
    return (
      <div>
        <p>No more cards</p>
        <button onClick={onComplete}>Back to Main</button>
      </div>
    );
  }

  const current = cards[index];

  return (
    <div style={{ border: '1px solid #ccc', padding: '20px', textAlign: 'center' }}>
      <h1>{isFlipped ? current.front : current.back}</h1>
      
      {!isFlipped ? (
        <div>
          <button onClick={() => setIndex(index + 1)} style={{ color: 'green' }}>✓</button>
          <button onClick={() => setIsFlipped(true)} style={{ color: 'red' }}>X</button>
        </div>
      ) : (
        <button onClick={() => { setIsFlipped(false); setIndex(index + 1); }}>
          Next Word
        </button>
      )}
    </div>
  );
}