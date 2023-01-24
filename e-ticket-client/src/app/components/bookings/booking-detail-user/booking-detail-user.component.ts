import { DOCUMENT } from '@angular/common';
import { Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PrimeNGConfig, ConfirmationService, Message, MessageService, } from 'primeng/api';
import { Observable } from 'rxjs';
import { BookingDetail } from 'app/model/booking.model';
import { BookingUserService } from 'app/services/booking-user.service';
import { Constants } from 'app/utils/constants';
import { environment } from 'environments/environment';

@Component({
  selector: 'app-booking-detail-user',
  templateUrl: './booking-detail-user.component.html',
  styleUrls: ['./booking-detail-user.component.css'],
  providers: [ConfirmationService, MessageService]
})
export class BookingDetailUserComponent implements OnInit {
  @ViewChild('amount') amountElement!: ElementRef;
  bookingId!: number;
  bookingDetail$: Observable<BookingDetail> = new Observable<BookingDetail>();
  BASE_API = environment.host;
  placePaymentForm: any = new FormGroup({});
  blockedDocument: boolean = false;

  constructor(private bookingService: BookingUserService,
    private _route: ActivatedRoute,
    private fb: FormBuilder,
    @Inject(DOCUMENT) private document: Document,
    private confirmationService: ConfirmationService,
    private primengConfig: PrimeNGConfig,
    private messageService: MessageService,
    private _router: Router) { }

  ngOnInit(): void {
    this.primengConfig.ripple = true;
    let paramValue = this._route.snapshot.paramMap.get('bookingId');
    if (paramValue !== null) {
      this.bookingId = parseInt(paramValue);
      this.bookingService.getBookingDetail(this.bookingId);
      this.bookingDetail$ = this.bookingService.bookingDetail$
    }
    this.placePaymentForm = this.fb.group({
      paymentType: ['PayPal', Validators.required]
    })
  }

  getBookingStatusClass(status: string): string {
    var bookingStatusClass = '';
    switch (status) {
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
    switch (type) {
      case Constants.PAYMENT_TYPE_PAYPAL:
        imageLink = 'assets/img/paypal.jpg'
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
    return false;
  }

  placePayment() {
    this.blockedDocument = true;
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
    this.confirmationService.confirm({
      message: 'Are you sure that you want to cancel this booking?',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.handleCancelBooking(bookingId)
      },
      reject: () => {
        this.messageService.add({severity:'info', summary: 'Reject', detail: 'Reject!'});
      }
    });
  }

  handleCancelBooking(bookingId: number) {
    this.bookingService.cancelBooking(bookingId).subscribe({
      next: (resp: any) => {
        if (resp.meta.code === 200) {
          this.messageService.add({severity:'success', summary: 'Success', detail: 'Completed canceling booking!'});
          this.bookingService.updateStatus(Constants.BOOKING_STATUS_CANCEL)
          this.bookingDetail$ = this.bookingService.bookingDetail$
        } else {
          this.messageService.add({severity:'waring', summary: 'Error', detail: 'Having error!'});
        }
      }
    })
  }

  displayRemoveButton(bookingStatus: string): boolean {
    return bookingStatus == Constants.BOOKING_STATUS_CANCEL || bookingStatus == Constants.BOOKING_STATUS_PENDING;
  }

  openConfirmRemove(bookingId: number) {
    this.confirmationService.confirm({
      message: 'Are you sure that you want to remove this booking?',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.handleRemoveBooking(bookingId)
      },
      reject: () => {
        this.messageService.add({severity:'info', summary: 'Reject', detail: 'Reject!'});
      }
    });
  }

  handleRemoveBooking(bookingId: number) {
    this.bookingService.removeBooking(bookingId).subscribe({
      next: (resp: any) => {
        if (resp.meta.code == 200) {
          this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Remove successfully!' });
          setTimeout(() => {
            this._router.navigate(["/booking"]);
          }, 500)
        }
      },
      error: (err) => {
        this.messageService.add({ severity: 'warning', summary: 'Error', detail: 'Remove failure!' });
      }
    })
  }

}
