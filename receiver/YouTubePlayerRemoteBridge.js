import { YouTubeMessage } from "./YouTubeMessage.js";

function YouTubePlayerRemoteBridge(communicationConstants, communicationChannel) {

    function sendYouTubeIframeAPIReady() {
        communicationChannel.sendMessage(new YouTubeMessage(communicationConstants.IFRAME_API_READY))
    }

    function sendReady() {
        communicationChannel.sendMessage(new YouTubeMessage(communicationConstants.READY))
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

export default YouTubePlayerRemoteBridge;