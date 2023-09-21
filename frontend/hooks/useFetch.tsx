import useSWR, { Fetcher } from "swr";


const fetcher: Fetcher<any, string> = (...args) => fetch(...args).then(res => res.json())

export default function useFetch<T>(path: string) {
    const { data, error, isLoading } = useSWR<T>(path, fetcher)
    return data;
}