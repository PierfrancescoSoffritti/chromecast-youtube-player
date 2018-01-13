function SenderMessagesDispatcher(communicationConstants, callbacks) {

    function onMessage(event) {
        console.log(event.data)

        if(event.data.command === communicationConstants.LOAD)
            callbacks.loadVideo(event.data.videoId, Number(event.data.startSeconds))
        else if(event.data.command === communicationConstants.PLAY)
            callbacks.playVideo()
        else if(event.data.command === communicationConstants.PAUSE)
            callbacks.pauseVideo()
        
        else if(event.data.command === communicationConstants.MUTE)
            callbacks.mute()
        else if(event.data.command === communicationConstants.UNMUTE)
            callbacks.unMute()
        else if(event.data.command === communicationConstants.SET_VOLUME)
            callbacks.setVolume(Number(event.data.volumePercent))
        else if(event.data.command === communicationConstants.SEEK_TO)
            callbacks.seekTo(Number(event.data.time))
    }

    return {
        onMessage
    }
}

export default SenderMessagesDispatcher;