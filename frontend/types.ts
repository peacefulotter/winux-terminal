import { JSXElementConstructor } from "react";
import { ValueOf } from "next/dist/shared/lib/constants";

import Line from "./components/Line";
import ListLine from "./components/ListLine";
import FlexLine from "./components/FlexLine";
import TableLine from "./components/TableLine";
import Bat from "./components/Bat";
import FixedCommandLine from "./components/FixedCommandLine";

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

// Type of React Component
type Component = JSXElementConstructor<any> | keyof JSX.IntrinsicElements
// Type of a Component Response, that can either take the component props as data or an object
type ComponentResponse<T extends string, U extends Component | object> = 
  Response<T, U extends Component ? React.ComponentProps<U> : U>

// Component Responses
export const ResponsesComponent = {
  'line': Line,
  'error': ListLine,
  'list': ListLine,
  'flex': FlexLine,
  'table': TableLine,
  'bat': Bat,
  'cmd': FixedCommandLine
} as const

export type ComponentResponses = ValueOf<{
  [Name in keyof typeof ResponsesComponent]: ComponentResponse<Name, (typeof ResponsesComponent)[Name]>
}>;

// Other responses that are handled differently (not as straightforward components)
export type PathResponse = Response<'path', string>
export type HistoryResponse = Response<'history', string>
type NothingResponse = Response<'nothing', string>
type ExtractErrorType<T> = T extends { name: "error" } ? T : never;
export type ErrorResponse = ExtractErrorType<ComponentResponses>;
export type AutocompletePropositions = React.ComponentProps<typeof FlexLine>['data'] | null
export type AutocompletionResponse = Response<'autocompletion', {
  autocompletion: string
  propositions: AutocompletePropositions,
}>

// Groups all responses that we can get from the backend
export type FetchResponse = 
  | ComponentResponses 
  | PathResponse 
  | HistoryResponse 
  | AutocompletionResponse 
  | NothingResponse

/** Actions **/
export type CmdAction<T> = (state: TerminalState) => Promise<T>
export type Actions<T = void> = Record<string, CmdAction<T | undefined>>