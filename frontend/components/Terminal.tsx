import { useTerminal } from "@/context/TerminalContext"

export default function Terminal() {
    const { lines } = useTerminal()
    return (
        <div className='px-5 py-3'>
            {lines}
        </div>
    )
}