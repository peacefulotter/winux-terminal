
import { ChangeEventHandler, Dispatch, KeyboardEventHandler, SetStateAction, useEffect, useRef, useState } from "react"
import { useTerminal } from "@/context/TerminalContext";
import BaseCommandLine from "./BaseCommandLine";
import FixedCommandLine from "./FixedCommandLine";


export default function CommandLine() {

    const [fixedData, setFixedData] = useState({cmd: '', path: '', isFixed: false})
    const { cmd, path, actions, setCmd } = useTerminal()

    const ref = useRef<HTMLInputElement>(null);

    useEffect(() => {
        if (ref.current) {
            ref.current.focus();
        }
    }, []);

    const onChange: ChangeEventHandler<HTMLInputElement> = (e) => {
      	setCmd(e.target.value)
    }
  
    const onKeyDown: KeyboardEventHandler<HTMLInputElement> = (e) => {
        if (Object.keys(actions).includes(e.key)) {
            e.preventDefault()
            e.stopPropagation()
            actions[e.key]().then(isFixed => {
                setFixedData({isFixed, cmd, path})
            })
        }
    }
    
    return fixedData.isFixed
        ? <FixedCommandLine cmd={fixedData.cmd} path={fixedData.path} />
        : <BaseCommandLine path={path}> 
            <input 
                ref={ref}
                className='bg-transparent text-foreground outline-none w-full' 
                type='text' 
                value={cmd} 
                onChange={onChange}
                onKeyDown={onKeyDown} />
        </BaseCommandLine>
}
  