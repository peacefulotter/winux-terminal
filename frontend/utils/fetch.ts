import { FetchResponse, TerminalState } from "@/types"


export default function terminalFetch<T extends TerminalState, U extends FetchResponse, V>(
  endpoint: string, 
  body: T,
  callback: (u: U) => V,
)
: Promise<V> 
{
  return new Promise( async (resolve, _) => {
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