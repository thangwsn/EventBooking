import { Component, Inject, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PrimeNGConfig } from 'primeng/api';
import { EventService } from 'app/services/event.service';
import { NoWhitespaceValidator } from 'app/utils/no-whitespace.validator';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-edit-ticket-dialog',
  templateUrl: './edit-ticket-dialog.component.html',
  styleUrls: ['./edit-ticket-dialog.component.css']
})
export class EditTicketDialogComponent implements OnInit {
  @Input() eventId!: number;
  @Input() eventType: string = '';
  @Input() ticketCatalog: any = {};

  editTicketCatalogForm: any = new FormGroup({});

  constructor(
    private fb: FormBuilder,
    private eventService: EventService,
    private primengConfig: PrimeNGConfig,
    ) { }

  ngOnInit(): void {
    this.primengConfig.ripple = true;
    this.editTicketCatalogForm = this.fb.group({
      title: ['', NoWhitespaceValidator()],
      slot: [this.ticketCatalog.soldSlot, Validators.compose([
        Validators.min(this.ticketCatalog.soldSlot),
        Validators.required
      ])],
      price: [0.0, Validators.compose([
        Validators.min(0.0),
        Validators.required
      ])],
      remark: ['']
    })
    this.editTicketCatalogForm.setValue({
      title: this.ticketCatalog.title,
      slot: this.ticketCatalog.slot,
      price: this.ticketCatalog.price,
      remark: this.ticketCatalog.remark
    })
  }

  submit(): Observable<any> {
    const ticketCatalogEditRequest = {
      id: this.ticketCatalog.id,
      title: this.editTicketCatalogForm.get('title').value,
      slot: this.editTicketCatalogForm.get('slot').value,
      price: (this.eventType === 'FREE') ? 0 : this.editTicketCatalogForm.get('price').value,
      remark: this.editTicketCatalogForm.get('remark').value,
    }
    return this.eventService.editTicketCatalog(ticketCatalogEditRequest, this.eventId)
  }

}
