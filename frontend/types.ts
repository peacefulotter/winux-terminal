import Line from "./components/Line";
import ListLine from "./components/ListLine";
import FlexLine from "./components/FlexLine";

export enum Status { Success, Error, Nothing }


type Response<T extends string, U> = {
  status: Status,
  name: T,
} & (U extends null | undefined ? {} : { data: U });


type EmtpyResponse = Response<'empty', {}>
type LineResponse = Response<'line', React.ComponentProps<typeof Line>>
type ListResponse = Response<'list', React.ComponentProps<typeof ListLine>>
type FlexResponse = Response<'flex', React.ComponentProps<typeof FlexLine>>

export type ContentResponse = 
  EmtpyResponse | 
  LineResponse |
  ListResponse | 
  FlexResponse
  
export type PathResponse = Response<'path', string>
export type InputResponse = Response<'input', string>
export type PropositionsResponse = Response<'propositions', {
  propositions: React.ComponentProps<typeof FlexLine>,
  cmd: string
}>
export type SSEResponse = LineResponse

export type FetchResponse = ContentResponse | PathResponse | InputResponse | PropositionsResponse
export type FetchResponseData = FetchResponse['data']

export type UIResponse = ContentResponse |
  Response<'fixed', {cmd: string, path: string}> |
  Response<'cmd', undefined>

export type Actions = Record<string, () => void>


export const Color = {
  'success': 'text-success',
  'error': 'text-error',
  'info': 'text-info',
  'file': 'text-file',
  'directory': 'text-directory'
} as const
