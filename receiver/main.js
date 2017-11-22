const namespace = "urn:x-cast:com.pierfrancescosoffritti.chromecastyoutubesample.customchannel";

const context = cast.framework.CastReceiverContext.getInstance();

function initCommunications(event) {
    console.log("INIT MESSAGE RECEIVED!!");

    const script = document.createElement('script');
    script.src = "https://www.youtube.com/iframe_api";
    document.getElementsByTagName('head')[0].appendChild(script);

    // if(event.data.videoId)
    //     loadVideo(event.data.videoId, 0)

    context.sendCustomMessage(namespace, event.senderId, "test message from receiver");
    context.removeCustomMessageListener(namespace, initCommunications);
}

context.addCustomMessageListener(namespace, initCommunications);

context.start();