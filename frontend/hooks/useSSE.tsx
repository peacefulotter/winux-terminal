import { Status } from "@/types";
import { useEffect, useState } from "react";

export default function useSSE() {

	const [sse, setSSE] = useState<EventSource>()
	const [status, setStatus] = useState<Status>(Status.Nothing)

	useEffect( () => {
		const es = new EventSource('http://localhost:9000/terminal/sse')
		es.onopen = () => {
			setStatus(Status.Success)
			console.log('connection is opened');
		}
		es.onerror = () => {
			setStatus(Status.Error)
			console.log('error in opening conn.');
		}
		setSSE(es)
		return () => setStatus(Status.Nothing)
	}, [])

	return { sse, status }
}