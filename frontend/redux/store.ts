import { Action, AnyAction, Dispatch, Draft, Middleware, PayloadAction, configureStore, createSlice } from "@reduxjs/toolkit"
import { TerminalState, UIResponse } from "@/types"

export type RootState = { path: string, content: UIResponse[] }[]
export type PathPayload = { session: number, path: string }
export type UIPayload = UIResponse & { session: number }

const DEFAULT_DIR = "D:\\"

const Slice = createSlice({
    name: 'lines',
    initialState: [{ path: DEFAULT_DIR, content: [] }] as RootState,
    reducers: {
        add: (state, { payload }: PayloadAction<UIPayload>) => {
            const { session, ...data } = payload
            console.log("[store] adding content ", payload);
            state[session].content.push(data)
        },
        replace: (state, { payload }: PayloadAction<UIPayload>) => {
            const { session, ...data } = payload
            console.log("[store] replacing content", payload);
            state[session].content.splice(-1, 1, data)
        },
        addFixedCmd: (state, { payload }: PayloadAction<TerminalState>) => {
            const { session, ...data } = payload
            state[session].content.push({ name: 'cmd', data })
        },
        setPath: (state, { payload }: PayloadAction<PathPayload>) => {
            const { session, path } = payload
            state[session].path = path
        },
        addTab: (state) => {
            state.push({ path: DEFAULT_DIR, content: [] })
        },
        removeTab: (state, { payload }: PayloadAction<number>) => {
            // state.remove(payload.session)
        }
    }
} )

const { reducer, actions } = Slice
const store = configureStore({ reducer })

export default store
// export type RootState = ReturnType<typeof store.getState>

const exportReducer = <T,U>(
    action: (payload: T) => ({payload: T, type: U})
) => {
    return (t: T) => store.dispatch(action(t))
}


export const addContent = exportReducer(actions.add)
export const replaceContent = exportReducer(actions.replace)
export const addFixedCmd = exportReducer(actions.addFixedCmd)
export const setPath = exportReducer(actions.setPath)
export const addTab = exportReducer(actions.addTab)
export const removeTab = exportReducer(actions.removeTab)
