import { FetchResponse, Status } from "@/types"


export default function terminalFetch<T, U extends FetchResponse, V>(
  endpoint: string, 
  body: T,
  callback: (u: U) => V,
)
: Promise<V> 
{
  return new Promise( async (resolve, _) => {
    try {
      const res = await fetch(`http://localhost:9000/terminal${endpoint}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(body),
      } )
      const json = await res.json() as U
      resolve(callback(json))
    }
    catch (e: any) {
      const json = { status: Status.Error, name: 'error', data: e.message as string } as U
      resolve(callback(json))
    }
  } )
} 