import { SortDescriptor, Table, TableBody, TableCell, TableColumn, TableHeader, TableRow } from "@nextui-org/react";
import { Key, useCallback, useMemo, useState } from "react";
import Line from "./Line";

type TableList = Record<string, any>

interface ITable<T extends TableList> {
    data: {
        keys: string[]
        table: T[]
    }
}

const getDescriptor = <T extends TableList,>(list: T[]): SortDescriptor => ({ 
    column: list.length === 0 ? '...' : Object.keys(list[0])[0], 
    direction: 'descending' 
})

export default function TableLine<T extends TableList>({ data }: ITable<T>) {

    const { keys, table } = data
    const [descriptor, setDescriptor] = useState<SortDescriptor>(getDescriptor(table))

    const renderCell = useCallback( (t: T, columnKey: Key) => {
        const val = t[columnKey as keyof T]
        const text = Array.isArray(val) ? val[1] : val
        return <Line text={text} />
    }, []);
    
    const sortedItems = useMemo(() => [...table].sort((a, b) => {
        const first = a[descriptor.column as keyof T];
        const second = b[descriptor.column as keyof T];
        let cmp = 0
        if (Array.isArray(first) && Array.isArray(second))
            cmp = first[0] < second[0] ? -1 : 1
        else
            cmp = first < second ? -1 : 1
        return descriptor.direction === "descending" ? -cmp : cmp
    }), [descriptor, data]);

    return (
        <Table
            aria-label="table"
            isHeaderSticky
            sortDescriptor={descriptor}
            onSortChange={setDescriptor}
            classNames={{
                table: 'bg-transparent',
                wrapper: 'bg-background-dark',
                th: 'bg-background text-neutral-300'
            }}
        >
            <TableHeader> 
                { keys.map( k => 
                    <TableColumn key={k} allowsSorting>
                        {k}
                    </TableColumn>
                ) }
            </TableHeader>
            <TableBody
                items={sortedItems} 
            >
                {(item) => (
                    <TableRow key={item.process_id}>
                        {(columnKey) => <TableCell>{renderCell(item, columnKey)}</TableCell>}
                    </TableRow>
                )}
            </TableBody>
        </Table>
    )
}
  