import useCmdCallback from "@/hooks/useCmdCallback";
import useLines from "@/hooks/useLines";
import useSSE from "@/hooks/useSSE";
import { Actions, Status } from "@/types";
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
    const { lines, addCmdLine, addFixedCmdLine } = useLines({ setCmd })
    const callback = useCmdCallback({ setCmd, setPath, addFixedCmdLine, addCmdLine })

    const fetch = (endpoint: string) => () => {
		terminalFetch(endpoint, { cmd, path }, callback(cmd, path))
	}
    const actions: Actions = {
		'Tab': fetch('/autocomplete'),
		'Enter': fetch('/cmd'),
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
            console.log(json);
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