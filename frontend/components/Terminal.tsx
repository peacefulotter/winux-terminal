'use client'
import React, { useMemo } from "react"
import { useSelector } from "react-redux"

import { RootState } from "@/redux/store"

import CommandLine from "./CommandLine"
import FlexLine from "./FlexLine"
import Bat from "./Bat"
import { ResponsesComponent } from "@/types"
import Line from "./Line"

interface ITerminal { session: number }

export default function Terminal({ session }: ITerminal) {
    
    const {content, autocomplete} = useSelector((state: RootState) => state[session])

    const terminalLines = useMemo( () => content.map( (res, i) => {
        const Node = ResponsesComponent[res.name]
        const node = Node === undefined 
            ? <Line text={`unknown res.name for component: ${res.name}, this is likely an issue with the backend`} color="error"></Line>
            : <Node {...res.data as any} /> // find a workaround for as any?
        return <React.Fragment key={`node-${i}`}>{node}</React.Fragment>
    }) , [content])
    
    return (
        <div className='flex flex-col-reverse overflow-y-auto min-h-full'>
            <div className='flex flex-col overflow-y-auto scrollbar-thin min-h-full'>
                {terminalLines}
                <CommandLine session={session} />
                {autocomplete && <FlexLine data={autocomplete} />}
                {<Bat lang='typescript' file='test.ts' content={['function foo(a: number) {}']}/>}
            </div>
        </div>
    )
}