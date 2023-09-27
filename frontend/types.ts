import Line from "./components/Line";
import ListLine from "./components/ListLine";
import FlexLine from "./components/FlexLine";

export enum Status { Success, Error, Nothing }

export type TerminalState = { 
  path: string 
  cmd: string, 
}

type Response<T extends string, U> = {
  status: Status,
  name: T
} & (U extends null | undefined ? { data?: U } : { data: U })

type LineResponse = Response<'line', React.ComponentProps<typeof Line>['text']>
type ListResponse = Response<'list', React.ComponentProps<typeof ListLine>['data']>
type FlexResponse = Response<'flex', React.ComponentProps<typeof FlexLine>['data']>

export type ContentResponse = LineResponse
  | ListResponse 
  | FlexResponse
  
export type PathResponse = Response<'path', string>
export type HistoryResponse = Response<'history', string>
export type AutocompletionResponse = Response<'autocompletiob', {
  autocompletion: string
  propositions: React.ComponentProps<typeof FlexLine>['data'] | null,
}>

export type FetchResponse = ContentResponse | PathResponse | HistoryResponse | AutocompletionResponse
export type FetchResponseData = FetchResponse['data']

// https://dev.to/safareli/pick-omit-and-union-types-in-typescript-4nd9
type Keys<T> = keyof T;
type DistributiveKeys<T> = T extends unknown ? Keys<T> : never;
export type DistributiveOmit<T, K extends DistributiveKeys<T>> =
  T extends unknown ? Omit<T, Extract<keyof T, K>> : never;

type FixedCmdResponse = Response<"cmd", TerminalState>
export type UIResponse = DistributiveOmit<
  (ContentResponse | FixedCmdResponse), 'status'
>

export type CmdAction<T> = (state: TerminalState) => Promise<T>
export type Actions<T = void> = Record<string, CmdAction<T>>
