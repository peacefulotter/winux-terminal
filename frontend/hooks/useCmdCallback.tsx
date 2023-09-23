import { Color, Response, Status } from "@/types";
import { Dispatch, SetStateAction, ReactNode } from "react";

interface Props {
	setCmd: Dispatch<SetStateAction<string>>
	setPath: Dispatch<SetStateAction<string>>
	addLine: (text: string, color?: string) => void;
	addList: (data: string[]) => void;
	addFlex: (data: [string, boolean][]) => void;
	addFixedCmdLine: (cmd: string, path: string) => void;
	addCmdLine: () => void;
}

export default function useCmdCallback( { setCmd, setPath, addLine, addList, addFlex, addFixedCmdLine, addCmdLine }: Props ) {

	// TODO: refactor
	// add(
	//	{ type: 'cmd', text: ...},
	// { type: 'line', text: ...},
	// { type: 'flex': data: }       { type: ResType, ...Props[ResType] }
	// )

    const callback = (cmd: string, path: string) => ({status, name, data}: Response<any>, noNewLine?: boolean) => {
		console.log(status, name, data);
		if (status === Status.Error || (status === Status.Success && name !== 'empty' && name !== 'input')) {
			addFixedCmdLine(cmd, path)
			setCmd('')
		}

		let newLine = true
		
        if (status === Status.Error) {
			console.log(data);
			addLine(data, Color.error)
		}
        else if (status === Status.Success)
          	switch (name) {
				case 'empty':
					newLine = false
					break;
				case 'line':
					addLine(data)
					break;
				case 'list':
					addList(data)
					break;
				case 'flex':
					addFlex(data)
					break;
				case 'path':
					setPath(data)
					break;
				case 'input':
					setCmd(data)
					newLine = false
					break;
          	}

		if ( !noNewLine && newLine )
			addCmdLine()
    }

	return callback
}