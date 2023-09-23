import { Response, Status } from "@/types";
import { Dispatch, SetStateAction, ReactNode } from "react";

interface Props {
	setCmd: Dispatch<SetStateAction<string>>
	setPath: Dispatch<SetStateAction<string>>
	addFixedCmdLine: (cmd: string, path: string) => void;
	addCmdLine: () => void;
}

export default function useCmdCallback( { setCmd, setPath, addFixedCmdLine, addCmdLine }: Props ) {

    const callback = (cmd: string, path: string) => ({status, name, data}: Response<any>) => {
		console.log(status, name, data);
		if (status === Status.Error || (status === Status.Success && name !== 'empty' && name !== 'input')) {
			addFixedCmdLine(cmd, path)
			setCmd('')
		}
		
        if (status === Status.Nothing) 
			return
        else if (status === Status.Error)
          console.log(data);
        else
          	switch (name) {
				case 'empty':
					break;
				case 'line':
					break;
				case 'list':
					break;
				case 'flex':
					break;
				case 'path':
					setPath(data)
					addCmdLine()
					break;
				case 'input':
					console.log("input", data);
					setCmd(data)
					break;
          	}
    }

	return callback
}