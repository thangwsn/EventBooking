import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { environment } from 'environments/environment';
import { EventUserService } from 'app/services/event-user.service';
import { EventDetailUserComponent } from 'app/components/events/event-detail-user/event-detail-user.component';

export class EventWebSocketAPI {
    private endPoint = environment.ws;
    private stompClient: any;
    constructor(private eventService: EventUserService, private eventId: number, private eventDetailUser : EventDetailUserComponent) { }

    _connect() {
        console.log("Initialize WebSocket Connection");
        let ws = new SockJS(this.endPoint);
        this.stompClient = Stomp.over(ws);
        const _this = this;
        let topic = `/event/${this.eventId}/event-update`;
        let topicNotify = `/event/${this.eventId}/notify`;
        _this.stompClient.connect({}, (frame: any) => {
            _this.stompClient.subscribe(topic, (sdkEvent: any) => {
                _this.onMessageReceivedUpdate(sdkEvent);
            });
            _this.stompClient.subscribe(topicNotify, (sdkEvent: any) => {
                _this.onMessageReceivedNotify(sdkEvent);
            });

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

    onMessageReceivedUpdate(message: any) {
        const event = JSON.parse(message.body);
        console.log(event);
        this.eventService.handleRealtimeEvent(event);
    }

    onMessageReceivedNotify(message: any) {
        const notify = JSON.parse(message.body);
        this.eventDetailUser.displayNotify(notify)
    }
}