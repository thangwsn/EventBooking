import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { ConfirmationService, Message, MessageService, PrimeNGConfig } from 'primeng/api';
import { EventDetail } from 'app/model/event.model';
import { EventService } from 'app/services/event.service';
import { Constants } from 'app/utils/constants';

@Component({
  selector: 'app-event-detail',
  templateUrl: './event-detail.component.html',
  styleUrls: ['./event-detail.component.css'],
  providers: [ConfirmationService, MessageService]
})
export class EventDetailComponent implements OnInit {
  BASE_API = Constants.HOST
  event$: Observable<EventDetail> = new Observable<EventDetail>();
  eventStatusList$: Observable<string[]> = new Observable<string[]>();
  eventId!: number;
  changeStatusForm: any = new FormGroup({})
  hiddenAddCatalog: boolean = false;
  hiddenEditCatalog: boolean = false;

  constructor(private _route: ActivatedRoute,
    private eventService: EventService,
    private fb: FormBuilder,
    private confirmationService: ConfirmationService,
    private primengConfig: PrimeNGConfig,
    private messageService: MessageService) { }

  ngOnInit(): void {
    this.primengConfig.ripple = true;
    let paramValue = this._route.snapshot.paramMap.get('eventId');
    if (paramValue !== null) {
      this.eventId = parseInt(paramValue);
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
          this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Update event status successfully!' });
        } else {
          this.messageService.add({ severity: 'error', summary: 'Failure', detail: 'Change event status failure!' });
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
    switch (status) {
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

  openRemoveConfirm(event: any) {
    this.confirmationService.confirm({
      message: 'Are you sure that you want to delete this event?',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.handleRemove(event.id)
      },
      reject: () => {
        this.messageService.add({ severity: 'info', summary: 'Success', detail: 'Cancel removing!' });
      }
    });
  }

  private handleRemove(eventId: number) {
    // coding here
  }
}
