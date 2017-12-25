const namespace = "urn:x-cast:com.pierfrancescosoffritti.chromecastyoutubesample.youtubeplayercommunication";
let CommunicationConstants;

const context = cast.framework.CastReceiverContext.getInstance();

const senderMessagesDispatcher = new SenderMessagesDispatcher(context);

context.addCustomMessageListener(namespace, initCommunications);
context.start();

function initCommunications(event) {
    console.log("INIT MESSAGE RECEIVED!!");

    CommunicationConstants = event.data;

    // load youtube apis
    const script = document.createElement('script');
    script.src = "https://www.youtube.com/iframe_api";
    document.getElementsByTagName('head')[0].appendChild(script);

    context.removeCustomMessageListener(namespace, initCommunications);

    context.addCustomMessageListener(namespace, senderMessagesDispatcher.onMessage);
}