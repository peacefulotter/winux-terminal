import { useState, useEffect, useRef } from 'react'

import { Color } from "@/types";
import Line from "./Line";


interface IFlexLine {
    data: [string, boolean][];
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
      	<div ref={ref} className='grid' style={{gridTemplateColumns}}>
			{ data.map( ([text, isDir]) => 
                <Line text={text} color={isDir ? Color.directory : Color.file} />
            ) }
      	</div>
    )
}
  