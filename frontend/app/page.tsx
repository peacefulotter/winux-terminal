'use client'
import { Provider } from 'react-redux';
import { NextUIProvider } from "@nextui-org/react";

import ConnectionStatus from "@/components/ConnectionStatus"
import Welcome from "@/components/Welcome"
import store from '@/redux/store';
import { SSEProvider } from '@/context/SSEContext';
import TabsProvider from '@/components/TabsProvider';
import Terminal from '@/components/Terminal';


export default function Home() {
  return (
    <NextUIProvider>
    <SSEProvider>
      <main className="flex h-screen flex-col items-center justify-center px-32 gap-8 font-mono overflow-hidden">
        <div className='flex gap-4'>
          <Welcome />
          <ConnectionStatus />
        </div>
        <div className='w-full h-[80vh] rounded-2xl flex flex-col border-2 border-sky-950 bg-background overflow-hidden'>
          <Provider store={store}>
            <TabsProvider>
              {session => <Terminal session={session}/>}
            </TabsProvider>
          </Provider>
        </div>
      </main>
    </SSEProvider>
    </NextUIProvider>
  )
}
