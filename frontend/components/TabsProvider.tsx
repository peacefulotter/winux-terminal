import { Key, ReactNode, useState } from "react"
import { Tabs, Tab } from "@nextui-org/react";
import { RootState, addTab } from "@/redux/store";
import { createSelector } from "@reduxjs/toolkit";
import { useSelector } from "react-redux";

type Session = number
type Item = { session: Session, label: string }

interface ITabsProvider {
    children: (session: Session) => ReactNode
}

const addTabItem: Item = { session: -1, label: '+' }

const tabsSelector = createSelector(
    (state: RootState) => state, 
    s => s.map((s, i) => ({ session: i, label: s.path }))
)

export default function TabsProvider({ children }: ITabsProvider) {

    const [selected, setSelected] = useState<string>("0");

    const state = useSelector((state: RootState) => state);
    const tabs: Item[] = tabsSelector(state)

    const onSelectionChange = (key: Key) => {
        if (key == addTabItem.session) {
            const len = tabs.length
            addTab()
            setSelected(len.toString())
        }
        else
            setSelected(key as string)
    }

    return (
        <div className="p-2 flex w-full flex-col text-foreground max-h-full after:absolute after:bottom-1 after:w-full after:h-6 after:bg-gradient-to-t after:from-background-950 after:via-background-950/60 after:to-transparent after:shadow-xl after:z-50">
            <Tabs 
                aria-label="Dynamic tabs" 
                variant='light' 
                items={[...tabs, addTabItem]} 
                selectedKey={selected}
                onSelectionChange={onSelectionChange} 
                classNames={{
                    base: 'shadow-xl shadow-background-950 z-40 max-w-full',
                    panel: 'h-full overflow-y-scroll shadow-terminal',
                    tab: 'bg-background-900 last:w-12 py-5 group',
                    cursor: 'text-foreground rounded-lg bg-background-800 border border-background-300/20 group-aria-selected:border-accent-700',
                    tabList: 'w-full max-w-full rounded-none overflow-x-hidden',
                    tabContent: "w-fit group-data-[selected=true]:font-bold group-data-[selected=true]:text-foreground text-foreground overflow-hidden text-ellipsis",
                }}
            >
                {({ session, label }) => (
                    <Tab key={session} title={label}>
                        {session >= 0 
                            ? children(session)
                            : <></>
                        }
                    </Tab>
                )}
            </Tabs>
        </div>  
    )
}