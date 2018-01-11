function SenderMessagesDispatcher(communicationConstants, callbacks) {

    function onMessage(event) {
        console.log(event.data)

        if(event.data.command === communicationConstants.LOAD)
            callbacks.loadVideo(event.data.videoId, Number(event.data.startSeconds))
        else if(event.data.command === communicationConstants.PLAY)
            callbacks.playVideo()
        else if(event.data.command === communicationConstants.PAUSE)
            callbacks.pauseVideo()
    }

    return {
        onMessage
    }
}

export default SenderMessagesDispatcher;