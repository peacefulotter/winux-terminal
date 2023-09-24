import {
	createContext,
	Dispatch,
	PropsWithChildren,
	SetStateAction,
	useContext,
	useEffect,
	useState,
} from "react";
import  { addContent, addSingle } from '@/redux/store' 

import useSSE from "@/hooks/useSSE";
import terminalFetch from "@/utils/fetch";
import { 
    Status,
    Actions, 
    FetchResponseData, 
    InputResponse, 
    PropositionsResponse, 
    SSEResponse,
    FetchResponse,
    PathResponse,
    ContentResponse, 
} from "@/types";


interface ContextProps {
    cmd: string;
    path: string;
    actions: Actions
    status: Status 
    setCmd: Dispatch<SetStateAction<string>>
}

const TerminalContext = createContext({} as ContextProps);

export const TerminalProvider = ({ children }: PropsWithChildren) => {
    const [path, setPath] = useState<string>('D:\\')
    const [cmd, setCmd] = useState<string>('')

    const { sse, status } = useSSE()

    const fetch = <T extends FetchResponse,>(endpoint: string, callback: (data: T) => void) => () => {
		terminalFetch(endpoint, { cmd, path }, callback)
	}
    // Prevent sending request if cmd is empty
    const emptyMiddleware = ( f: () => void ) => {
        return () => cmd.trim().length === 0 
            ? addContent({status: Status.Success, name: 'empty', data: {}, cmd, path})  
            : f()
    }
    const arrowCallback = ({status, data}: InputResponse) => {
        if (status === Status.Success)
            setCmd(data)
    }
    const actions: Actions = {
		'Tab': fetch('/autocomplete', (res: InputResponse | PropositionsResponse) => {
            if (res.status !== Status.Success) return;
            if (res.name === 'input') 
                setCmd(res.data)
            else {
                addContent({status: res.status, name: 'flex', data: res.data.propositions, cmd, path})
                setCmd(res.data.cmd)
            }
        } ),
		'Enter': emptyMiddleware( fetch('/cmd', (res: ContentResponse | PathResponse) => 
            res.name === 'path'
                ? setPath(res.data) 
                : addContent( {...res, cmd, path})
        ) ),
		'ArrowUp': fetch('/history/up', arrowCallback),
		'ArrowDown': fetch('/history/down', arrowCallback),
    }

    useEffect( () => {
        if (sse === undefined) return;
        sse.onmessage = ({data}) => {
            const res = JSON.parse(data) as SSEResponse
            addSingle(res)
        }
    }, [sse])
    
    return (
		<TerminalContext.Provider
			value={{ cmd, path, actions, status, setCmd }}
		>
			{children}
		</TerminalContext.Provider>
	);
};

export const useTerminal = () => useContext(TerminalContext);