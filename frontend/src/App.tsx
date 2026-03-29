import { useState, useEffect } from 'react';
import StudyView from './components/StudyView';

interface CardStack {
  id: string;
  name: string;
  lastUsed: string;
}

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [stacks, setStacks] = useState<CardStack[]>([]);
  const [selectedCards, setSelectedCards] = useState<any[] | null>(null);
  const [isCreating, setIsCreating] = useState(false);
  const [newStackName, setNewStackName] = useState('');

  const handleCreateStack = async () => {
    const response = await fetch('http://localhost:8080/api/stacks', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name: newStackName, lastUsed: new Date().toISOString() })
    });

    if (response.ok) {
      setIsCreating(false);
      setNewStackName('');
      // Refresh the list (you could also use a library like TanStack Query here)
      const data = await fetch('http://localhost:8080/api/stacks').then(res => res.json());
      setStacks(data);
    }
  };

  const handleSelectStack = async (stackId: string) => {
    try {
      const response = await fetch(`http://localhost:8080/api/stacks/${stackId}/cards`);
      if (response.ok) {
        const cards = await response.json();
        setSelectedCards(cards);
      }
    } catch (error) {
      console.error("Error fetching cards:", error);
    }
  };

  // Fetch stacks when logged in
  useEffect(() => {
    if (isLoggedIn) {
      fetch('http://localhost:8080/api/stacks')
        .then(res => res.json())
        .then(data => setStacks(data))
        .catch(err => console.error("Failed to fetch:", err));
    }
  }, [isLoggedIn]);

  if (!isLoggedIn) {
    return (
      <div style={{ textAlign: 'center', padding: '50px' }}>
        <h1>Flashcard App</h1>
        <button onClick={() => setIsLoggedIn(true)}>Enter App</button>
      </div>
    );
  }

  if (selectedCards) {
    return <StudyView cards={selectedCards} onComplete={() => setSelectedCards(null)} />;
  }

    if (isCreating) {
    return (
      <div style={{ padding: '20px' }}>
        <h3>Create New Stack</h3>
        <input 
          placeholder="Stack Name" 
          value={newStackName}
          onChange={(e) => setNewStackName(e.target.value)}
        />
        <button onClick={handleCreateStack}>Save</button>
        <button onClick={() => setIsCreating(false)}>Cancel</button>
      </div>
    );
  }

  return (
    <div style={{ padding: '20px' }}>
      <h2>My Card Stacks</h2>

      {stacks.length === 0 ? (
        <div>
          <p>No card stacks yet.</p>
          <button onClick={() => setIsCreating(true)}>+ Create Your First Stack</button>
        </div>
      ) : (
        stacks.map(stack => (
          <div 
            key={stack.id} 
            onClick={() => handleSelectStack(stack.id)}
            style={{ border: '1px solid blue', margin: '10px 0', padding: '10px', cursor: 'pointer' }}
          >
            <h3>{stack.name}</h3>
            <small>Last used: {new Date(stack.lastUsed).toLocaleDateString()}</small>
          </div>
        ))
      )}
    <button onClick={() => setIsCreating(true)}>+ Create Stack</button>
    </div>
  );
}

export default App;