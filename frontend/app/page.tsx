'use client'
import { Provider } from 'react-redux';
import ConnectionStatus from "@/components/ConnectionStatus"
import Terminal from "@/components/Terminal"
import Welcome from "@/components/Welcome"
import { TerminalProvider } from "@/context/TerminalContext"
import store from '@/redux/store';

export default function Home() {
  return (
    <main className="flex h-screen flex-col items-center justify-center px-32 gap-8 font-mono overflow-hidden">
      <Welcome />
      <div className='w-full h-[80vh] rounded-2xl flex flex-col border-2 border-sky-950 bg-background overflow-hidden'>
        <Provider store={store}>
          <TerminalProvider>
            <ConnectionStatus />
            <Terminal />
          </TerminalProvider>
        </Provider>
      </div>
    </main>
  )
}
