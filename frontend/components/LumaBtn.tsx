
import { raleway } from '@/fonts';
import { MouseEventHandler, PropsWithChildren, useRef, useState } from 'react';
import { IconType } from 'react-icons';

const themes = {
    pink: {
        from: 'from-pink-500/90',
        via: 'via-pink-500/0',
        active: 'group-active:bg-pink-500/50',
        bg: 'bg-pink-600/30' ,
        text: 'text-pink-600',
        fill: 'fill-pink-600'
    },
    blue: {
        from: 'from-blue-500/90',
        via: 'via-blue-500/0',
        active: 'group-active:bg-blue-500/50',
        bg: 'bg-blue-600/30' ,
        text: 'text-blue-600',
        fill: 'fill-blue-600'
    },
    red: {
        from: 'from-red-500/90',
        via: 'via-red-500/0',
        active: 'group-active:bg-red-500/50',
        bg: 'bg-red-600/20' ,
        text: 'text-red-600/80',
        fill: 'fill-red-600/80'
    },
    cyan: {
        from: 'from-cyan-500/90',
        via: 'via-cyan-500/0',
        active: 'group-active:bg-cyan-500/50',
        bg: 'bg-cyan-600/20' ,
        text: 'text-cyan-600/80',
        fill: 'fill-cyan-600/80'
    },
    yellow: {
        from: 'from-yellow-500/90',
        via: 'via-yellow-500/0',
        active: 'group-active:bg-yellow-500/50',
        bg: 'bg-yellow-600/20' ,
        text: 'text-yellow-600/80',
        fill: 'fill-yellow-600/80'
    },
    orange: {
        from: 'from-orange-500/90',
        via: 'via-orange-500/0',
        active: 'group-active:bg-orange-500/50',
        bg: 'bg-orange-600/20' ,
        text: 'text-orange-600/80',
        fill: 'fill-orange-600/80'
    },
    green: {
        from: 'from-green-500/90',
        via: 'via-green-500/0',
        active: 'group-active:bg-green-500/50',
        bg: 'bg-green-600/20' ,
        text: 'text-green-600/80',
        fill: 'fill-green-600/80'
    },
    purple: {
        from: 'from-purple-500/90',
        via: 'via-purple-500/0',
        active: 'group-active:bg-purple-500/50',
        bg: 'bg-purple-600/20' ,
        text: 'text-purple-600/80',
        fill: 'fill-purple-600/80'
    }
} as const

interface ILumaBtn {
    theme: keyof typeof themes
    icon: IconType,
    fill?: boolean
}

export default function LumaBtn( { theme, fill, icon: Icon, children }: PropsWithChildren<ILumaBtn> ) {

    const parentRef = useRef<HTMLButtonElement>(null)
    const gradientRef = useRef<HTMLDivElement>(null)
    const [pos, setPos] = useState({x: 0, y: 0})

    const onMouseOver: MouseEventHandler<HTMLDivElement> = (e) => {
        const parentBounds = (parentRef.current as HTMLButtonElement).getBoundingClientRect();
        const gradientBounds = (gradientRef.current as HTMLDivElement).getBoundingClientRect();
        const  x = e.clientX - parentBounds.left - gradientBounds.width / 2;
        const  y = e.clientY - parentBounds.top - gradientBounds.height / 2;
        setPos({x, y})
    }

    const { from, via, active, bg, text, fill: fillClass } = themes[theme]

    return (
        <button className='relative overflow-hidden h-min font-sans group' ref={parentRef} style={{borderRadius: 'calc(0.75rem + 1px)'}}>
            <div className='absolute -inset-3 saturate-150 transition-opacity'>
                <div className={`absolute -inset-48 top-0 left-0 bg-gradient-radial ${from} ${via} to-blue-500/0 ${active} transition-[background,opacity] ease-in-out opacity-0 group-hover:opacity-100`}
                    style={{transform: `translate(${Math.round(pos.x)}px, ${Math.round(pos.y)}px)`}}
                    ref={gradientRef}
                >
                </div>
            </div>
            <div className='absolute inset-[1px] bg-background-950/80 backdrop-blur-lg rounded-xl'></div>
            <div className='relative flex flex-1 inset-0 justify-start items-center gap-3 rounded-lg px-3 py-2 z-50' 
                onMouseMove={onMouseOver}
            >
                <div className={`${bg} ${text} p-2 rounded-lg text-xl brightness-110`}>
                    <Icon size={20} style={{ strokeWidth: 2 }} className={fill ? fillClass : ''} />
                </div>
                { children && <div className={`text-left text-neutral-100 ${raleway.className} pr-4`}>
                    { children }
                </div> }
            </div>
        </button>
    )
}