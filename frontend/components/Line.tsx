import Ansi from "ansi-to-react";

interface ILine {
    text: string;
    color?: keyof typeof Color;
    fromFlex?: boolean
}


export const Color = {
    'foreground': 'text-foreground',
    'success': 'text-success',
    'error': 'text-error',
    'info': 'text-info',
    'file': 'text-file',
    'directory': 'text-directory'
} as const

export default function Line( { text, color, fromFlex }: ILine ) {
    const colorClass = color ? Color[color] : Color['foreground']
    const flexClass = fromFlex ? 'max-w-min whitespace-nowrap' : 'w-full'
    return (
        <div className={`${colorClass} ${flexClass}`}>
            <Ansi useClasses>{text}</Ansi>
        </div>      	
    )
}
  