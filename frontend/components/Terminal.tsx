'use client'
import React, { ReactNode } from "react"
import { useSelector } from "react-redux"

import { RootState } from "@/redux/store"

import CommandLine from "./CommandLine"
import Line from "./Line"
import ListLine from "./ListLine"
import FlexLine from "./FlexLine"
import FixedCommandLine from "./FixedCommandLine"

export default function Terminal() {
    const lines = useSelector((state: RootState) => state)
    return (
        <div className='h-full px-5 py-3 overflow-y-scroll scrollbar-thin'>
            {lines.map( (res, i) => {
                let node: ReactNode;
                switch (res.name) {
                    case 'cmd':
                        node = <CommandLine />
                        break;
                    case 'fixed':
                        node = <FixedCommandLine {...res.data} />
                        break;
                    case "line":
                        node = <Line {...res.data} />
                        break;
                    case "list":
                        node = <ListLine {...res.data} /> 
                        break;
                    case "flex":
                        node = <FlexLine {...res.data} />
                        break
                }
                return node
                    ? <React.Fragment key={`node-${i}`}>{node}</React.Fragment>
                    : null;
            })}
        </div>
    )
}