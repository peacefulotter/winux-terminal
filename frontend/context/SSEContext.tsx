import {
	createContext,
	PropsWithChildren,
	useContext,
	useEffect,
    useState,
} from "react";

import  { addContent, AddUIPayload } from '@/redux/store' 
import { Status, UIResponse } from "@/types";


interface ContextProps {
    status: Status 
}

const SSEContext = createContext({} as ContextProps);

export const SSEProvider = ({ children }: PropsWithChildren) => {

    const [sse, setSSE] = useState<EventSource>()
	const [status, setStatus] = useState<Status>(Status.Nothing)

	useEffect( () => {
		const es = new EventSource('http://localhost:9000/terminal/sse')
		es.onopen = () => setStatus(Status.Success)
		es.onerror = () => setStatus(Status.Error)
		setSSE(es)
		return () => setStatus(Status.Nothing)
	}, [])

    useEffect( () => {
        if (sse === undefined) return;
        sse.onmessage = ({ data }) => {
            const res = JSON.parse(data) as AddUIPayload // TODO: make sure casting properly 
            console.log('SSE RES: ', res);
            addContent(res)
        }
    }, [sse])
    
    return (
		<SSEContext.Provider
			value={{ status }}
		>
			{children}
		</SSEContext.Provider>
	);
};

export const useSSE = () => useContext(SSEContext);