import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';
import { EventDetail } from 'src/app/model/event.model';
import { EventService } from 'src/app/services/event.service';
import { Constants } from 'src/app/utils/constants';

@Component({
  selector: 'app-event-detail',
  templateUrl: './event-detail.component.html',
  styleUrls: ['./event-detail.component.css']
})
export class EventDetailComponent implements OnInit {
  BASE_API = Constants.HOST
  event$: Observable<EventDetail> = new Observable<EventDetail>();
  eventStatusList$: Observable<string[]> = new Observable<string[]>();
  eventId!: number;
  changeStatusForm: any = new FormGroup({})
  hiddenAddCatalog: boolean = false;
  hiddenEditCatalog: boolean = false;

  constructor(private _route: ActivatedRoute, private eventService: EventService, private fb: FormBuilder, private toastr: ToastrService) { }

  ngOnInit(): void {
    let paramValue = this._route.snapshot.paramMap.get('eventId');
    if (paramValue !== null) {
      this.eventId =parseInt(paramValue);
      this.eventService.fetchEventDetail(this.eventId);
      this.event$ = this.eventService.event$;
      this.updateHidden();

      this.eventService.getListEventStatus(this.eventId);
      this.eventStatusList$ = this.eventService.eventStatusList$;

      this.changeStatusForm = this.fb.group({
        targetStatus: [Validators.required]
      })
    }

  }

  changeStatus() {
    const request = {
      eventId: this.eventId,
      targetStatus: this.changeStatusForm.get('targetStatus').value
    }
    this.eventService.changeEventStatus(request).subscribe({
      next: (statusCode) => {
        if (statusCode == 200) {
          this.changeStatusForm.patchValue({
            targetStatus: ''
          })
          this.updateHidden();
          this.toastr.success('', 'Change event status successfully!', {timeOut: 1000, newestOnTop: true, tapToDismiss: true});

        } else {
          this.toastr.error('', 'Change event status failure!', {timeOut: 1000, newestOnTop: true, tapToDismiss: true});
        }
      }
    })
  }

  updateHidden() {
    this.event$.subscribe({
      next: (event) => {
        this.hiddenAddCatalog = (event.statusString === 'LIVE') || (event.statusString === 'FINISH')
        this.hiddenEditCatalog = this.hiddenAddCatalog;
      }
    })
  }

  getClassOfEventType() {
    var type = '';
    this.event$.subscribe({
      next: (event: EventDetail) => {
        type = event.typeString;
      }
    })
    var eventTypeClass = '';
    switch (type) {
      case 'CHARGE':
        eventTypeClass = 'badge rounded-pill bg-primary'
        break;
      case 'FREE':
        eventTypeClass = 'badge rounded-pill bg-success'
        break;
      default:
        eventTypeClass = 'badge rounded-pill bg-danger'
    }
    return eventTypeClass;
  }

  getClassOfEventStatus() {
    var status = ''
    this.event$.subscribe({
      next: (event: EventDetail) => {
        status = event.statusString;
      }
    })
    var eventStatusClass = '';
    switch(status) {
      case Constants.EVENT_STATUS_CREATED:
        eventStatusClass = 'badge bg-primary'
        break;
      case Constants.EVENT_STATUS_OPEN:
        eventStatusClass = 'badge bg-success'
        break;
      case Constants.EVENT_STATUS_SOLD:
        eventStatusClass = 'badge bg-danger'
        break;
      case Constants.EVENT_STATUS_LIVE:
        eventStatusClass = 'badge bg-info text-dark'
        break;
      case Constants.EVENT_STATUS_FINISH:
        eventStatusClass = 'badge bg-secondary'
        break;
      default:
        eventStatusClass = 'badge bg-light text-dark'
    }
    return eventStatusClass;
  }

}
