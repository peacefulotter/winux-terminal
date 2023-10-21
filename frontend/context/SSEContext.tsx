import {
	createContext,
	PropsWithChildren,
	useContext,
	useEffect,
    useState,
} from "react";

import  { addContent, ComponentPayload, replaceContent } from '@/redux/store' 
import { Status } from "@/types";

type SSEResponse = ComponentPayload & { replace?: string }

interface ContextProps {
    status: Status 
}

const SSEContext = createContext({} as ContextProps);

export const SSEProvider = ({ children }: PropsWithChildren) => {

	const [status, setStatus] = useState<Status>(Status.Nothing)

	useEffect( () => {
		const es = new EventSource('http://localhost:9000/terminal/sse')
		es.onopen = () => setStatus(Status.Success)
		es.onerror = () => setStatus(Status.Error)
		es.onmessage = ({ data }) => {
            const res = JSON.parse(data) as SSEResponse // TODO: make sure casting properly 
            console.log('SSE RES: ', res);
			if (res.replace)
				replaceContent(res)
			else
	            addContent(res)
        }
		return () => setStatus(Status.Nothing)
	}, [])
    
    return (
		<SSEContext.Provider
			value={{ status }}
		>
			{children}
		</SSEContext.Provider>
	);
};

export const useSSE = () => useContext(SSEContext);