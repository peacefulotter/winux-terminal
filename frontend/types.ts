export enum Status { Success, Error, Nothing }
export type ResName = 'empty' | 'line' | 'list' | 'flex' | 'path' | 'input'

export type Response<T> = {
  status: Status,
  name: ResName,
  data: T;
}

export type Actions = Record<string, () => void>


export const Color = {
  'success': 'text-success',
  'error': 'text-error',
  'info': 'text-info',
  'file': 'text-file',
  'directory': 'text-directory'
} as const