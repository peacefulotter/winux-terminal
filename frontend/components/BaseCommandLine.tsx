
import { Status } from "@/types"
import { PropsWithChildren } from "react"

interface Props {
    path: string
    className: string
}

// const statusEmojiMap: Record<Status, string> = {
//     [Status.Success]: '✔️',
//     [Status.Error]:   '❌',
//     [Status.Nothing]: '', // ⚠️
// }

export default function BaseCommandLine({path, className, children}: PropsWithChildren<Props>) {
    return (
        <div className={`${className} flex gap-2 justify-between`}>
            <div className='flex gap-2 w-full'>
                <p className='text-path whitespace-nowrap'>{path}</p>
                <p className='text-dollar'>$</p>
                {children}
            </div>
			<div>✔️</div>
      	</div>
    )
}
  