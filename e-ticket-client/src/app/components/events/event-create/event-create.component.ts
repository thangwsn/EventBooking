import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EventService } from 'src/app/services/event.service';

import { Notifications, setOptions } from '@mobiscroll/angular';
import { EventCreateRequest, LocationDTO } from 'src/app/model/event.model';
import { Router } from '@angular/router';
import { TimeConvert } from 'src/app/utils/time-convert';
import { OrganizerService } from 'src/app/services/organizer.service';
import { NoWhitespaceValidator } from 'src/app/utils/no-whitespace.validator';

setOptions({
  theme: 'windows',
  themeVariant: 'light'
});

@Component({
  selector: 'app-event-create',
  templateUrl: './event-create.component.html',
  styleUrls: ['./event-create.component.css']
})
export class EventCreateComponent implements OnInit {
  createEventForm: any = new FormGroup({});
  listOrganizer: any;
  listEventType: any;

  selectedFiles!: FileList;

  constructor(private eventService: EventService, private organizerService: OrganizerService, private fb: FormBuilder, private _router: Router, private notify: Notifications) { }

  ngOnInit(): void {
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
      startTime_date: [Validators.required],
      duration: [Validators.required],
      startTime_time: [Validators.required],
      launchTime_date: [Validators.required],
      launchTime_time: [Validators.required],
      closeTime_date: [Validators.required],
      closeTime_time: [Validators.required],
      videoLink: ['']
    })
  }

  selectFiles(event: any) {
    this.selectedFiles = event.target.files;
  }

  onSubmit() {
    console.log(this.createEventForm);
    let startTime = TimeConvert(this.createEventForm.get('startTime_date').value, this.createEventForm.get('startTime_time').value);
    let launchTime = TimeConvert(this.createEventForm.get('launchTime_date').value, this.createEventForm.get('launchTime_time').value)
    let closeTime = TimeConvert(this.createEventForm.get('closeTime_date').value, this.createEventForm.get('closeTime_time').value)
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
      startTime,
      launchTime,
      closeTime,
      this.createEventForm.get('duration').value,
      this.createEventForm.get('totalSlot').value,
      this.createEventForm.get('organizerId').value,
      locationDto,
      this.createEventForm.get('videoLink').value
    );
    this.eventService.createEvent(eventCreateRequest, this.selectedFiles).subscribe({
      next: (response: any) => {
        if (response.meta.code == 200) {
          this.notify.toast({
            message: 'Create event successfully!',
            color: 'success',
            duration: 3000,
            display: 'bottom'
          });
          this._router.navigate(["admin/events"]);
        } else {
          this.notify.toast({
            message: 'Create event failure!',
            color: 'warning',
            duration: 3000,
            display: 'bottom'
          });
        }
      },
      error: (resp) => {
        this.notify.toast({
          message: 'Create event failure!',
          color: 'warning',
          duration: 3000,
          display: 'bottom'
        });
      }
    })
  }
}
