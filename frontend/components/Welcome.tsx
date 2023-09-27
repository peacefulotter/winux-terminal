


export default function Welcome() {
    return (
        <div className='text-xs font-black leading-3'>
            <div className='text-indigo-400'    >{`    __     __     __     __   __     __  __     __  __   `.replace(/ /g, "\u00A0")}</div>
            <div className='text-blue-400' >{`   /\\ \\  _ \\ \\   /\\ \\   /\\ "-.\\ \\   /\\ \\/\\ \\   /\\_\\_\\_\\  `.replace(/ /g, "\u00A0")}</div>
            <div className='text-sky-400'  >{`   \\ \\ \\/ ".\\ \\  \\ \\ \\  \\ \\ \\-.  \\  \\ \\ \\_\\ \\  \\/_/\\_\\/_ `.replace(/ /g, "\u00A0")}</div>
            <div className='text-cyan-400'  >{`    \\ \\__/".~\\_\\  \\ \\_\\  \\ \\_\\\\"\\_\\  \\ \\_____\\   /\\_\\/\\_\\`.replace(/ /g, "\u00A0")}</div>
            <div className='text-teal-400'    >{`     \\/_/   \\/_/   \\/_/   \\/_/ \\/_/   \\/_____/   \\/_/\\/_/`.replace(/ /g, "\u00A0")}</div>
        </div>
    )
}