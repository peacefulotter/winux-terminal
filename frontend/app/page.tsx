'use client'
import { Provider } from 'react-redux';
import { NextUIProvider } from "@nextui-org/react";
import { FiBookmark, FiAirplay, FiCodepen, FiThumbsUp, FiTrendingUp, FiCodesandbox, FiBold, FiItalic, FiCode, FiUnderline, FiLink, FiList, FiImage, FiAnchor } from 'react-icons/fi'

import ConnectionStatus from "@/components/ConnectionStatus"
import Welcome from "@/components/Welcome"
import store from '@/redux/store';
import { SSEProvider } from '@/context/SSEContext';
import TabsProvider from '@/components/TabsProvider';
import Terminal from '@/components/Terminal';
import LumaBtn from '@/components/LumaBtn';


export default function Home() {
  return (
    <NextUIProvider>
    <SSEProvider>
      <main className="dark flex h-screen flex-col items-center justify-center px-32 gap-4 font-mono overflow-hidden">
        <div className='flex gap-8'>
          <div className='flex flex-col gap-2'>
            <Welcome />
            <ConnectionStatus />
          </div>
          <div className='flex flex-wrap gap-1'>
            <LumaBtn theme='blue' icon={FiBookmark}>Bookmarks</LumaBtn>
            <LumaBtn theme='red' icon={FiAirplay}>Imagine</LumaBtn>
            <LumaBtn theme='yellow' icon={FiCodepen}>A new world</LumaBtn>
            <LumaBtn theme='cyan' icon={FiThumbsUp}>Its crazy</LumaBtn>
            <LumaBtn theme='green' icon={FiTrendingUp}>Trending</LumaBtn>
            <LumaBtn theme='purple' icon={FiCodesandbox}>Codesandbox</LumaBtn>
            
            <LumaBtn theme='pink' icon={FiAnchor}></LumaBtn>
            <LumaBtn theme='purple' icon={FiList}></LumaBtn>
            <LumaBtn theme='blue' icon={FiBold}></LumaBtn>
            <LumaBtn theme='cyan' icon={FiCode}></LumaBtn>
            <LumaBtn theme='green' icon={FiLink}></LumaBtn>
            <LumaBtn theme='yellow' icon={FiUnderline}></LumaBtn>
            <LumaBtn theme='orange' icon={FiImage}></LumaBtn>
            <LumaBtn theme='red' icon={FiItalic}></LumaBtn>
          </div>
        </div>
        <div className='relative w-full h-[80vh] rounded-2xl flex flex-col border-2 border-sky-950 bg-background overflow-hidden'>
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
