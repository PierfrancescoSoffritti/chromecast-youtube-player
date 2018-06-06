const INIT_COMMUNICATION_CONSTANTS = "INIT_COMMUNICATION_CONSTANTS";

function SenderMessagesDispatcher(communicationConstants, callbacks, initMessageReceived) {

    function onMessage(message) {
        console.log(message.data)

        if(message.data.command === INIT_COMMUNICATION_CONSTANTS)    
            callbacks.onInitMessageReceived(message.data.communicationConstants);
            
        else if(message.data.command === communicationConstants.LOAD)
            callbacks.loadVideo(message.data.videoId, Number(message.data.startSeconds))
        else if(message.data.command === communicationConstants.PLAY)
            callbacks.playVideo()
        else if(message.data.command === communicationConstants.PAUSE)
            callbacks.pauseVideo()
        
        else if(message.data.command === communicationConstants.SET_VOLUME)
            callbacks.setVolume(Number(message.data.volumePercent))
        else if(message.data.command === communicationConstants.SEEK_TO)
            callbacks.seekTo(Number(message.data.time))
    }

    return {
        onMessage
    }
}

export default SenderMessagesDispatcher;