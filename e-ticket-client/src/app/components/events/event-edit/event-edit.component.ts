import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EventDetail, EventEditRequest, LocationDTO } from 'app/model/event.model';
import { EventService } from 'app/services/event.service';
import { OrganizerService } from 'app/services/organizer.service';
import { NoWhitespaceValidator } from 'app/utils/no-whitespace.validator';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-event-edit',
  templateUrl: './event-edit.component.html',
  styleUrls: ['./event-edit.component.css']
})
export class EventEditComponent implements OnInit {
  editEventForm: any = new FormGroup({})
  listOrganizer: any;
  @Input() eventDetail!: EventDetail;

  constructor(private eventService: EventService,
    private organizerService: OrganizerService, 
    private fb: FormBuilder) { }

  ngOnInit(): void {
    this.organizerService.getOrganizersForCreateEvent().subscribe({
      next: (resp: any) => {
        if (resp.meta.code == 200) {
          this.listOrganizer = resp.data.listOrganizer || [];
        }
      }
    })
    this.editEventForm = this.fb.group({
      title: ['', NoWhitespaceValidator()],
      summary: [],
      description: [],
      listTag: [],
      street: ['', Validators.required],
      ward: [''],
      district: [''],
      city: [''],
      organizerId: [Validators.required],
      startTime: [Validators.required],
      duration: [0, Validators.required],
      launchTime: [Validators.required],
      closeTime: [Validators.required],
      videoLink: ['']
    })
    this.editEventForm.patchValue({
      title: this.eventDetail.title,
      summary:  this.eventDetail.summary,
      description: this.eventDetail.description,
      listTag: this.eventDetail.listTag,
      street: this.eventDetail.location.street,
      ward: this.eventDetail.location.ward,
      district: this.eventDetail.location.district,
      city: this.eventDetail.location.city,
      organizerId: this.eventDetail.organizer.id,
      startTime: new Date(this.eventDetail.startTime),
      duration: this.eventDetail.duration,
      launchTime: new Date(this.eventDetail.launchTime),
      closeTime: new Date(this.eventDetail.closeTime),
      videoLink: this.eventDetail.videoLink
    })
  }

  onSubmit(): Observable<any> {
    let locationDto = new LocationDTO(
      this.editEventForm.get('street').value,
      this.editEventForm.get('ward').value,
      this.editEventForm.get('district').value,
      this.editEventForm.get('city').value
    );
    let eventEditRequest = new EventEditRequest(
      this.editEventForm.get('title').value,
      this.editEventForm.get('summary').value,
      this.editEventForm.get('description').value,
      this.editEventForm.get('listTag').value,
      this.editEventForm.get('startTime').value.getTime(),
      this.editEventForm.get('launchTime').value.getTime(),
      this.editEventForm.get('closeTime').value.getTime(),
      this.editEventForm.get('duration').value,
      this.editEventForm.get('organizerId').value,
      locationDto,
      this.editEventForm.get('videoLink').value
    );

    return this.eventService.editEvent(eventEditRequest, this.eventDetail.id);
  }
}
