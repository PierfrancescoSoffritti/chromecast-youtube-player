import SenderMessagesDispatcher from "./SenderMessagesDispatcher.js";
import YouTubePlayer from "./YouTubePlayer.js";
import CustomChannel from "./CustomChannel.js";

const namespace = "urn:x-cast:com.pierfrancescosoffritti.chromecastyoutubesample.youtubeplayercommunication";
let communicationConstants = {};

const context = cast.framework.CastReceiverContext.getInstance();

const communicationChannel = new CustomChannel(namespace);
const youTubePlayer = new YouTubePlayer(communicationConstants, communicationChannel);
const senderMessagesDispatcher = new SenderMessagesDispatcher(communicationConstants, youTubePlayer.getActions());

context.addCustomMessageListener(namespace, initCommunications);
context.start();

function initCommunications(event) {
    console.log("INIT MESSAGE RECEIVED!!");

    initializeCommunicationConstants(event);
    loadYouTubeIFrameAPIs();
    setupSenderMessagesDispatcher();
}

function initializeCommunicationConstants(event) {
    for (let key in event.data)
        communicationConstants[key] = event.data[key];
}

function loadYouTubeIFrameAPIs() {
    const script = document.createElement('script');
    script.src = "https://www.youtube.com/iframe_api";
    document.getElementsByTagName('head')[0].appendChild(script);
}

function setupSenderMessagesDispatcher() {
    context.removeCustomMessageListener(namespace, initCommunications);
    context.addCustomMessageListener(namespace, senderMessagesDispatcher.onMessage);
}

// called automatically by the IFrame APIs
function onYouTubeIframeAPIReady() {
    youTubePlayer.initialize();
}

window.main_onYouTubeIframeAPIReady = onYouTubeIframeAPIReady