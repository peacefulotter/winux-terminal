import { ActionCreatorWithPayload, ActionCreatorWithoutPayload, PayloadAction, configureStore, createSlice } from "@reduxjs/toolkit"
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

// type ActionCreator<T, U extends string = string> = T extends undefined 
//     ? ActionCreatorWithoutPayload<U> 
//     : ActionCreatorWithPayload<T, U>  

// function actionWithoutPayload<U extends string = string>(
//     action: ActionCreator<any, U>
// ): action is ActionCreatorWithoutPayload<U>  
// {
//     return action.length === 0;
// }

// function actionWithPayload<T, U extends string>(
//     action: ActionCreator<any, U>
// ): action is ActionCreatorWithPayload<T, U>  
// {
//     return action.length === 1;
// }

// TODO: find way to group these two functions together
const exportReducerWithPayload = <T, U extends string = string>(action: ActionCreatorWithPayload<T, U>) => {
    return (t: T) => store.dispatch(action(t));
} 

const exportReducer = <U extends string = string,>(action: ActionCreatorWithoutPayload<U>) => {
    return () => store.dispatch(action())
}

export const addContent = exportReducerWithPayload(actions.add)
export const replaceContent = exportReducerWithPayload(actions.replace)
export const addFixedCmd = exportReducerWithPayload(actions.addFixedCmd)
export const setPath = exportReducerWithPayload(actions.setPath)
export const addTab = exportReducer(actions.addTab)
export const removeTab = exportReducerWithPayload(actions.removeTab)
