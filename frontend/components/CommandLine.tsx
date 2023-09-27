
import { ChangeEventHandler, KeyboardEventHandler, useEffect, useRef, useState } from "react"
import { useTerminal } from "@/context/TerminalContext";
import { Status } from "@/types";
import BaseCommandLine from "./BaseCommandLine";

export default function CommandLine() {

    const { cmdActions, contentActions } = useTerminal()
    
    const [path, setPath] = useState<string>('D:\\')
    const [cmd, setCmd] = useState<string>('')
    const [disabled, setDisabled] = useState(false)

    const ref = useRef<HTMLInputElement>(null);

    useEffect(() => {
        if (ref.current && !disabled)
            ref.current.focus();
    }, [disabled]);

    const onChange: ChangeEventHandler<HTMLInputElement> = (e) => {
        setCmd(e.target.value)
    }
  
    const onKeyDown: KeyboardEventHandler<HTMLInputElement> = (e) => {
        if (Object.keys(cmdActions).includes(e.key)) {
            e.preventDefault()
            e.stopPropagation()
            cmdActions[e.key]({path, cmd}).then(({status, cmd}) => {
                if (status === Status.Success)
                    setCmd(cmd)
            })
        }

        else if (Object.keys(contentActions).includes(e.key)) {
            e.preventDefault()
            e.stopPropagation()
            setDisabled(true)
            const prevCmd = cmd
            setCmd('')
            contentActions[e.key]({path, cmd: prevCmd}).then(({path}) => {    
                setPath(path)
                setDisabled(false)
            })
        }
    }
    
    return (
        <BaseCommandLine path={path} className='bg-sky-950'>
            <input 
                disabled={disabled}
                ref={ref}
                className='bg-transparent text-foreground outline-none w-full disabled:hidden' 
                type='text' 
                value={cmd} 
                onChange={onChange}
                onKeyDown={onKeyDown} />
        </BaseCommandLine>
    )
}
  