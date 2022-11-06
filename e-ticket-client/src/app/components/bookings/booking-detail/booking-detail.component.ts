import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { BookingDetail } from 'src/app/model/booking.model';
import { BookingService } from 'src/app/services/booking.service';
import { Constants } from 'src/app/utils/constants';

@Component({
  selector: 'app-booking-detail',
  templateUrl: './booking-detail.component.html',
  styleUrls: ['./booking-detail.component.css']
})
export class BookingDetailComponent implements OnInit {

  bookingId!: number;
  bookingDetail$: Observable<BookingDetail> = new Observable<BookingDetail>();

  constructor(private bookingService: BookingService, private _route: ActivatedRoute) { }

  ngOnInit(): void {
    let paramValue = this._route.snapshot.paramMap.get('bookingId');
    if (paramValue !== null) {
     this.bookingId = parseInt(paramValue);
     this.bookingService.getBookingDetail(this.bookingId);
     this.bookingDetail$ = this.bookingService.bookingDetail$
    }
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
        imageLink = 'https://i.imgur.com/cMk1MtK.jpg'
    }
    return imageLink
  }

}
