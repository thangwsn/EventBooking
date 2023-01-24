import { DOCUMENT } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { MessageService, PrimeNGConfig } from 'primeng/api';
import { AccountInfo } from 'app/model/account.model';
import { BookingCreateRequest, ItemCreateRequest } from 'app/model/booking.model';
import { AccountService } from 'app/services/account.service';
import { BookingUserService } from 'app/services/booking-user.service';
import { Constants } from 'app/utils/constants';
import { NoWhitespaceValidator } from 'app/utils/no-whitespace.validator';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css'],
  providers: [MessageService]
})
export class CheckoutComponent implements OnInit {
  bookingPreCheckout: any;
  displayPayment: boolean = true;
  bookingCheckoutForm: any = new FormGroup({});
  userInfo$: Observable<AccountInfo> = new Observable<AccountInfo>();
  blockedDocument: boolean = false;

  constructor(
    private bookingService: BookingUserService,
    private accountService: AccountService,
    private fb: FormBuilder,
    private _router: Router,
    @Inject(DOCUMENT) private document: Document,
    private primengConfig: PrimeNGConfig,
    private messageService: MessageService) { }

  ngOnInit(): void {
    this.primengConfig.ripple = true;
    this.bookingPreCheckout = this.bookingService.bookingPreCheckout || { event: {}, listItem: [] };
    this.displayPayment = this.bookingPreCheckout.event.typeString == Constants.EVENT_TYPE_CHARGE
    this.accountService.getUserInfo();
    this.userInfo$ = this.accountService.accountInfo$
    this.bookingCheckoutForm = this.fb.group({
      fullName: [''],
      mobile: [''],
      paymentType: ['PayPal', Validators.required]
    })
    this.userInfo$.subscribe({
      next: (userInfo: AccountInfo) => {
        this.bookingCheckoutForm.patchValue({
          fullName: userInfo.fullName,
          mobile: userInfo.mobile,
        })
      }
    })

  }

  placeBooking() {
    this.blockedDocument = true;
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
            this.messageService.add({ severity: 'success', detail: 'Completed booking!!' });
            setTimeout(() => {
              this._router.navigate(['/booking/' + data.id]);
            }, 500) 
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
            this.messageService.add({ severity: 'warn', detail: data.message });
          }
        } else {
          this.messageService.add({ severity: 'error', detail:'Having error!' });
        }

      },
      error: () => {
        this.messageService.add({ severity: 'error', detail:'Having error!' });
      }
    });

  }

}
