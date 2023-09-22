

export default function terminalFetch<T, U>(endpoint: string, body: T, callback: (u: U) => void) {
    fetch(`http://localhost:9000/terminal${endpoint}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(body),
    } ).then(res => res.json()).then(callback)
} 