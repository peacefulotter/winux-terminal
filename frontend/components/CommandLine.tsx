
import { ChangeEventHandler, KeyboardEvent, KeyboardEventHandler, useEffect, useRef, useState } from "react"
import { cmdActions, contentActions } from "@/redux/actions";
import { Actions, Status } from "@/types";
import BaseCommandLine from "./BaseCommandLine";
import { useSelector } from "react-redux";
import { RootState, addFixedCmd, setAutocompleteContent, setPath } from "@/redux/store";

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
            setAutocompleteContent({ data: null, session })
            contentActions[e.key]({ path, cmd: prevCmd, session }).then(res => {
                if (res !== undefined) {
                    console.log("Content action command line: ", res);
                    setPath({ session, path: res.path })
                }    
                setDisabled(false)
            })
        }
    }

    if (disabled)
        return null
    
    return (
        <BaseCommandLine path={path} className='bg-background-900 p-2 border border-transparent rounded-lg my-2 focus-within:border-neutral-100/10 transition-colors'>
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
  