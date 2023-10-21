import Ansi from "ansi-to-react";

interface ILine {
    text: string;
    color?: string;
    fromFlex?: boolean
}


export const colorClasses = {
    'foreground': 'text-foreground',
    'success': 'text-success',
    'error': 'text-error',
    'info': 'text-info',
    'file': 'text-file',
    'directory': 'text-directory'
} as const

export default function Line( { text, color, fromFlex }: ILine ) {
    const colorClass = color && color in colorClasses 
        ? colorClasses[color as keyof typeof colorClasses]
        : colorClasses['foreground']

    const flexClass = fromFlex ? 'max-w-min whitespace-nowrap' : 'w-full'
    
    return (
        <div className={`${colorClass} ${flexClass}`}>
            <pre><Ansi useClasses>{text}</Ansi></pre>
        </div>      	
    )
}
  