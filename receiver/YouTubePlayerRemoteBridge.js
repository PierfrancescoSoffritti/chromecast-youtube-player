function YouTubePlayerRemoteBridge() {

    const channel = new CustomChannel();

    function sendYouTubeIframeAPIReady() {
        channel.sendMessage(Constants.IframeAPIReady)
    }

    function sendReady() {
        channel.sendMessage(Constants.Ready)
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