import {
	createContext,
	PropsWithChildren,
	useContext,
	useEffect,
} from "react";
import  { addContent, addFixedCmd } from '@/redux/store' 

import useSSE from "@/hooks/useSSE";
import terminalFetch from "@/utils/fetch";
import { 
    Status,
    Actions,
    PathResponse,
    ContentResponse,
    HistoryResponse,
    AutocompletionResponse,
    UIResponse,
    TerminalState, 
} from "@/types";



interface ContextProps {
    contentActions: Actions<{path: string}>
    cmdActions: Actions<{status: Status, cmd: string}>
    status: Status 
}

const TerminalContext = createContext({} as ContextProps);

export const TerminalProvider = ({ children }: PropsWithChildren) => {

    const { sse, status } = useSSE()

    const processCommand = (state: TerminalState) => {
        addFixedCmd(state)

        // Prevent sending request if cmd is empty
        if (state.cmd.trim().length === 0)
            return Promise.resolve({path: state.path})
        
        return terminalFetch(
            '/cmd', state,
            (res: ContentResponse | PathResponse) => {
                console.log(res)
                if ( res.name !== 'path' && res.status !== Status.Nothing )
                    addContent(res)
                return { path: res.name === 'path' ? res.data : state.path }
            }
        )
    }

    const searchHistory = (dir: 'up' | 'down') => (state: TerminalState) => terminalFetch(
        '/history', { ...state, dir },
        ({status, data}: HistoryResponse) => ({ status, cmd: data }), 
    )

    const getAutocompletion = (state: TerminalState) => terminalFetch(
        '/autocomplete', state,
        ({status, data}: AutocompletionResponse) => {
            if (status === Status.Success && data.propositions !== null)
                addContent({ name: 'flex', data: data.propositions })
            const cmd = status === Status.Nothing ? state.cmd : data.autocompletion
            return { status, cmd }
        }
    )
    
    // Actions that modify the terminal content and reset the cmd
    const contentActions: Actions<{path: string}> = { 
		'Enter': processCommand
    }

    // Actions that modify the command itself
    const cmdActions: Actions<{status: Status, cmd: string}> = {
        'Tab': getAutocompletion,
        'ArrowUp': searchHistory('up'),
		'ArrowDown': searchHistory('down')
    }

    useEffect( () => {
        if (sse === undefined) return;
        sse.onmessage = (test) => {
            const { data } = test
            const res = JSON.parse(data) as UIResponse
            console.log('SSE RES: ', res);
            addContent(res)
        }
    }, [sse])
    
    return (
		<TerminalContext.Provider
			value={{ contentActions, cmdActions, status }}
		>
			{children}
		</TerminalContext.Provider>
	);
};

export const useTerminal = () => useContext(TerminalContext);