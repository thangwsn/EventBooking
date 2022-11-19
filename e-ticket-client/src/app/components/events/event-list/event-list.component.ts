import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { PrimeNGConfig, MessageService, } from 'primeng/api';

import { EventGet } from 'app/model/event.model';
import { EventService } from 'app/services/event.service';
import { Constants } from 'app/utils/constants';

@Component({
  selector: 'app-event-list',
  templateUrl: './event-list.component.html',
  styleUrls: ['./event-list.component.css'],
  providers: [MessageService]
})
export class EventListComponent implements OnInit {
  eventList$: Observable<EventGet[]> = new Observable<EventGet[]>();

  constructor(private eventService: EventService, private primengConfig: PrimeNGConfig,
    private messageService: MessageService) { }

  ngOnInit(): void {
    this.primengConfig.ripple = true;
    this.eventService.fetchEventListForAdmin();
    this.eventList$ = this.eventService.eventList$;
  }

  getClassOfEventType(type: string) {
    var eventTypeClass = '';
   switch(type) {
    case 'CHARGE':
      eventTypeClass = 'badge bg-primary'
      break;
    case 'FREE':
      eventTypeClass = 'badge bg-success'
      break;
    default:
      eventTypeClass = 'badge bg-danger'
   }
    return eventTypeClass;
  }

  getClassOfEventStatus(status: string) {
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
