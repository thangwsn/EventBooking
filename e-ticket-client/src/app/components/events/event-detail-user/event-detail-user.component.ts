import { Component, OnInit, QueryList, ViewChildren } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { MessageService, PrimeNGConfig } from 'primeng/api';

import { EventDetail } from 'app/model/event.model';
import { BookingUserService } from 'app/services/booking-user.service';
import { EventUserService } from 'app/services/event-user.service';
import { TokenStorageService } from 'app/services/token-storage.service';
import { Constants } from 'app/utils/constants';
import { TicketCatalogItemComponent } from '../ticket-catalog-item/ticket-catalog-item.component';
import { EventWebSocketAPI } from 'app/websocket/EventWebSocketAPI';
import { environment } from 'environments/environment';


@Component({
  selector: 'app-event-detail-user',
  templateUrl: './event-detail-user.component.html',
  styleUrls: ['./event-detail-user.component.css'],
  providers: [MessageService]
})
export class EventDetailUserComponent implements OnInit {
  @ViewChildren(TicketCatalogItemComponent) selectTicketList!: QueryList<TicketCatalogItemComponent>;

  BASE_API = environment.host;
  followClass: string = 'bi bi-heart text-danger';
  eventId!: number;
  event$: Observable<EventDetail> = new Observable<EventDetail>();
  eventType!: string;
  eventWebSocketAPI!: EventWebSocketAPI;

  responsiveOptions:any[] = [
    {
        breakpoint: '1024px',
        numVisible: 5
    },
    {
        breakpoint: '768px',
        numVisible: 3
    },
    {
        breakpoint: '560px',
        numVisible: 1
    }
];
  
  constructor(private _route: ActivatedRoute,
    private eventService: EventUserService,
    private bookingService: BookingUserService, 
    private _router: Router,
    private tokenService: TokenStorageService,
    private primengConfig: PrimeNGConfig,
    private messageService: MessageService) { }

  ngOnInit(): void {
    this.primengConfig.ripple = true;
    let paramValue = this._route.snapshot.paramMap.get('eventId');
    if (paramValue !== null) {
      this.eventId = parseInt(paramValue);
      this.eventService.fetchEventDetail(this.eventId);
      this.event$ = this.eventService.event$;
      this.event$.subscribe({
        next: (event) => {
          this.eventType = event.typeString
        }
      })
      this.eventWebSocketAPI = new EventWebSocketAPI(this.eventService, this.eventId, this)
      this.eventWebSocketAPI._connect();
    }
    
  }

  ngOnDestroy(): void {
    this.eventWebSocketAPI._disconnect();
  }

  booking() {
    let totalSelectTicket = 0;
    this.selectTicketList.forEach(item => {
      totalSelectTicket += item.quantity
    })
    if (totalSelectTicket === 0) {
      this.messageService.add({ severity: 'warn', detail: 'Select less than 1 ticket!' });
      return;
    }
    if (this.eventType === Constants.EVENT_TYPE_FREE && totalSelectTicket > 1) {
      this.messageService.add({ severity: 'warn', detail: 'With free event, only booking a ticket per user!' });
      return;
    }
    let listItem: any = this.selectTicketList.filter(item => item.quantity > 0).map(item => {
      return {
        ticketCatalog: item.ticketCatalog,
        quantity: item.quantity
      }
    })
    var event: any;
    this.event$.subscribe({
      next: (_event: EventDetail) => {
       event = _event;
      }
    })
    const bookingPreCheckout = {
      event: event,
      listItem: listItem
    }
    this.bookingService.preCheckout(bookingPreCheckout);
    this._router.navigate(['checkout']);
  }

  toggleFollowEvent() {
    if (this.tokenService.getToken() == null) {
      this._router.navigate(['login'])
      return;
    }
    this.eventService.toggleFollow();
  }

  getClassOfEventStatus() {
    var status = ''
    this.event$.subscribe({
      next: (event: EventDetail) => {
        status = event.statusString;
      }
    })
    var eventStatusClass = '';
    switch (status) {
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

  getClassOfEventType() {
    var type = '';
    this.event$.subscribe({
      next: (event: EventDetail) => {
        type = event.typeString;
      }
    })
    var eventTypeClass = '';
    switch (type) {
      case 'CHARGE':
        eventTypeClass = 'badge rounded-pill bg-primary'
        break;
      case 'FREE':
        eventTypeClass = 'badge rounded-pill bg-success'
        break;
      default:
        eventTypeClass = 'badge rounded-pill bg-danger'
    }
    return eventTypeClass;
  }

  getFollowClass(followed: boolean): string {
    if (followed) {
      return Constants.FOLLOW_CLASS
    }
    return Constants.UN_FOLLOW_CLASS
  }

  displayNotify(notify: any) {
    if (notify.type === 'FOLLOW') {
      this.messageService.add({ severity: 'success', key: 'notify', life: 1000, detail: notify.message });
    } else if (notify.type === 'BOOKING') {
      this.messageService.add({ severity: 'info', key: 'notify', life: 1000, detail: notify.message });
    }   
  }

}
