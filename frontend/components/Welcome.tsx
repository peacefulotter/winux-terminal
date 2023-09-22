


export default function Welcome() {
    return (
        <div className='leading-5 w-min shadow-2xl rounded-3xl p-4 -mt-12'>
            <div className='text-pink-500'    >{`    __     __     __     __   __     __  __     __  __   `.replace(/ /g, "\u00A0")}</div>
            <div className='text-fuchsia-500' >{`   /\\ \\  _ \\ \\   /\\ \\   /\\ "-.\\ \\   /\\ \\/\\ \\   /\\_\\_\\_\\  `.replace(/ /g, "\u00A0")}</div>
            <div className='text-violet-500'  >{`   \\ \\ \\/ ".\\ \\  \\ \\ \\  \\ \\ \\-.  \\  \\ \\ \\_\\ \\  \\/_/\\_\\/_ `.replace(/ /g, "\u00A0")}</div>
            <div className='text-indigo-500'  >{`    \\ \\__/".~\\_\\  \\ \\_\\  \\ \\_\\\\"\\_\\  \\ \\_____\\   /\\_\\/\\_\\`.replace(/ /g, "\u00A0")}</div>
            <div className='text-blue-500'    >{`     \\/_/   \\/_/   \\/_/   \\/_/ \\/_/   \\/_____/   \\/_/\\/_/`.replace(/ /g, "\u00A0")}</div>
        </div>
    )
}