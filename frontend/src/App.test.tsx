import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { vi, expect, it } from 'vitest';
import App from './App';

// Mock the global fetch
global.fetch = vi.fn();

it('fetches and displays card stacks from the backend', async () => {
  const mockStacks = [
    { id: '1', name: 'Java Basics', lastUsed: '2026-03-26T10:00:00' }
  ];

  (fetch as any).mockResolvedValue({
    ok: true,
    json: async () => mockStacks,
  });

  render(<App />);

//   Click login to get to the dashboard
  const loginBtn = screen.getByText(/Enter App/);
  loginBtn.click();

  // The app should now show the stack from the "backend"
  await waitFor(() => {
    expect(screen.getByText('Java Basics')).toBeInTheDocument();
  });
});

it('allows user to create a new stack when none exist', async () => {
  // 1. Mock empty response from backend
  (fetch as any).mockResolvedValueOnce({
    ok: true,
    json: async () => [], 
  });

  render(<App />);
  fireEvent.click(screen.getByText(/Enter App/i));

  // 2. Expect to see the "Create" button
  const createBtn = await screen.findByText(/\+ Create Your First Stack/i);
  fireEvent.click(createBtn);

  // 3. Fill out the form
  const input = screen.getByPlaceholderText(/Stack Name/i);
  fireEvent.change(input, { target: { value: 'New Test Stack' } });
  
  // 4. Mock the POST request
  (fetch as any).mockResolvedValueOnce({ ok: true });
  fireEvent.click(screen.getByText(/Save/i));

  // 5. Verify fetch was called with POST
  await waitFor(() => {
    expect(fetch).toHaveBeenCalledWith(
      expect.stringContaining('/api/stacks'),
      expect.objectContaining({ method: 'POST' })
    );
  });
});

it('fetches cards and enters StudyView when a stack is clicked', async () => {
  const mockStacks = [{ id: '1', name: 'Java Basics', lastUsed: '2026-03-26T10:00:00' }];
  const mockCards = [{ id: '101', front: 'What is JVM?', back: 'Java Virtual Machine' }];

  // 1. Mock the stacks list
  (fetch as any).mockResolvedValueOnce({ ok: true, json: async () => mockStacks });
  
  render(<App />);
  fireEvent.click(screen.getByText(/Enter App/i));

  // 2. Mock the cards fetch for when the stack is clicked
  (fetch as any).mockResolvedValueOnce({ ok: true, json: async () => mockCards });

  const stackElement = await screen.findByText('Java Basics');
  fireEvent.click(stackElement);

  // 3. Verify StudyView is rendered (Assuming StudyView shows the front of the card)
  expect(await screen.findByText('What is JVM?')).toBeInTheDocument();
});