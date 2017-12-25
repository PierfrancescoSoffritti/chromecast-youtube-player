function YouTubePlayerRemoteBridge() {

    const channel = new CustomChannel();

    function sendYouTubeIframeAPIReady() {
        channel.sendMessage(new YouTubeMessage(CommunicationConstants.IFRAME_API_READY))
    }

    function sendReady() {
        channel.sendMessage(new YouTubeMessage(CommunicationConstants.READY))
    }

    function sendPlayerStateChange(data) {

    }

    function sendPlaybackQualityChange(data) {

    }

    function sendPlaybackRateChange(data) {

    }

    function sendError(data) {

    }

    function sendApiChange() {

    }

    function sendVideoCurrentTime(data) {

    }

    function sendVideoDuration(data) {

    }

    function sendStateChange(data) {
        
    }

    function sendMessage(data) {

    }

    function sendVideoId(data) {
        
    }

    return {
        sendYouTubeIframeAPIReady: sendYouTubeIframeAPIReady,
        sendReady: sendReady,
        sendPlayerStateChange: sendPlayerStateChange,
        sendPlaybackQualityChange: sendPlaybackQualityChange,
        sendPlaybackRateChange: sendPlaybackRateChange,
        sendError: sendError,
        sendApiChange: sendApiChange,
        sendVideoCurrentTime: sendVideoCurrentTime,
        sendVideoDuration: sendVideoDuration,
        sendStateChange: sendStateChange,
        sendMessage: sendMessage,
        sendVideoId: sendVideoId
    }
}