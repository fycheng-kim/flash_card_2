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
  const [managingStackId, setManagingStackId] = useState<string | null>(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const pageSize = 3; // You can make this a constant


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
      fetchStacks(currentPage)
//       setStacks(data);
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

  const handleDeleteStack = async (id: string) => {
    if (!window.confirm("Delete this stack and all its cards?")) return;

    const response = await fetch(`http://localhost:8080/api/stacks/${id}`, { method: 'DELETE' });
    if (response.ok) {
      setStacks(stacks.filter(s => s.id !== id));
      setManagingStackId(null);
    }
  };

    const handleUpdateName = async (id: string, newName: string) => {
    await fetch(`http://localhost:8080/api/stacks/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name: newName })
    });
    // Refresh local state or re-fetch
    setManagingStackId(null);
    fetchStacks(currentPage);
    };

    const fetchStacks = async (page: number) => {
    try {
      const response = await fetch(`http://localhost:8080/api/stacks?page=${page}&size=${pageSize}`);
      const data = await response.json();

      // Spring Boot 4 'Page' object structure:
      setStacks(data.content || []);
      setTotalPages(data.totalPages ||  0);
      setCurrentPage(data.number); // 'number' is the current page index from backend
    } catch (error) {
      console.error("Error fetching stacks:", error);
      setStacks([]);
    }
  };

  // Fetch stacks when logged in
  useEffect(() => {
    if (isLoggedIn) {
        fetchStacks(currentPage)
//       fetch(`http://localhost:8080/api/stacks?page=0&size=${pageSize}`)
//         .then(res => res.json())
//         .then(data => setStacks(data.content))
//         .catch(err => console.error("Failed to fetch:", err));
    }
  }, [isLoggedIn, currentPage]);


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
                <div style={{ border: '1px solid gray', margin: '10px 0', padding: '10px'}}>
                <div
                    key={stack.id}
                    onClick={() => handleSelectStack(stack.id)}
                    style={{  margin: '10px 0', padding: '10px', cursor: 'pointer' }}
                >
                    <h3>{stack.name}</h3>
                    </div>
                    <small>Last used: {new Date(stack.lastUsed).toLocaleDateString()}</small>
                    <button
                        onClick={(e) => {
                        e.stopPropagation(); // Don't trigger "Select Stack"
                        setManagingStackId(stack.id);
                        }}
                        style={{margin: 10, right: '10px', top: '10px', cursor: 'pointer' }}
                    >
                      ⚙️
                    </button>

                    {managingStackId === stack.id && (
                        <div className="management-menu">
                            <button onClick={ () => {/*TBD*/} }> +Add Cards </button>
                            <button onClick={ () => {
                                const n = prompt("New name?", stack.name);
                                if(n) handleUpdateName(stack.id, n);
                            }}>Rename</button>
                            <button onClick={ () => handleDeleteStack(stack.id)} style={{color: 'red'}}>Delete</button>
                            <button onClick={ () => setManagingStackId(null)}> Close </button>
                        </div>
                    )}


                </div>

        )))}
        <button onClick={() => setIsCreating(true)}>+ Create Stack</button>

        {/* PAGINATION CONTROLS */}
        <div style={{ marginTop: '20px', display: 'flex', gap: '10px', alignItems: 'center' }}>
            <button
                          onClick={() => setCurrentPage(prev => Math.max(0, prev - 1))}
                          disabled={currentPage === 0}
                        >
                          Previous
                        </button>

                        <span>Page {currentPage + 1} of {totalPages}</span>

                        <button
                          onClick={() => setCurrentPage(prev => Math.min(totalPages - 1, prev + 1))}
                          disabled={currentPage >= totalPages - 1}
                        >
                          Next
                        </button>
        </div>
    </div>

    )
}

export default App;