import { YouTubeMessage, isMessagePoperlyFormatted } from "./YouTubeMessage.js";

function CustomChannel(namespace) {
    // const namespace = "urn:x-cast:com.pierfrancescosoffritti.chromecastyoutubesample.youtubeplayercommunication";    
    const context = cast.framework.CastReceiverContext.getInstance();

    function sendMessage(data) {
        if(!isMessagePoperlyFormatted(data)) {
            console.error("object not properly formatted.");
            console.error(data);

            return;
        }
        
        const sender = context.getSenders()[0];
        context.sendCustomMessage(namespace, sender.id, data);
    }

    return {
        sendMessage: sendMessage
    }
}

export default CustomChannel;