import {
	createContext,
	Dispatch,
	PropsWithChildren,
	SetStateAction,
	useContext,
	useEffect,
	useState,
} from "react";
import  { addContent } from '@/redux/store' 

import useSSE from "@/hooks/useSSE";
import terminalFetch from "@/utils/fetch";
import { 
    Status,
    Actions, 
    FetchResponse,
    PathResponse,
    ContentResponse,
    HistoryResponse,
    AutocompletionResponse,
    CmdAction, 
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

    const fetch = <T extends FetchResponse,>(endpoint: string, callback: (data: T) => boolean) => () => {
		return terminalFetch(endpoint, { cmd, path }, callback)
	}
    // Prevent sending request if cmd is empty
    const emptyMiddleware = ( f: CmdAction ) => () => {
        if (cmd.trim().length === 0) {
            return Promise.resolve(true);
        }
        return f()
    }

    const arrowCallback = ({status, data}: HistoryResponse) => {
        if (status === Status.Success)
            setCmd(data)
        return false
    }
    
    const actions: Actions = { 
		'Tab': fetch('/autocomplete', ({status, data}: AutocompletionResponse) => {
            if (status === Status.Success) {
                setCmd(data.autocompletion)
                if ( data.propositions !== null)
                    addContent({name: 'flex', data: data.propositions})
            }
            return false
        } ),
		'Enter': emptyMiddleware( fetch('/cmd', (res: ContentResponse | PathResponse) => {
            console.log(res)
            if ( res.name === 'path' ) {
                setPath(res.data)
            }
            else
                addContent(res)

            setCmd('')
            return true
        })),
		'ArrowUp': fetch('/history/up', arrowCallback),
		'ArrowDown': fetch('/history/down', arrowCallback),
    }

    useEffect( () => {
        if (sse === undefined) return;
        sse.onmessage = (test) => {
            console.log('SSE ', test);
            const { data } = test
            
            const res = JSON.parse(data)
            addContent(res)
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