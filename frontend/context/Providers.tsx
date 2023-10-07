import { PropsWithChildren } from "react";
import { NextUIProvider } from "@nextui-org/react";
import { SSEProvider } from "./SSEContext";
import { Provider } from "react-redux";
import store from "@/redux/store";

export default function Providers({ children }: PropsWithChildren ) {
    return (
        <NextUIProvider>
            <SSEProvider>
                <Provider store={store}>
                    { children }
                </Provider>
            </SSEProvider>
        </NextUIProvider>
    
    )
}