import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { EventService } from 'src/app/services/event.service';
import { NoWhitespaceValidator } from 'src/app/utils/no-whitespace.validator';

@Component({
  selector: 'app-add-ticket-dialog',
  templateUrl: './add-ticket-dialog.component.html',
  styleUrls: ['./add-ticket-dialog.component.css']
})
export class AddTicketDialogComponent implements OnInit {
  @Input() eventId!: number;
  @Input() eventType: string = '';
  addTicketCatalogForm: any;

  constructor(private fb: FormBuilder, private eventService: EventService, private toastr: ToastrService) { }

  ngOnInit(): void {
    console.log("Event ID", this.eventId);
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

  submit() {
    const ticketCatalogCreateRequest = {
      title: this.addTicketCatalogForm.get('title').value,
      slot: this.addTicketCatalogForm.get('slot').value,
      price: (this.eventType === 'FREE') ? 0 : this.addTicketCatalogForm.get('price').value,
      remark: this.addTicketCatalogForm.get('remark').value, 
    }
    this.eventService.createTicketCatalog(ticketCatalogCreateRequest, this.eventId).subscribe({
      next: (statusCode) => {
        if (statusCode == 200) {
          //reset form
          this.addTicketCatalogForm.setValue({
            title: '',
            slot: 0,
            price: 0,
            remark: ''
          })
          this.toastr.success('', 'Create ticket catalog successfully!', {timeOut: 1000, newestOnTop: true, tapToDismiss: true});

        } else {
          this.toastr.error('', 'Create ticket catalog failure!', {timeOut: 1000, newestOnTop: true, tapToDismiss: true});
        }
      }
    })    
  }
}
