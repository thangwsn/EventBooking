import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { environment } from 'environments/environment';
import { EventUserService } from 'app/services/event-user.service';

export class EventWebSocketAPI {
    private endPoint = environment.ws;
    private stompClient: any;
    constructor(private eventService: EventUserService, private eventId: number) { }

    _connect() {
        console.log("Initialize WebSocket Connection");
        let ws = new SockJS(this.endPoint);
        this.stompClient = Stomp.over(ws);
        const _this = this;
        let topic = `/event/${this.eventId}/event-update`;
        _this.stompClient.connect({}, (frame: any) => {
            _this.stompClient.subscribe(topic, (sdkEvent: any) => {
                _this.onMessageReceived(sdkEvent);
            });
            // _this.stompClient.reconnect_delay = 2000;
        }, this.errorCallBack);
    };

    _disconnect() {
        if (this.stompClient !== null) {
            this.stompClient.disconnect();
        }
        console.log("Disconnected");
    }

    // on error, schedule a reconnection attempt
    errorCallBack(error: any) {
        console.log("errorCallBack -> " + error)
        setTimeout(() => {
            this._connect();
        }, 5000);
    }

    onMessageReceived(message: any) {
        const event = JSON.parse(message.body);
        console.log(event);
        this.eventService.handleRealtimeEvent(event); 
    }
}