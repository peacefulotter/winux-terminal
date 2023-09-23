'use client'
import CommandLine from "@/components/CommandLine"
import ConnectionStatus from "@/components/ConnectionStatus"
import Terminal from "@/components/Terminal"
import Welcome from "@/components/Welcome"
import { TerminalProvider } from "@/context/TerminalContext"
import useSSE from "@/hooks/useSSE"

export default function Home() {
  return (
    <main className="flex h-screen flex-col items-center justify-center px-32 gap-2 font-mono overflow-hidden">
      <Welcome />
      <div className='w-full rounded-3xl p-2 bg-gradient-to-br from-fuchsia-800 to-sky-800 mt-3'>
        <div className='w-full h-[70vh] flex flex-col rounded-2xl border border-neutral-800 bg-background overflow-hidden'>
          <TerminalProvider>
            <ConnectionStatus />
            <Terminal />
          </TerminalProvider>
        </div>
      </div>
    </main>
  )
}
