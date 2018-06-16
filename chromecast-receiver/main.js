import SenderMessagesDispatcher from "./SenderMessagesDispatcher.js";
import YouTubePlayer from "./YouTubePlayer.js";
import CustomChannel from "./CustomChannel.js";

const namespace = "urn:x-cast:com.pierfrancescosoffritti.chromecastyoutubesample.youtubeplayercommunication";
let communicationConstants = {};

const context = cast.framework.CastReceiverContext.getInstance();

const communicationChannel = new CustomChannel(namespace);
const youTubePlayer = new YouTubePlayer(communicationConstants, communicationChannel);
const senderMessagesDispatcher = new SenderMessagesDispatcher(communicationConstants, { ...youTubePlayer.getActions(), onInitMessageReceived });

let receiverReady = false;

context.addCustomMessageListener(namespace, senderMessagesDispatcher.onMessage);
context.start();

// context.addEventListener(cast.framework.system.EventType.SENDER_CONNECTED, () => {
// })

function onInitMessageReceived(parsedCommunicationConstants) {
    if(!receiverReady) {
        initCommunicationConstants(parsedCommunicationConstants);
        loadYouTubeIFrameAPIs();
    } else
        youTubePlayer.restoreCommunication();
}

function initCommunicationConstants(parsedCommunicationConstants) {
    for (let key in parsedCommunicationConstants)
        communicationConstants[key] = parsedCommunicationConstants[key];
}

function loadYouTubeIFrameAPIs() {
    const script = document.createElement('script');
    script.src = "https://www.youtube.com/iframe_api";
    document.getElementsByTagName('head')[0].appendChild(script);
}

// called automatically by the IFrame APIs
function onYouTubeIframeAPIReady() {
    receiverReady = true;
    youTubePlayer.initialize();
}

window.main_onYouTubeIframeAPIReady = onYouTubeIframeAPIReady