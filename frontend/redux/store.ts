import { PayloadAction, configureStore, createSlice } from "@reduxjs/toolkit"
import { UIResponse } from "@/types"

const { reducer, actions } = createSlice({
    name: 'lines',
    initialState: [
        { name: 'cmd' }
    ] as UIResponse[],
    reducers: {
        add: (state, action: PayloadAction<UIResponse>) => {
            state.push( action.payload )
            state.push({ name: 'cmd' })
        }
    }
} )

const store = configureStore({ reducer })

export default store
export type RootState = ReturnType<typeof store.getState>

export const addContent = (payload: UIResponse) => 
    store.dispatch(actions.add(payload))