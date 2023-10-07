import { useState, useEffect, useRef } from 'react'

import Line from "./Line";


interface IFlexLine {
    data: [string, boolean][];
}

function isStringBooleanTuple(value: any): value is [string, boolean] {
    return Array.isArray(value) && value.length === 2 && typeof value[0] === 'string' && typeof value[1] === 'boolean';
}

export default function FlexLine( { data }: IFlexLine ) {
    const ref = useRef<HTMLDivElement>(null)
    const [gridTemplateColumns, setGridTemplateColumns] = useState('')

    useEffect( () => {
        if (ref.current === null) return;

        const children = ref.current.querySelectorAll('div')
        let maxParagraphWidth = 0;
    
        children.forEach( child => {
            const paragraphWidth = child.offsetWidth;
            if (paragraphWidth > maxParagraphWidth) {
                maxParagraphWidth = paragraphWidth;
            }
        });
    
        setGridTemplateColumns(`repeat(auto-fill, minmax(${maxParagraphWidth}px, 1fr))`)
    }, [])
    

    return (
      	<div ref={ref} className='grid gap-x-1' style={{gridTemplateColumns}}>
			{ data.map( (elt, i) => {
                if (isStringBooleanTuple(elt))
                    return <Line key={`line-${i}`} text={elt[0]} color={elt[1] ? 'directory' : 'file'} fromFlex={true} />
                else
                    return <Line key={`line-${i}`} text={elt} fromFlex={true} />
            }) }
      	</div>
    )
}
  