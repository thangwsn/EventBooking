import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable, VirtualTimeScheduler } from "rxjs";
import { BookingCreateRequest, BookingDetail, BookingGet, Payment } from "../model/booking.model";
import { EventGet } from "../model/event.model";
import { Constants } from "../utils/constants";
import { ConvertToDate } from "../utils/time-convert";
import { TokenStorageService } from "./token-storage.service";

const BASE_API = Constants.HOST + "/api/booking";

@Injectable({
    providedIn: 'root'
})
export class BookingUserService {
    private httpOptions: any;
    bookingPreCheckout: any;

    private bookingList: BookingGet[] = [];
    private displayBookingListSubject: BehaviorSubject<BookingGet[]> = new BehaviorSubject<BookingGet[]>([]);
    bookingList$: Observable<BookingGet[]> = this.displayBookingListSubject.asObservable();

    private bookingDetail!: BookingDetail;
    private displayBookingDetailSubject: BehaviorSubject<BookingDetail> = new BehaviorSubject<BookingDetail>(new BookingDetail(0, '', 0, '', '', '', 0, '', 0, [], '', new Payment(0, '', '', 0), [], {}, 0));
    bookingDetail$: Observable<BookingDetail> = this.displayBookingDetailSubject.asObservable();

    constructor(
        private http: HttpClient,
        private tokenService: TokenStorageService
    ) { }

    preCheckout(bookingPreCheckout: any) {
        this.bookingPreCheckout = bookingPreCheckout;
    }

    placeBooking(request: BookingCreateRequest): Observable<any> {
        this.httpOptions = this.getHeader();
        return this.http.post(`${BASE_API}/checkout`, request, this.httpOptions)
    }

    makePayment(bookingPaymentRequest: any): Observable<any> {
        this.httpOptions = this.getHeader();
        return this.http.post(`${BASE_API}/make-payment`, bookingPaymentRequest, this.httpOptions)
    }

    completePayment(paymentId: string, payerId: string): Observable<any> {
        this.httpOptions = this.getHeader();
        return this.http.post(`${BASE_API}/complete-payment?paymentId=${paymentId}&payerId=${payerId}`, this.httpOptions);
    }

    cancelPayment(bookingId: string): Observable<any> {
        this.httpOptions = this.getHeader();
        return this.http.post(`${BASE_API}/cancel-booking?bookingId=${bookingId}`, this.httpOptions);
    }

    getBookingList(request: any) {
        this.http.post(`${BASE_API}/get-list-booking`, request, this.getHeader()).subscribe({
            next: (resp: any) => {
                this.bookingList = resp.data.listBooking;
                this.bookingList.forEach(booking => {
                    booking.createTime = ConvertToDate(booking.createTime)
                })
                this.updateBookingList();
            }
        })
    }

    getBookingDetail(bookingId: number) {
        this.http.get(`${BASE_API}/${bookingId}`, this.getHeader()).subscribe({
            next: (resp: any) => {
                this.bookingDetail = resp.data
                this.bookingDetail.createTime = ConvertToDate(this.bookingDetail.createTime)
                this.bookingDetail.payment.createTime = ConvertToDate(this.bookingDetail.payment.createTime)
                this.updateBookingDetail()
            }
        })
    }

    cancelBooking(bookingId: number): Observable<any> {
        return this.http.get(`${BASE_API}/cancel-booking?bookingId=${bookingId}`, this.getHeader());
    }

    private updateBookingList() {
        this.displayBookingListSubject.next(this.bookingList);
    }

    private updateBookingDetail() {
        this.displayBookingDetailSubject.next(this.bookingDetail);
    }

    private getHeader() {
        return {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*',
                'Authorization': 'Bearer ' + this.tokenService.getToken()
            })
        }
    }



}