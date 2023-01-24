import { Component, Input, OnInit } from '@angular/core';
import { EventGet } from 'app/model/event.model';
import { Constants } from 'app/utils/constants';
import { environment } from 'environments/environment';

@Component({
  selector: 'app-horizontal-event-item',
  templateUrl: './horizontal-event-item.component.html',
  styleUrls: ['./horizontal-event-item.component.css']
})
export class HorizontalEventItemComponent implements OnInit {
  @Input() event!: EventGet;
  BASE_API: string = environment.host;
  constructor() { }

  ngOnInit(): void {
  }

  getClassOfEventStatus() {
    var status = this.event.statusString;
    var eventStatusClass = '';
    switch(status) {
      case Constants.EVENT_STATUS_CREATED:
        eventStatusClass = 'badge bg-primary'
        break;
      case Constants.EVENT_STATUS_OPEN:
        eventStatusClass = 'badge bg-success'
        break;
      case Constants.EVENT_STATUS_SOLD:
        eventStatusClass = 'badge bg-danger'
        break;
      case Constants.EVENT_STATUS_LIVE:
        eventStatusClass = 'badge bg-info text-dark'
        break;
      case Constants.EVENT_STATUS_FINISH:
        eventStatusClass = 'badge bg-secondary'
        break;
      default:
        eventStatusClass = 'badge bg-light text-dark'
    }
    return eventStatusClass;
  }
}
