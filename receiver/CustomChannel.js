function CustomChannel() {
    // const namespace = "urn:x-cast:com.pierfrancescosoffritti.chromecastyoutubesample.youtubeplayercommunication";    
    const context = cast.framework.CastReceiverContext.getInstance();

    function sendMessage(data) {
        const sender = context.getSenders()[0];
        // if(sender)
            context.sendCustomMessage(namespace, sender.id, data);
        // else
        //     console.error("senderId undefined");
    }

    return {
        sendMessage: sendMessage
    }
}