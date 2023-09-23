

interface ILine {
    text: string;
    color?: string | undefined;
}

export default function Line( { text, color }: ILine ) {
    return (
      	<div className={`${color} whitespace-nowrap max-w-min`}>
			{ text }
      	</div>
    )
}
  