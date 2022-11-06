import { DOCUMENT } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';
import { AccountInfo } from 'src/app/model/account.model';
import { BookingCreateRequest, ItemCreateRequest } from 'src/app/model/booking.model';
import { AccountService } from 'src/app/services/account.service';
import { BookingUserService } from 'src/app/services/booking-user.service';
import { Constants } from 'src/app/utils/constants';
import { NoWhitespaceValidator } from 'src/app/utils/no-whitespace.validator';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {
  bookingPreCheckout: any;
  displayPayment: boolean = true;
  bookingCheckoutForm: any = new FormGroup({});
  userInfo$: Observable<AccountInfo> = new Observable<AccountInfo>();


  constructor(
    private bookingService: BookingUserService,
    private accountService: AccountService,
    private fb: FormBuilder,
    private toastr: ToastrService,
    private _router: Router,
    @Inject(DOCUMENT) private document: Document,
    private _route: ActivatedRoute) { }

  ngOnInit(): void {
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
      this.bookingService.completePayment(paymentId, payerId).subscribe({
        next: (resp: any) => {
          if (resp.meta.code === 200) {
            this._router.navigate(['/booking'])
          }
        },
        error: (err) => {
          console.log(err);
        }
      });
    }

    if (cancel === 'true' && bookingId !== undefined) {
      this.bookingService.cancelPayment(bookingId).subscribe({
        next: (resp: any) => {
          if (resp.meta.code === 200) {
            this._router.navigate(['/booking'])
          }
        },
        error: (err) => {
          console.log(err);
        }
      });
    }

    this.bookingPreCheckout = this.bookingService.bookingPreCheckout || { event: {}, listItem: [] };
    this.displayPayment = this.bookingPreCheckout.event.typeString == Constants.EVENT_TYPE_CHARGE
    this.accountService.getUserInfo();
    this.userInfo$ = this.accountService.accountInfo$
    this.bookingCheckoutForm = this.fb.group({
      fullName: [''],
      mobile: [''],
      paymentType: ['PayPal', Validators.required]
    })

  }

  placeBooking() {
    var listItem: ItemCreateRequest[] = this.bookingPreCheckout.listItem.map((item: any) => new ItemCreateRequest(item.ticketCatalog.id, item.quantity))
    const request: BookingCreateRequest = new BookingCreateRequest(
      this.bookingPreCheckout.event.id,
      this.bookingCheckoutForm.get("fullName").value,
      this.bookingCheckoutForm.get("mobile").value,
      listItem,
      this.bookingCheckoutForm.get("paymentType").value
    );
    this.bookingService.placeBooking(request).subscribe({
      next: (resp: any) => {
        if (resp.meta.code === 200) {
          const data = resp.data;
          if (data.status === Constants.BOOKING_STATUS_COMPLETED) {
            this.toastr.success('', 'Completed booking!', { timeOut: 1000, newestOnTop: true, tapToDismiss: true });
            this._router.navigate(['/booking']);
          } else if (data.status === Constants.BOOKING_STATUS_PENDING) {
            // handle payment
            const bookingPaymentRequest = {
              bookingId: data.id,
              paymentType: data.paymentType,
              amount: data.amount
            }
            this.bookingService.makePayment(bookingPaymentRequest).subscribe({
              next: (resp: any) => {
                this.document.location.href = resp.redirect_url;
              }
            });
          } else if (data.status === Constants.BOOKING_STATUS_REJECT) {
            this.toastr.warning('', data.message, { timeOut: 1000, newestOnTop: true, tapToDismiss: true });
          }
        } else {
          this.toastr.error('', 'Having error!', { timeOut: 1000, newestOnTop: true, tapToDismiss: true });
        }

      },
      error: () => {
        this.toastr.error('', 'Having error!', { timeOut: 1000, newestOnTop: true, tapToDismiss: true });
      }
    });

  }

}
