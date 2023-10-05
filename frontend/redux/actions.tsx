import  { addContent } from '@/redux/store' 

import terminalFetch from "@/utils/fetch";
import { 
    Status,
    Actions,
    PathResponse,
    ContentResponse,
    HistoryResponse,
    AutocompletionResponse,
    TerminalState,
    FetchResponse, 
} from "@/types";

const prefillFetch = <T extends FetchResponse, U>(
    endpoint: string, 
    callback: (t: T, state: TerminalState) => U | undefined, 
    body?: object
) => (state: TerminalState) => terminalFetch(
    endpoint, 
    {...state, ...body},
    (res: T) => {
        if (res.name === 'error') {
            console.log('[actions - res: error]', res);
            addContent({ name: 'line', data: res.data, session: state.session })
            return undefined
        }    
        else
            return callback(res, state)
    }
)

export const processCommand = prefillFetch(
    '/cmd',
    (res: ContentResponse | PathResponse, state) => {
        console.log('[processCommand]', res, state)
        if ( res.name !== 'path' && res.status !== Status.Nothing )
            addContent({...res, session: state.session})
        return { path: res.name === 'path' ? res.data : state.path }
    }
)

const searchHistory = (dir: 'up' | 'down') => prefillFetch(
    '/history',
    ({status, data}: HistoryResponse) => ({ status, cmd: data }), 
    { dir }
)

const getAutocompletion = prefillFetch(
    '/autocomplete',
    ({status, data}: AutocompletionResponse, state) => {
        if (status === Status.Success && data.propositions !== null)
            addContent({ name: 'flex', data: data.propositions, session: state.session })
        const cmd = status === Status.Nothing ? state.cmd : data.autocompletion
        return { status, cmd }
    }
)
    
// Actions that modify the terminal content and reset the cmd
export const contentActions: Actions<{path: string}> = { 
    'Enter': processCommand
}

// Actions that modify the command itself
export const cmdActions: Actions<{status: Status, cmd: string}> = {
    'Tab': getAutocompletion,
    'ArrowUp': searchHistory('up'),
    'ArrowDown': searchHistory('down')
}
