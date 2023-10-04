import Line from "./components/Line";
import ListLine from "./components/ListLine";
import FlexLine from "./components/FlexLine";
import TableLine from "./components/TableLine";

// https://dev.to/safareli/pick-omit-and-union-types-in-typescript-4nd9
type Keys<T> = keyof T;
type DistributiveKeys<T> = T extends unknown ? Keys<T> : never;
export type DistributiveOmit<T, K extends DistributiveKeys<T>> =
  T extends unknown ? Omit<T, Extract<keyof T, K>> : never;



export enum Status { Success, Error, Nothing }
export type Session = number
export type TerminalState = { 
  path: string 
  cmd: string
  session: Session
}

/** Responses **/
type Response<T extends string, U> = {
  status: Status,
  name: T
} & (U extends null | undefined ? { data?: U } : { data: U })

type LineResponse = Response<'line', React.ComponentProps<typeof Line>['text']>
type ListResponse = Response<'list', React.ComponentProps<typeof ListLine>['data']>
type FlexResponse = Response<'flex', React.ComponentProps<typeof FlexLine>['data']>
type TableResponse = Response<'table', React.ComponentProps<typeof TableLine>['data']>
type ErrorResponse = Response<'error', string>
type FixedCmdResponse = Response<'cmd', Omit<TerminalState, 'session'>>
export type PathResponse = Response<'path', string>
export type HistoryResponse = Response<'history', string>
export type AutocompletionResponse = Response<'autocompletiob', {
  autocompletion: string
  propositions: React.ComponentProps<typeof FlexLine>['data'] | null,
}>

export type ContentResponse = 
  | LineResponse
  | ListResponse 
  | FlexResponse
  | TableResponse
  
export type FetchResponse = ContentResponse | PathResponse | HistoryResponse | AutocompletionResponse | ErrorResponse
export type FetchResponseData = FetchResponse['data']

export type UIResponse = DistributiveOmit<
  (ContentResponse | FixedCmdResponse), 'status'
>

/** Actions **/
export type CmdAction<T> = (state: TerminalState) => Promise<T>
export type Actions<T = void> = Record<string, CmdAction<T | undefined>>