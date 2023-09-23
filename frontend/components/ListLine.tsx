import Line from "./Line";


interface IListLine {
    data: string[];
}

export default function ListLine( { data }: IListLine ) {
    return (
      	<div className=''>
			{ data.map( text => 
                <Line text={text} />
            ) }
      	</div>
    )
}
  