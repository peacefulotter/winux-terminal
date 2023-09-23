import { useTerminal } from "@/context/TerminalContext"

export default function Terminal() {
    const { lines } = useTerminal()
    return (
        <div className='h-full px-5 py-3 overflow-y-scroll scrollbar-thin'>
            {lines}
        </div>
    )
}