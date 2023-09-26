import { PayloadAction, configureStore, createSlice } from "@reduxjs/toolkit"
import { UIResponse } from "@/types"


type Payload = {
    res?: UIResponse,
    addCmd: boolean
}

const Slice = createSlice({
    name: 'lines',
    initialState: [
        { name: 'cmd' }
    ] as UIResponse[],
    reducers: {
        add: (state, action: PayloadAction<Payload>) => {
            console.log("adding content ", action.payload);
            if (action.payload.res)
                state.push( action.payload.res )
            if (action.payload.addCmd)
                state.push({ name: 'cmd' })
        }
    }
} )

const { reducer, actions } = Slice
const store = configureStore({ reducer })

export default store
export type RootState = ReturnType<typeof store.getState>

export const addContent = (payload: Payload) => 
    store.dispatch(actions.add(payload))
