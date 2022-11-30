import BackgroundService from "react-native-background-actions";

const startListener = async (taskData) => {
    await new Promise(async (resolve) => {
        /* const detector = new CallDetector();
        detector.startListenerTapped(); */
    });
};

const options = {
    taskName: "listen_call",
    taskTitle: "Listening for calls",
    taskDesc: "Looking out for incoming calls to alert nearby computers",
    taskIcon: {
        name: "ic_launcher",
        type: "mipmap",
    },
    color: "#fc5531",
};

export const setupBackgroundTasks = async () => {
    console.log("INIT call detector");
    await BackgroundService.start(startListener, options);
    await BackgroundService.updateNotification({
        taskDesc: "New ExampleTask description",
    });
    // await BackgroundService.stop();
};
