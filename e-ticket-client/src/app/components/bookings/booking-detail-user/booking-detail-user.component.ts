import { DOCUMENT } from '@angular/common';
import { Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Notifications } from '@mobiscroll/angular';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';
import { BookingDetail } from 'src/app/model/booking.model';
import { BookingUserService } from 'src/app/services/booking-user.service';
import { Constants } from 'src/app/utils/constants';

@Component({
  selector: 'app-booking-detail-user',
  templateUrl: './booking-detail-user.component.html',
  styleUrls: ['./booking-detail-user.component.css']
})
export class BookingDetailUserComponent implements OnInit {
  @ViewChild('amount') amountElement!: ElementRef; 
  bookingId!: number;
  bookingDetail$: Observable<BookingDetail> = new Observable<BookingDetail>();
  BASE_API = Constants.HOST;
  placePaymentForm: any = new FormGroup({});

  constructor(private bookingService: BookingUserService, 
    private _route: ActivatedRoute, 
    private fb: FormBuilder,
    @Inject(DOCUMENT) private document: Document,
    private notify: Notifications,
    private _router: Router,
    private toastr: ToastrService) { }

  ngOnInit(): void {
    let paramValue = this._route.snapshot.paramMap.get('bookingId');
    if (paramValue !== null) {
     this.bookingId = parseInt(paramValue);
     this.bookingService.getBookingDetail(this.bookingId);
     this.bookingDetail$ = this.bookingService.bookingDetail$
    }
    this.placePaymentForm= this.fb.group({
      paymentType: ['PayPal', Validators.required]
    })
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

  isHiddenCancelBtn(bookingStatus: string, eventType: string, eventStatus: string): boolean {
    if (eventType === Constants.EVENT_TYPE_FREE 
        && (eventStatus === Constants.EVENT_STATUS_OPEN || eventStatus === Constants.EVENT_STATUS_SOLD)
        && (bookingStatus === Constants.BOOKING_STATUS_COMPLETED || bookingStatus === Constants.BOOKING_STATUS_PENDING)) {
      return false;
    }
    if (eventType === Constants.EVENT_TYPE_CHARGE 
        && (eventStatus === Constants.EVENT_STATUS_OPEN || eventStatus === Constants.EVENT_STATUS_SOLD)
        && (bookingStatus === Constants.BOOKING_STATUS_PENDING)) {
      return false; 
    }
    return true;
  }

  pendingBooking(bookingStatus: string, eventStatus: string, bookingTimeout: number): boolean {
    if (bookingStatus === Constants.BOOKING_STATUS_PENDING 
        && (eventStatus === Constants.EVENT_STATUS_OPEN || eventStatus === Constants.EVENT_STATUS_SOLD)) {
        return true;
    }
    if (bookingTimeout > 0 && bookingTimeout <= new Date().getTime()) {
      return true;
    }
    return false;
  }

  placePayment() {
    const bookingPaymentRequest = {
      bookingId: this.bookingId,
      paymentType: this.placePaymentForm.get("paymentType").value,
      amount: this.amountElement.nativeElement.textContent
    }
    this.bookingService.makePayment(bookingPaymentRequest).subscribe({
      next: (resp: any) => {
        this.document.location.href = resp.redirect_url;
      }
    });
  }

  openConfirmCancel(bookingId: number) {
    // Using callback function
    this.notify.confirm({
      title: 'Cancel Booking Confirm',
      message: 'Are your sure cancel this booking?',
      okText: 'OK',
      cancelText: 'Cancel'
    }).then((result) => {
      if (result == true) {
        this.handleCancelBooking(bookingId)
      } 
    })
    .catch((err) => {
      console.log(err);
    });
  }

  handleCancelBooking(bookingId: number) {
    this.bookingService.cancelBooking(bookingId).subscribe({
      next: (resp: any) => {
        if (resp.meta.code === 200) {
          this.toastr.success('', 'Completed canceling booking!', { timeOut: 1000, newestOnTop: true, tapToDismiss: true });
          this._router.navigate(['/booking']);
        } else {
          this.toastr.error('', 'Having error!', { timeOut: 1000, newestOnTop: true, tapToDismiss: true });
        }
      }
    })
  }

}
