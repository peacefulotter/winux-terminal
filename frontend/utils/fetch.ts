

export default function terminalFetch<T, U>(endpoint: string, body: T, callback: (u: U) => boolean): Promise<boolean> {
  return new Promise<boolean>( async (resolve, reject) => {
    const res = await fetch(`http://localhost:9000/terminal${endpoint}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(body),
    } )
    const json = await res.json() as U
    resolve(callback(json))
  } )
} 