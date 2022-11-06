import { Component, Input, OnInit } from '@angular/core';
import { Ticket } from 'src/app/model/booking.model';

@Component({
  selector: 'app-ticket-detail',
  templateUrl: './ticket-detail.component.html',
  styleUrls: ['./ticket-detail.component.css']
})
export class TicketDetailComponent implements OnInit {
  @Input() ticket!: Ticket;
  @Input() bookingStatus!: string;

  constructor() { }

  ngOnInit(): void {
  }

}
