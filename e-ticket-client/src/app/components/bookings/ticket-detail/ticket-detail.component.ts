import { Component, Input, OnInit } from '@angular/core';
import { Ticket } from 'app/model/booking.model';
import { Constants } from 'app/utils/constants';

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
