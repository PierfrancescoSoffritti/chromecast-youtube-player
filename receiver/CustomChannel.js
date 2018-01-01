import YouTubeMessage from "./YouTubeMessage.js";

function CustomChannel(namespace) {
    // const namespace = "urn:x-cast:com.pierfrancescosoffritti.chromecastyoutubesample.youtubeplayercommunication";    
    const context = cast.framework.CastReceiverContext.getInstance();

    function sendMessage(data) {
        if(!isObjectPoperlyFormatted(data)) {
            console.error("object not properly formatted.");
            console.error(data);

            return;
        }
        
        const sender = context.getSenders()[0];
        context.sendCustomMessage(namespace, sender.id, data);
    }

    function isObjectPoperlyFormatted(data) {
        const sampleMessage = new YouTubeMessage();

        for (const key in sampleMessage) 
            if (!data.hasOwnProperty(key))
                return false;

        return true;
    }

    return {
        sendMessage: sendMessage
    }
}

export default CustomChannel;