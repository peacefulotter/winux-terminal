
import { PropsWithChildren } from "react"

interface IFixedCommandLine {
    path: string;
}

export default function BaseCommandLine( { path, children }: PropsWithChildren<IFixedCommandLine> ) {
    return (
      	<div className='flex gap-2'>
			<p className='text-path whitespace-nowrap'>{path}</p>
			<p className='text-dollar'>$</p>
			{children}
      	</div>
    )
}
  