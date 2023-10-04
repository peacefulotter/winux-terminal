import { SortDescriptor, Table, TableBody, TableCell, TableColumn, TableHeader, TableRow } from "@nextui-org/react";
import { Key, useCallback, useMemo, useState } from "react";
import Line from "./Line";

type TableList = Record<string, string>

interface ITable<T> {
    data: T[]
}

const getDescriptor = <T extends TableList,>(list: T[]): SortDescriptor => ({ 
    column: list.length === 0 ? '...' : Object.keys(list[0])[0], 
    direction: 'descending' 
})

export default function TableLine<T extends TableList>({ data }: ITable<T>) {

    const [descriptor, setDescriptor] = useState<SortDescriptor>(getDescriptor(data))

    const renderCell = useCallback( (t: T, columnKey: Key) => {
        return <Line text={t[columnKey as keyof T]} />
    }, []);
    
    const sortedItems = useMemo(() => [...data].sort((a, b) => {
        const first = a[descriptor.column as keyof T];
        const second = b[descriptor.column as keyof T];
        const cmp = (parseInt(first) || first) < (parseInt(second) || second) ? -1 : 1;
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
                { Object.keys(sortedItems[0]).map( k => 
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
  