import  { addContent, setAutocompleteContent } from '@/redux/store' 

import terminalFetch from "@/utils/fetch";
import { 
    Status,
    Actions,
    PathResponse,
    ComponentResponses,
    HistoryResponse,
    AutocompletionResponse,
    TerminalState,
    FetchResponse,
    ResponsesComponent,
    ErrorResponse, 
} from "@/types";


const prefillFetch = <T extends FetchResponse, U>(
    endpoint: string, 
    callback: (t: T, state: TerminalState) => U | undefined, 
    body?: object
) => (state: TerminalState) => terminalFetch(
    endpoint, 
    {...state, ...body},
    (res: T | ErrorResponse) => {
        console.log('[prefillFetch - response]', res);
        if (res.name === 'nothing')
            return undefined
        else if (res.name === 'error') {
            addContent({ ...res, session: state.session })
            return undefined
        }    
        else
            return callback(res, state)
    }
)

export const processCommand = prefillFetch(
    '/cmd',
    (res: ComponentResponses | PathResponse, state) => {
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
            setAutocompleteContent({ data: data.propositions, session: state.session })
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
