import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PrimeNGConfig } from 'primeng/api';
import { EventService } from 'app/services/event.service';
import { NoWhitespaceValidator } from 'app/utils/no-whitespace.validator';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-add-ticket-dialog',
  templateUrl: './add-ticket-dialog.component.html',
  styleUrls: ['./add-ticket-dialog.component.css']
})
export class AddTicketDialogComponent implements OnInit {
  @Input() eventId!: number;
  @Input() eventType: string = '';
  addTicketCatalogForm: any = new FormGroup({});

  constructor(
    private fb: FormBuilder,
    private eventService: EventService,
    private primengConfig: PrimeNGConfig) { }

  ngOnInit(): void {
    this.primengConfig.ripple = true;
    this.addTicketCatalogForm = this.fb.group({
      title: ['', NoWhitespaceValidator()],
      slot: [0, Validators.compose([
        Validators.min(0),
        Validators.required
      ])],
      price: [0.0, Validators.compose([
        Validators.min(0.0),
        Validators.required
      ])],
      remark: ['']
    })
  }

  submit(): Observable<any> {
    const ticketCatalogCreateRequest = {
      title: this.addTicketCatalogForm.get('title').value,
      slot: this.addTicketCatalogForm.get('slot').value,
      price: (this.eventType === 'FREE') ? 0 : this.addTicketCatalogForm.get('price').value,
      remark: this.addTicketCatalogForm.get('remark').value,
    }
    return this.eventService.createTicketCatalog(ticketCatalogCreateRequest, this.eventId)
  }
}
