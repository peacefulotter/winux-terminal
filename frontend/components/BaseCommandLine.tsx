
import { PropsWithChildren } from "react"

interface Props {
    path: string
    className: string
}

export default function BaseCommandLine({path, className, children}: PropsWithChildren<Props>) {
    return (
        <div className={`${className} flex gap-2`}>
			<p className='text-path whitespace-nowrap'>{path}</p>
			<p className='text-dollar'>$</p>
			{children}
      	</div>
    )
}
  