import { Component, Input, OnInit } from '@angular/core';
import { TicketCatalogGetResponse } from 'src/app/model/event.model';

@Component({
  selector: 'app-ticket-catalog-item',
  templateUrl: './ticket-catalog-item.component.html',
  styleUrls: ['./ticket-catalog-item.component.css']
})
export class TicketCatalogItemComponent implements OnInit {
  @Input() ticketCatalog!: TicketCatalogGetResponse;
  quantity: number = 0;
  constructor() { }

  ngOnInit(): void {
  }

  add() {
    (this.quantity < (this.ticketCatalog.slot - this.ticketCatalog.soldSlot)) ? this.quantity++ : this.quantity;
  }

  sub() {
    (this.quantity > 0) ? this.quantity-- : this.quantity;
  }

  isHiddenAdd() {
    return this.quantity >=  (this.ticketCatalog.slot - this.ticketCatalog.soldSlot) || (this.ticketCatalog.slot - this.ticketCatalog.soldSlot) <= 0;
  }

  isHiddenSub() {
    return this.quantity <= 0;
  }

}
