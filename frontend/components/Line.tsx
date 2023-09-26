
interface ILine {
    text: string;
    color?: keyof typeof Color;
}


export const Color = {
    'foreground': 'text-foreground',
    'success': 'text-success',
    'error': 'text-error',
    'info': 'text-info',
    'file': 'text-file',
    'directory': 'text-directory'
} as const
  
export default function Line( { text, color }: ILine ) {
    const colorClass = color ? Color[color] : Color['foreground']
    return (
      	<div className={`${colorClass} max-w-min whitespace-nowrap`}>
			<pre>{text}</pre>
      	</div>
    )
}
  