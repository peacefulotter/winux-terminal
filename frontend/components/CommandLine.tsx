
import { Actions } from "@/types";
import { ChangeEventHandler, Dispatch, KeyboardEventHandler, SetStateAction } from "react"

interface ICommandLine {
    cmd: string;
    path: string;
    setCmd: Dispatch<SetStateAction<string>>
    actions: Actions
}

export default function CommandLine( { cmd, path, setCmd, actions }: ICommandLine ) {

    const onChange: ChangeEventHandler<HTMLInputElement> = (e) => {
      	setCmd(e.target.value)
    }
  
    const keyDownHandler: KeyboardEventHandler<HTMLInputElement> = (e) => {
        if (Object.keys(actions).includes(e.key)) {
            e.preventDefault()
            e.stopPropagation()
            actions[e.key]()
        }
    }
  
    return (
      	<div className='flex gap-2'>
			<p className='text-path whitespace-nowrap'>{path}</p>
			<p className='text-dollar'>$</p>
			<input 
				className='bg-transparent text-foreground outline-none w-full' 
				type='text' 
				value={cmd} 
				onChange={onChange} 
				onKeyDown={keyDownHandler}/>
      	</div>
    )
}
  