import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, test } from 'vitest'; // Added test here
import StudyView from './StudyView';

const mockCards = [
  { id: '1', term: 'Apple', definition: 'A red fruit' }
];

describe('StudyView Component', () => {
  test('renders the first card term', () => {
    render(<StudyView cards={mockCards} onComplete={() => {}} />);
    expect(screen.getByText('Apple')).toBeInTheDocument();
  });

  test('flips the card to show definition when X is clicked', () => {
    render(<StudyView cards={mockCards} onComplete={() => {}} />);
    
    const crossBtn = screen.getByText('X');
    fireEvent.click(crossBtn);

    expect(screen.getByText('A red fruit')).toBeInTheDocument();
    expect(screen.getByText('Next Word')).toBeInTheDocument();
  });
});