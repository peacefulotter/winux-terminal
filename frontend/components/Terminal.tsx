'use client'
import React, { ReactNode, useMemo } from "react"
import { useSelector } from "react-redux"

import { RootState } from "@/redux/store"

import CommandLine from "./CommandLine"
import Line from "./Line"
import ListLine from "./ListLine"
import FlexLine from "./FlexLine"
import FixedCommandLine from "./FixedCommandLine"
import TableLine from "./TableLine"
import Bat from "./Bat"

interface ITerminal { session: number }

export default function Terminal({ session }: ITerminal) {
    
    const {content, autocomplete} = useSelector((state: RootState) => ({
        content: state[session].content,
        autocomplete: state[session].autocomplete,
    }))

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
            case 'table':
                node = <TableLine {...res} />
                break
        }
        return <React.Fragment key={`node-${i}`}>{node}</React.Fragment>
    }) , [content])
    
    return (
        <div className='flex flex-col-reverse overflow-y-auto min-h-full'>
            <div className='flex flex-col overflow-y-auto scrollbar-thin min-h-full'>
                {terminalLines}
                <CommandLine session={session} />
                {autocomplete && <FlexLine data={autocomplete} />}
                {<Bat />}
            </div>
        </div>
    )
}