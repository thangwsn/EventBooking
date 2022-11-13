import { Component, Input, OnInit } from '@angular/core';
import { Ticket } from 'src/app/model/booking.model';
import { Constants } from 'src/app/utils/constants';

@Component({
  selector: 'app-ticket-detail',
  templateUrl: './ticket-detail.component.html',
  styleUrls: ['./ticket-detail.component.css']
})
export class TicketDetailComponent implements OnInit {
  @Input() ticket!: Ticket;
  @Input() bookingStatus!: string;

  BASE_API = Constants.HOST
  constructor() { }

  ngOnInit(): void {
  }

}
