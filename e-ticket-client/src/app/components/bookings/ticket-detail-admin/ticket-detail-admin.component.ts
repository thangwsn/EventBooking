import { Component, Input, OnInit } from '@angular/core';
import { Ticket } from 'app/model/booking.model';

@Component({
  selector: 'app-ticket-detail-admin',
  templateUrl: './ticket-detail-admin.component.html',
  styleUrls: ['./ticket-detail-admin.component.css']
})
export class TicketDetailAdminComponent implements OnInit {
  @Input() ticket!: Ticket;

  constructor() { }

  ngOnInit(): void {
  }

}
