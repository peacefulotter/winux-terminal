import { SortDescriptor, Table, TableBody, TableCell, TableColumn, TableHeader, TableRow } from "@nextui-org/react";
import { Key, useCallback, useMemo, useState } from "react";

interface ITable {
}


export default function CTable( {}: ITable ) {
    const list = [
        { name: 'x', height: "300", year: '09/08/1907' },
        { name: 'y', height: "200", year: '01/02/1903' },
        { name: 'z', height: "100", year: '03/04/1905' },
    ]

    const [sortDescriptor, setSortDescriptor] = useState<SortDescriptor>({
        column: "name",
        direction: "descending"
    })

    const renderCell = useCallback( (user: any, columnKey: Key) => {
        switch (columnKey) {
            case "name":
                return <div className="">{user.name}</div>
            case "height":
                return <div className=''>{user.height}</div>
            case "year":
                return <div className=''>{user.year}</div>
        }
    }, []);
    
    const sortedItems = useMemo(() => [...list].sort((a, b) => {
        let first = a[sortDescriptor.column as keyof typeof a];
        let second = b[sortDescriptor.column as keyof typeof b];
        let cmp = (parseInt(first) || first) < (parseInt(second) || second) ? -1 : 1;
        return sortDescriptor.direction === "descending" ? -cmp : cmp
    }), [sortDescriptor, list]);

    return (
        <Table
            aria-label="table"
            isHeaderSticky
            sortDescriptor={sortDescriptor}
            onSortChange={setSortDescriptor}
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
  