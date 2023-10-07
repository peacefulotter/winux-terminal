
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { oneDark } from "react-syntax-highlighter/dist/cjs/styles/prism";

export default function Bat() {

    const language = 'typescript'
    const file = 'trap.ts'
    const content = [
        'const array = new Array(4).fill([])',
        'array[0].push("once")',
        'console.log(array)'
    ]

    return (
        <div className='grid p-1 mt-2' style={{
            gridTemplateColumns: '100px auto',
            gridTemplateRows: '30px auto'
        }}>
            <div
                className='border-t border-background-600'
                style={{gridColumn: 1, gridRow: 1}}
            >
            </div>
            <div 
                className='flex flex-col items-end pb-2 pt-[10px] pr-4 text-background-400 border-t border-b border-background-600' 
                style={{gridColumn: 1, gridRow: 2}}
            >
                { content.map((_, i) =>
                    <span key={`line-idx-${i}`}>{i}</span>
                )}
            </div>
            <div className='flex items-center gap-1 p-2 border-l border-t border-background-600' style={{gridColumn: 2, gridRow: 1}}>
                <span className='font-bold'>File:</span>
                {file}
            </div>
            <div className='flex flex-col p-2 border-t border-l border-b border-background-600' style={{gridColumn: 2, gridRow: 2}}>
                <SyntaxHighlighter 
                    customStyle={{background: 'var(--tw-background-900)', padding: '0px', margin: '0px'}} 
                    language={language} 
                    style={oneDark}
                >
                    {content.join('\n')}
                </SyntaxHighlighter>
            </div>
        </div>
    )
}