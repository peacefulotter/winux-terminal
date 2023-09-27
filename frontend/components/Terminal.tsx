'use client'
import React, { ReactNode, useMemo } from "react"
import { useSelector } from "react-redux"

import { RootState } from "@/redux/store"

import CommandLine from "./CommandLine"
import Line from "./Line"
import ListLine from "./ListLine"
import FlexLine from "./FlexLine"
import FixedCommandLine from "./FixedCommandLine"

export default function Terminal() {
    const lines = useSelector((state: RootState) => state)
    // console.log(lines);

    const terminalLines = useMemo( () => lines.map( (res, i) => {
        let node: ReactNode;
        switch (res.name) {
            case 'cmd':
                node = <FixedCommandLine {...res.data} />
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
    }) , [lines])
      
    return (
        <div className='h-full px-5 pt-3 pb-[50%] overflow-y-scroll scrollbar-thin'>
            {terminalLines}
            <CommandLine />
        </div>
    )
}