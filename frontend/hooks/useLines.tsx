import React, { Dispatch, ReactNode, SetStateAction, useState } from "react";

import FixedCommandLine from "@/components/FixedCommandLine";
import CommandLine from "@/components/CommandLine";

interface Props {
	setCmd: Dispatch<SetStateAction<string>>
}

export default function useLines( { setCmd }: Props ) {
    const [lines, setLines] = useState<ReactNode[]>([])

    const getNodeWrapper = (node: ReactNode, len: number) => 
        <React.Fragment key={`node-${len}`}>{node}</React.Fragment>

    const add = (node: ReactNode) => {
        setLines(prev => [...prev, getNodeWrapper(node, prev.length)])
    }

    const addCmdLine = () => add(
        <CommandLine setCmd={setCmd} />
    )

    const addFixedCmdLine = (cmd: string, path: string) => setLines( prev => {
        const temp = [...prev]
        const node = <FixedCommandLine cmd={cmd} path={path} />
        temp[temp.length - 1] = getNodeWrapper(node, temp.length - 1)
        return temp
    } )

	return { lines, addCmdLine, addFixedCmdLine }
}