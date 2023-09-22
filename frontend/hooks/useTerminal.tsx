import { useState } from "react";
import useActions from "./useActions";

export default function useTerminal() {
    const [path, setPath] = useState<string>('D:\\')
    const [cmd, setCmd] = useState<string>('')

	const actions = useActions({cmd, path, setCmd, setPath})

	return { cmd, path, setCmd, actions }
}