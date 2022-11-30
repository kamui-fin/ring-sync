import { useEffect } from "react";
import CallDetectorManager from "react-native-call-detection";

class CallDetector {
    private callDetector: CallDetectorManager;

    startListenerTapped() {
        this.callDetector = new CallDetectorManager(
            (event: string, phoneNumber: string) => {
                console.log(event, phoneNumber);
                if (event === "Incoming") {
                } else if (event === "Missed") {
                }
            },
            true,
            console.error,
            {
                title: "Phone State Permission",
                message:
                    "This app needs access to your phone state in order to react and/or to adapt to incoming calls.",
            }
        );
    }

    stopListenerTapped() {
        this.callDetector && this.callDetector.dispose();
    }
}

const App = () => {
    const detector = new CallDetector();

    useEffect(() => {
        detector.startListenerTapped();
        return detector.stopListenerTapped;
    }, []);

    return <></>;
};

export default App;
