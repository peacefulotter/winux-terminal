'use client'
import React, { ReactNode, useMemo } from "react"
import { useSelector } from "react-redux"

import { RootState } from "@/redux/store"

import CommandLine from "./CommandLine"
import Line from "./Line"
import ListLine from "./ListLine"
import FlexLine from "./FlexLine"
import FixedCommandLine from "./FixedCommandLine"

interface ITerminal { session: number }

export default function Terminal({ session }: ITerminal) {
    
    const content = useSelector((state: RootState) => state[session].content)

    const terminalLines = useMemo( () => content.map( (res, i) => {
        let node: ReactNode;
        switch (res.name) {
            case 'cmd':
                node = <FixedCommandLine {...res.data} session={session} />
                break;
            case "line":
                node = <Line text={res.data} />
                break;
            case "list":
                node = <ListLine {...res} /> 
                break;
            case "flex":
                node = <FlexLine {...res} />
                break
        }
        return <React.Fragment key={`node-${i}`}>{node}</React.Fragment>
    }) , [content])
      
    return (
        <div className='pb-[50%] overflow-y-scroll scrollbar-thin'>
            {terminalLines}
            <CommandLine session={session} />
        </div>
    )
}