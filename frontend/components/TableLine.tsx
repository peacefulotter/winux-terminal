import { SortDescriptor, Table, TableBody, TableCell, TableColumn, TableHeader, TableRow } from "@nextui-org/react";
import { Key, useCallback, useMemo, useState } from "react";

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
        return <div className="">{t[columnKey as keyof T]}</div>
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
                <TableColumn key="name" allowsSorting>
                    Name
                </TableColumn>
                <TableColumn key="height" allowsSorting>
                    Height
                </TableColumn>
                <TableColumn key="year">
                    Birth year
                </TableColumn>
            </TableHeader>
            <TableBody
                items={sortedItems} 
            >
                {(item) => (
                    <TableRow key={item.name}>
                        {(columnKey) => <TableCell>{renderCell(item, columnKey)}</TableCell>}
                    </TableRow>
                )}
            </TableBody>
        </Table>
    )
}
  