import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { BookingDetail, BookingGet, Payment } from "../model/booking.model";
import { ConvertToDate } from "../utils/time-convert";
import { TokenStorageService } from "./token-storage.service";
import { environment } from 'environments/environment';

const BASE_API = environment.host + "/api/emp/booking";

@Injectable({
    providedIn: 'root'
})
export class BookingService {
    private bookingList: BookingGet[] = [];
    private displayBookingListSubject: BehaviorSubject<BookingGet[]> = new BehaviorSubject<BookingGet[]>([]);
    bookingList$: Observable<BookingGet[]> = this.displayBookingListSubject.asObservable();

    private bookingDetail!: BookingDetail;
    private displayBookingDetailSubject: BehaviorSubject<BookingDetail> = new BehaviorSubject<BookingDetail>(new BookingDetail(0, '', 0, '', '', '', 0, '', 0, [], '', new Payment(0, '', '', 0), [], {}, 0));
    bookingDetail$: Observable<BookingDetail> = this.displayBookingDetailSubject.asObservable();

    constructor(
        private http: HttpClient,
        private tokenService: TokenStorageService
    ) {}

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
                this.updateBookingDetail();
            }
        })
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