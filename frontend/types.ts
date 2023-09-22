export enum Status { Success, Error, Nothing }
export type ResName = 'empty' | 'line' | 'list' | 'flex' | 'path' | 'input'

export type Response<T> = {
  status: Status,
  name: ResName,
  data: T;
}

export type Actions = Record<string, () => void>
