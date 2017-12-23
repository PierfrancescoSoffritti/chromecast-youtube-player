function SenderMessagesDispatcher() {

    let callbacks;

    function setCallbacks(c) {
        callbacks = c;
    }

    function onMessage(event) {
        console.log(event.data)

        if(event.data.command === CommunicationConstants.LOAD)
            callbacks.loadVideo(event.data.videoId, Number(event.data.startSeconds))
    }

    return {
        setCallbacks,
        onMessage
    }
}