import { Status } from "@/types"


interface IConnectionStatus {
    status: Status
}

const states = {
    [Status.Error]: ["#e44", 'Disconnected'],
    [Status.Nothing]: ["#c84", 'Attempting to connect..'], 
    [Status.Success]: ["#5e5", 'Connected'] 
} as const

export default function ConnectionStatus( { status }: IConnectionStatus ) {
    const [color, text] = states[status]
    return (
        <div className='flex items-center gap-3 px-8 py-2 bg-background-dark'>
            <div className='rounded-full h-2 w-2' style={{backgroundColor: color}}></div>
            <div className='text-slate-300 font-bold'>{text}</div>
        </div>
    )
}