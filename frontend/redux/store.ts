import { PayloadAction, configureStore, createSlice } from "@reduxjs/toolkit"
import { ContentResponse, SSEResponse, Status, UIResponse } from "@/types"

type AddPayload = ContentResponse & { cmd: string, path: string } 
type AddSinglePayload = ContentResponse | SSEResponse 

const { reducer, actions } = createSlice({
    name: 'lines',
    initialState: [
        { status: Status.Success, name: 'cmd' }
    ] as UIResponse[],
    reducers: {
        add: (state, action: PayloadAction<AddPayload>) => {
            const { name, cmd, path } = action.payload
            state[state.length - 1] = { name: 'fixed', data: { cmd, path }, status: Status.Success }
            if ( name !== 'empty' )
                state.push( action.payload )
            state.push( { status: Status.Success, name: 'cmd' } )
        },
        addSingle: (state, action: PayloadAction<AddSinglePayload>) => {
            state.push( action.payload )
        }
    }
} )

const store = configureStore({ reducer })

export default store
export type RootState = ReturnType<typeof store.getState>

export const addContent = (payload: AddPayload) => store.dispatch(actions.add(payload))
export const addSingle = (res: AddSinglePayload) => store.dispatch(actions.addSingle(res))