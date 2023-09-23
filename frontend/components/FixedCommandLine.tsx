
import { PropsWithChildren } from "react"
import BaseCommandLine from "./BaseCommandLine";

interface IFixedCommandLine {
    cmd: string;
    path: string;
}

export default function FixedCommandLine( { cmd, path }: IFixedCommandLine ) {
    return (
      	<BaseCommandLine path={path}>
            <div>{cmd}</div>
        </BaseCommandLine>
    )
}
  