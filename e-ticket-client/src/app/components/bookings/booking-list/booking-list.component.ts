import { Component, OnInit, ViewChild } from '@angular/core';
import { Observable } from 'rxjs';
import { BookingDetail, BookingGet } from 'app/model/booking.model';
import { BookingService } from 'app/services/booking.service';
import { Constants } from 'app/utils/constants';
import { Table } from 'primeng/table';

@Component({
  selector: 'app-booking-list',
  templateUrl: './booking-list.component.html',
  styleUrls: ['./booking-list.component.css']
})
export class BookingListComponent implements OnInit {
  @ViewChild('dt') dt!: Table;
  bookingList$: Observable<BookingGet[]> = new Observable<BookingGet[]>();
  bookingDetail$: Observable<BookingDetail> = new Observable<BookingDetail>();
  bookingDetailDialog: boolean = false;

  constructor(
    private bookingService: BookingService
  ) { }

  ngOnInit(): void {
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

  applyFilterGlobal($event: any, stringVal: any) {
    this.dt.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
  }

  detailBooking(id: number) {
    this.bookingService.getBookingDetail(id);
    this.bookingDetail$ = this.bookingService.bookingDetail$;
    this.bookingDetailDialog = true;
  }

  closeDetailDialog() {
    this.bookingDetailDialog = false;
  }
}
