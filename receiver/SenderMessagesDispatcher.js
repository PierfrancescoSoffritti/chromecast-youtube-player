function SenderMessagesDispatcher(communicationConstants, callbacks) {

    function onMessage(event) {
        console.log(event.data)

        if(event.data.command === communicationConstants.LOAD)
            callbacks.loadVideo(event.data.videoId, Number(event.data.startSeconds))
    }

    return {
        onMessage
    }
}

export default SenderMessagesDispatcher;