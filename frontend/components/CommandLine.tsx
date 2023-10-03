
import { ChangeEventHandler, KeyboardEvent, KeyboardEventHandler, useEffect, useRef, useState } from "react"
import { cmdActions, contentActions } from "@/redux/actions";
import { Actions, Status } from "@/types";
import BaseCommandLine from "./BaseCommandLine";
import { useSelector } from "react-redux";
import { RootState, addFixedCmd, setPath } from "@/redux/store";

interface ICommandLine {
    session: number;
}

const activateActionMiddleware = <T,>(a: Actions<T>, e: KeyboardEvent<HTMLInputElement>) => {
    const res = Object.keys(a).includes(e.key)
    if (res) {
        e.preventDefault()
        e.stopPropagation()
    }
    return res
}

export default function CommandLine({ session }: ICommandLine) {    

    const path = useSelector((state: RootState) => state[session].path)
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
        if (activateActionMiddleware(cmdActions, e)) {
            cmdActions[e.key]({path, cmd, session}).then(res => {
                if (res !== undefined && res.status === Status.Success)
                    setCmd(res.cmd)
            })
        }

        else if (
            activateActionMiddleware(contentActions, e) &&
            cmd.trim().length > 0
        ) {
            addFixedCmd({ cmd, path, session })
            setDisabled(true)
            const prevCmd = cmd
            setCmd('')
            contentActions[e.key]({path, cmd: prevCmd, session}).then(res => {
                if (res === undefined) return    
                console.log("Content action command line: ", res);
                setPath({ session, path: res.path })
                setDisabled(false)
            })
        }
    }
    
    return (
        <BaseCommandLine path={path} className='bg-sky-950 p-2 rounded-lg my-2'>
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
  