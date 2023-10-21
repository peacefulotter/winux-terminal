
import { TerminalState } from "@/types"
import BaseCommandLine from "./BaseCommandLine"

type FixedCmdLineProps = Omit<TerminalState, 'session'> 

export default function FixedCommandLine({ path, cmd }: FixedCmdLineProps) {
    return (
        <BaseCommandLine path={path} className='border-b-indigo-950'>
            <div>{cmd}</div>
        </BaseCommandLine>
    )
}
  