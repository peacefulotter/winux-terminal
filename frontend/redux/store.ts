import { PayloadAction, configureStore, createSlice } from "@reduxjs/toolkit"
import { TerminalState, UIResponse } from "@/types"


const Slice = createSlice({
    name: 'lines',
    initialState: [] as UIResponse[],
    reducers: {
        add: (state, { payload }: PayloadAction<UIResponse>) => {
            console.log("adding content ", payload);
            state.push(payload)
        },
        addFixedCmd: (state, { payload }: PayloadAction<TerminalState>) => {
            state.push({ name: 'cmd', data: payload })
        }
    }
} )

const { reducer, actions } = Slice
const store = configureStore({ reducer })

export default store
export type RootState = ReturnType<typeof store.getState>

export const addContent = (payload: UIResponse) => 
    store.dispatch(actions.add(payload))

export const addFixedCmd = (payload: TerminalState) => 
    store.dispatch(actions.addFixedCmd(payload))
