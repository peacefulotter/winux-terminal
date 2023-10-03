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

const addTabItem = { session: -1, label: '+' }

const tabsSelector = createSelector(
    (state: RootState) => state, 
    s => s.map((s, i) => ({ session: i, label: s.path }))
)

export default function TabsProvider({ children }: ITabsProvider) {

    const [selected, setSelected] = useState<string>("0");

    const state = useSelector((state: RootState) => state);
    const tabs = tabsSelector(state)

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
        <div className="p-2 flex w-full flex-col text-white max-h-full">
            <Tabs 
                aria-label="Dynamic tabs" 
                variant='light' 
                items={[...tabs, addTabItem]} 
                selectedKey={selected}
                onSelectionChange={onSelectionChange} 
                classNames={{
                    panel: 'h-full overflow-y-scroll ',
                    tab: 'bg-background-dark data-[selected=true]:bg-background-dark/60 data-[selected=true]:border border-sky-950 last:w-12',
                    cursor: 'bg-transparent rounded-lg',
                    tabList: 'w-full rounded-none',
                    tabContent: "w-fit group-data-[selected=true]:text-path last:text-neutral-300",
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