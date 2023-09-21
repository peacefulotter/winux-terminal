'use client'
import { ChangeEventHandler, KeyboardEventHandler, useEffect, useState } from "react"

const CommandLine = () => {

  const [cmd, setCmd] = useState<string>('')
  
  const onChange: ChangeEventHandler<HTMLInputElement> = (e) => {
    setCmd(e.target.value)
  }

  const actions: Record<string, () => void> = {
    'Tab': ( ) => console.log('Tab'),
    'Enter': ( ) => fetch('http://localhost:9000/terminal/cmd', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({cmd}),
    }),
    'ArrowUp': () => console.log('ArrowUp'),
    'ArrowDown': () => console.log('ArrowDown'),
  }

  const keyDownHandler: KeyboardEventHandler<HTMLInputElement> = (e) => {
    if (Object.keys(actions).includes(e.key)) {
      e.preventDefault()
      e.stopPropagation()
      actions[e.key]()
    }
  }

  return (
    <div className='flex gap-2 font-mono'>
      <p className='text-path'>D:/Scala/projects</p>
      <p className='text-dollar'>$</p>
      <input 
        className='bg-transparent text-foreground outline-none w-full' 
        type='text' 
        value={cmd} 
        onChange={onChange} 
        onKeyDown={keyDownHandler}/>
    </div>
  )
}


export default function Home() {

  const [sse, setSSE] = useState<EventSource>()

  useEffect( () => {
    const es = new EventSource('http://localhost:9000/terminal/sse')
    es.onopen = () => console.log('connection is opened');
    es.onerror = () => console.log('error in opening conn.');
    es.onmessage = (event) => {
      console.log('got message..',event)
      console.log( JSON.parse(event.data) )
    }
    setSSE(es)
  }, [])

  return (
    <main className="flex h-screen flex-col items-center justify-between p-32">
      <div className='rounded-2xl w-full h-full border border-white bg-background p-4'>
        <CommandLine />
      </div>
    </main>
  )
}
