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
    UIResponse, 
} from "@/types";

interface TerminalState { cmd: string, path: string }

interface ContextProps {
    state: TerminalState
    actions: Actions
    status: Status 
    setCmd: (cmd: string) => void;
}

const TerminalContext = createContext({} as ContextProps);

export const TerminalProvider = ({ children }: PropsWithChildren) => {
    const [state, setState] = useState<TerminalState>({ path: 'D:\\', cmd: '' })
    const setCmd = (cmd: string) => setState( ({path}) => ({path, cmd}))
    const setPath = (path: string) => setState( ({cmd}) => ({path, cmd}))

    const { sse, status } = useSSE()

    const fetch = <T extends FetchResponse,>(endpoint: string, callback: (data: T) => boolean) => () => {
		return terminalFetch(endpoint, state, callback)
	}
    // Prevent sending request if cmd is empty
    const emptyMiddleware = ( f: CmdAction ) => () => {
        if (state.cmd.trim().length === 0) {
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
                    addContent({res: {name: 'flex', data: data.propositions}, addCmd: true})
            }
            return false
        } ),
		'Enter': emptyMiddleware( fetch('/cmd', (res: ContentResponse | PathResponse) => {
            console.log(res)
            
            if ( res.name === 'path' ) {
                addContent({addCmd: true})
                setState({ cmd: '', path: res.data })
            }
            else {
                if ( res.status === Status.Nothing )
                    addContent({addCmd: true})
                else 
                    addContent({res, addCmd: true})
                setCmd('')
            }
            
            return true
        })),
		'ArrowUp': fetch('/history/up', arrowCallback),
		'ArrowDown': fetch('/history/down', arrowCallback),
    }

    useEffect( () => {
        if (sse === undefined) return;
        sse.onmessage = (test) => {
            const { data } = test
            const res = JSON.parse(data) as UIResponse
            console.log(res);
            addContent({res, addCmd: false})
        }
    }, [sse])
    
    return (
		<TerminalContext.Provider
			value={{ state, actions, status, setCmd }}
		>
			{children}
		</TerminalContext.Provider>
	);
};

export const useTerminal = () => useContext(TerminalContext);