'use client'
import CommandLine from "@/components/CommandLine"
import ConnectionStatus from "@/components/ConnectionStatus"
import Welcome from "@/components/Welcome"
import useSSE from "@/hooks/useSSE"
import useTerminal from "@/hooks/useTerminal"

export default function Home() {

  const { sse, status } = useSSE()
  const { cmd, path, setCmd, actions } = useTerminal()

  return (
    <main className="flex h-screen flex-col items-center justify-between px-32 py-16 font-mono">
      <Welcome />
      <div className='w-full h-full rounded-3xl p-2 bg-gradient-to-br from-fuchsia-800 to-sky-800 mt-3'>
        <div className='w-full h-full flex flex-col rounded-2xl border border-neutral-800 bg-background overflow-hidden'>
          <ConnectionStatus status={status}/>
          <div className='px-5 py-3'>
            <CommandLine cmd={cmd} path={path} setCmd={setCmd} actions={actions} />
          </div>
        </div>
      </div>
      
    </main>
  )
}
