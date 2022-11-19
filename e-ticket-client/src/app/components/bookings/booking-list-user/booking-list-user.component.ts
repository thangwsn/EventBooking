import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PrimeNGConfig } from 'primeng/api';
import { Observable } from 'rxjs';
import { BookingGet } from 'app/model/booking.model';
import { BookingUserService } from 'app/services/booking-user.service';
import { Constants } from 'app/utils/constants';

@Component({
  selector: 'app-booking-list-user',
  templateUrl: './booking-list-user.component.html',
  styleUrls: ['./booking-list-user.component.css']
})
export class BookingListUserComponent implements OnInit {
  bookingList$: Observable<BookingGet[]> = new Observable<BookingGet[]>();
  blockedDocument: boolean = false;
  constructor(
    private bookingService: BookingUserService,
    private _router: Router,
    private _route: ActivatedRoute,
    private primengConfig: PrimeNGConfig
  ) { }

  ngOnInit(): void {
    this.primengConfig.ripple = true;
    var paymentId = '';
    var payerId = '';
    var cancel = '';
    var bookingId = '';
    this._route.queryParams.subscribe(params => {
      paymentId = params['paymentId'];
      payerId = params['PayerID'];
      cancel = params['cancel'];
      bookingId = params['bookingId']
    })
   
    if (paymentId !== undefined && payerId !== undefined) {
      this.blockedDocument = true;
      this.bookingService.completePayment(paymentId, payerId).subscribe({
        next: (resp: any) => {
          if (resp.meta.code === 200) {
            this._router.navigate(['/booking/' + resp.data])
          }
        },
        error: (err) => {
          console.log(err);
        }
      });
    }

    if (cancel === 'true' && bookingId !== undefined) {
      this.blockedDocument = true;
      this.bookingService.cancelPayment(bookingId).subscribe({
        next: (resp: any) => {
          if (resp.meta.code === 200) {
            this._router.navigate(['/booking/' + resp.data])
          }
        },
        error: (err) => {
          console.log(err);
        }
      });
    }

    const request = {
      pageNo: 1,
      pageSize: 10,
      searchKey: '',
      sortDirection: 'desc',
      sortField: 'createTime',
      status: '',
    }
    this.bookingService.getBookingList(request);
    this.bookingList$ = this.bookingService.bookingList$;
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

}
