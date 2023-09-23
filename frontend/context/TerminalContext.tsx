import useCmdCallback from "@/hooks/useCmdCallback";
import useLines from "@/hooks/useLines";
import useSSE from "@/hooks/useSSE";
import { Actions, Response, Status } from "@/types";
import terminalFetch from "@/utils/fetch";
import {
	createContext,
	PropsWithChildren,
	ReactNode,
	useContext,
	useEffect,
	useState,
} from "react";


interface ContextProps {
    cmd: string;
    path: string;
    lines: ReactNode[]
    actions: Actions
    status: Status
}

const TerminalContext = createContext({} as ContextProps);

export const TerminalProvider = ({ children }: PropsWithChildren) => {
    const [path, setPath] = useState<string>('D:\\')
    const [cmd, setCmd] = useState<string>('')

    const { sse, status } = useSSE()
    const { lines, addLine, addList, addFlex, addCmdLine, addFixedCmdLine } = useLines({ setCmd })
    const callback = useCmdCallback({ setCmd, setPath, addLine, addList, addFlex, addFixedCmdLine, addCmdLine })

    const fetch = (endpoint: string) => () => {
		terminalFetch(endpoint, { cmd, path }, callback(cmd, path))
	}
    // Prevent sending request if cmd is empty
    const emptyMiddleware = ( f: () => void ) => {
        return () => cmd.trim().length === 0 
            ? (function() { addFixedCmdLine(cmd, path); addCmdLine(); })()   
            : f()
    }
    const actions: Actions = {
		'Tab': fetch('/autocomplete'),
		'Enter': emptyMiddleware( fetch('/cmd') ),
		'ArrowUp': fetch('/history/up'),
		'ArrowDown': fetch('/history/down'),
    }

    useEffect( () => {
        addCmdLine()
    }, [])
    
    useEffect( () => {
        if (sse === undefined) return;
        sse.onmessage = ({data}) => {
            const json = JSON.parse(data)
            callback(cmd, path)(json as Response<any>, true)
        }
    }, [sse])
    
    return (
		<TerminalContext.Provider
			value={{ cmd, path, actions, lines, status }}
		>
			{children}
		</TerminalContext.Provider>
	);
};

export const useTerminal = () => useContext(TerminalContext);