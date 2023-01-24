import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { BookingDetail } from 'app/model/booking.model';
import { BookingService } from 'app/services/booking.service';
import { Constants } from 'app/utils/constants';
import { environment } from 'environments/environment';
import { EventGet } from 'app/model/event.model';

@Component({
  selector: 'app-booking-detail',
  templateUrl: './booking-detail.component.html',
  styleUrls: ['./booking-detail.component.css']
})
export class BookingDetailComponent implements OnInit {
  @Input("bookingDetail") bookingDetail$: Observable<BookingDetail> = new Observable<BookingDetail>();

  BASE_API = environment.host;

  constructor(private bookingService: BookingService, private _route: ActivatedRoute) { }

  ngOnInit(): void {
  }

  getBookingStatusClass(status: string): string {
    var bookingStatusClass = '';
    switch(status) {
      case Constants.BOOKING_STATUS_PENDING:
        bookingStatusClass = 'badge bg-primary'
        break;
      case Constants.BOOKING_STATUS_COMPLETED:
        bookingStatusClass = 'badge bg-success'
        break;
      case Constants.BOOKING_STATUS_CANCEL:
        bookingStatusClass = 'badge bg-danger'
        break;
      default:
        bookingStatusClass = 'badge bg-light text-dark'
    }
    return bookingStatusClass;
  }

  getPaymentTypeLogo(type: string): string {
    var imageLink = '';
    switch(type) {
      case Constants.PAYMENT_TYPE_PAYPAL:
        imageLink = 'assets/img/paypal.jpg'
    }
    return imageLink
  }

  getClassOfEventStatus(event: EventGet) {
    var status = event.statusString;
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
