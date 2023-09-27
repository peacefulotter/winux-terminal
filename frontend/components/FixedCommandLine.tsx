
import { TerminalState } from "@/types"
import BaseCommandLine from "./BaseCommandLine"

export default function FixedCommandLine({path, cmd}: TerminalState) {
    return (
        <BaseCommandLine path={path} className='border-b-indigo-950'>
            <div>{cmd}</div>
        </BaseCommandLine>
    )
}
  