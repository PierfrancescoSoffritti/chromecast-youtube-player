const namespace = "urn:x-cast:com.pierfrancescosoffritti.chromecastyoutubesample.customchannel";

const context = cast.framework.CastReceiverContext.getInstance();

context.addCustomMessageListener(namespace, function(event) {
    console.log("CUSTOM MESSAGE RECEIVED!!")
    console.log(event)

    if(event.data.videoId)
        loadVideo(event.data.videoId, 0)

    // context.sendCustomMessage(namespace, event.senderId, "test message from receiver");
    // context.sendCustomMessage(namespace, undefined, "test message from receiver");
});

context.start();