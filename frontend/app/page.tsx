'use client'

import TabsProvider from '@/components/TabsProvider';
import Terminal from '@/components/Terminal';
import Providers from '@/context/Providers';
import Header from '@/components/Header';

export default function Home() {
	return (
		<Providers>
			<main className="flex h-screen flex-col items-center justify-center px-32 gap-4 font-mono overflow-hidden">
				<Header />
				<div className='relative w-full h-[80vh] rounded-2xl flex flex-col bg-background-950 overflow-hidden'>
					<TabsProvider>
						{session => <Terminal session={session}/>}
					</TabsProvider>
				</div>
			</main>
		</Providers>
	)
}
