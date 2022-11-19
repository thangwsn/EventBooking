import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PrimeNGConfig, MessageService, } from 'primeng/api';

import { EventService } from 'app/services/event.service';
import { EventCreateRequest, LocationDTO } from 'app/model/event.model';
import { Router } from '@angular/router';
import { OrganizerService } from 'app/services/organizer.service';
import { NoWhitespaceValidator } from 'app/utils/no-whitespace.validator';


@Component({
  selector: 'app-event-create',
  templateUrl: './event-create.component.html',
  styleUrls: ['./event-create.component.css'],
  providers: [MessageService]
})
export class EventCreateComponent implements OnInit {
  createEventForm: any = new FormGroup({});
  listOrganizer: any;
  listEventType: any;
  selectedFileArr: any[] = [];
  blockedDocument: boolean = false;

  constructor(private eventService: EventService,
    private organizerService: OrganizerService, 
    private fb: FormBuilder, 
    private _router: Router,
    private primengConfig: PrimeNGConfig,
    private messageService: MessageService) { }

  ngOnInit(): void {
    this.primengConfig.ripple = true;
    this.listEventType = this.eventService.getListEventType().subscribe({
      next: (resp) => {
        this.listEventType = resp;
      }
    })

    this.organizerService.getOrganizersForCreateEvent().subscribe({
      next: (resp: any) => {
        if (resp.meta.code == 200) {
          this.listOrganizer = resp.data.listOrganizer || [];
        }
      }
    })

    this.createEventForm = this.fb.group({
      title: ['', NoWhitespaceValidator()],
      typeString: [Validators.required],
      summary: [],
      description: [],
      listTag: [],
      street: ['', Validators.required],
      ward: [''],
      district: [''],
      city: [''],
      organizerId: [Validators.required],
      totalSlot: [0],
      startTime: [Validators.required],
      duration: [0, Validators.required],
      launchTime: [Validators.required],
      closeTime: [Validators.required],
      videoLink: ['']
    })
  }

  selectFiles(event: any) {
    for(let file of event.files) {
      this.selectedFileArr.push(file);
    }
  }

  onSubmit() {
    this.blockedDocument = true;
    let locationDto = new LocationDTO(
      this.createEventForm.get('street').value,
      this.createEventForm.get('ward').value,
      this.createEventForm.get('district').value,
      this.createEventForm.get('city').value
    );
    let eventCreateRequest = new EventCreateRequest(
      this.createEventForm.get('title').value,
      this.createEventForm.get('typeString').value,
      this.createEventForm.get('summary').value,
      this.createEventForm.get('description').value,
      this.createEventForm.get('listTag').value,
      this.createEventForm.get('startTime').value.getTime(),
      this.createEventForm.get('launchTime').value.getTime(),
      this.createEventForm.get('closeTime').value.getTime(),
      this.createEventForm.get('duration').value,
      this.createEventForm.get('totalSlot').value,
      this.createEventForm.get('organizerId').value,
      locationDto,
      this.createEventForm.get('videoLink').value
    );
    this.eventService.createEvent(eventCreateRequest, this.selectedFileArr).subscribe({
      next: (response: any) => {
        if (response.meta.code == 200) {
          this.messageService.add({severity:'success', summary: 'Success', detail: 'Create event successfully!'});
          setTimeout(() => {
            this._router.navigate(["admin/events"]);
          }, 500);
        } else {
          this.messageService.add({severity:'error', summary: 'Error', detail: 'Create event failure!'});
          this.blockedDocument = false;
        }
      },
      error: (resp) => {
        this.messageService.add({severity:'error', summary: 'Error', detail: 'Create event failure!'});
        this.blockedDocument = false;
      }
    })
  }
}
