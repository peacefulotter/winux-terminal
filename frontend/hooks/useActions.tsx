import { Actions, Response, Status } from "@/types";
import terminalFetch from "@/utils/fetch";
import { Dispatch, SetStateAction, useState } from "react";

interface Props {
  cmd: string;
  path: string;
  setCmd: Dispatch<SetStateAction<string>>
  setPath: Dispatch<SetStateAction<string>>
}

export default function useActions( { cmd, path, setCmd, setPath }: Props ) {

    const callback = ({status, name, data}: Response<any>) => {
		console.log(status, name, data);
		
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
					setCmd('')
					break;
				case 'input':
					console.log("input", data);
					setCmd(data)
					break;
          	}
    }

	const body = { cmd, path }
    const actions: Actions = {
        'Tab': ( ) => terminalFetch('/autocomplete', body, callback),
        'Enter': () => terminalFetch('/cmd', body, callback),
        'ArrowUp': () => terminalFetch('/history/up', body, callback),
        'ArrowDown': () => terminalFetch('/history/down', body, callback),
    }

	return actions
}