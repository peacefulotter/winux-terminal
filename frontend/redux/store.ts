import { PayloadAction, configureStore, createSlice } from "@reduxjs/toolkit"
import { TerminalState, UIResponse } from "@/types"

export type RootState = { path: string, content: UIResponse[] }[]
export type PathPayload = { session: number, path: string }
export type AddUIPayload = UIResponse & { session: number }

const DEFAULT_DIR = "D:\\"

const Slice = createSlice({
    name: 'lines',
    initialState: [{ path: DEFAULT_DIR, content: [] }] as RootState,
    reducers: {
        add: (state, { payload }: PayloadAction<AddUIPayload>) => {
            const { session, ...data } = payload
            console.log("adding content ", payload);
            state[session].content.push(data)
        },
        addFixedCmd: (state, { payload }: PayloadAction<TerminalState>) => {
            const { session, ...data } = payload
            state[session].content.push({ name: 'cmd', data })
        },
        setPath: (state, { payload }: PayloadAction<PathPayload>) => {
            const { session, path } = payload
            console.log(session, path, state[session]);
            
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

export const addContent = (payload: AddUIPayload) => 
    store.dispatch(actions.add(payload))

export const addFixedCmd = (payload: TerminalState) => 
    store.dispatch(actions.addFixedCmd(payload))

export const setPath = (payload: PathPayload) => 
    store.dispatch(actions.setPath(payload))

export const addTab = () => 
    store.dispatch(actions.addTab())

export const removeTab = (session: number) => 
    store.dispatch(actions.removeTab(session))
