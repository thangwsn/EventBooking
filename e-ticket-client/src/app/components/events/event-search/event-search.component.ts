import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { EventGet, EventSearchRequest } from 'app/model/event.model';
import { EventUserService } from 'app/services/event-user.service';
import { OrganizerService } from 'app/services/organizer.service';
import { Constants } from 'app/utils/constants';
import { Observable, last } from 'rxjs';

@Component({
  selector: 'app-event-search',
  templateUrl: './event-search.component.html',
  styleUrls: ['./event-search.component.css']
})
export class EventSearchComponent implements OnInit {
  searchEventList$: Observable<EventGet[]> = new Observable<EventGet[]>();
  listOrganizer: any;
  searchEventForm: any = new FormGroup({});
  pageNo: number = 1;
  isEnd: boolean = false;

  constructor(private eventService: EventUserService, 
    private organizerService: OrganizerService,
    private fb: FormBuilder ) { }

  ngOnInit(): void {
    this.searchEventForm = this.fb.group({
      searchKey: '',
      organizerId: '',
      eventStatus: '',
      direction: '',
      eventType: '',
      sortField: 'startTime'
    })
    this.pageNo = 1;
    window.scroll(0, 0);
    const eventSearchRequest: EventSearchRequest = new EventSearchRequest(1, 8, 'startTime', 'desc', '', '', '', -1);
    if (this.eventService.defaultSearch) {
      this.eventService.eventSearchRequest = eventSearchRequest;
    } else {
      this.searchEventForm.setValue({
        searchKey: '',
        organizerId: '',
        eventStatus: this.eventService.eventSearchRequest.status,
        direction: this.eventService.eventSearchRequest.sortDirection,
        eventType: '',
        sortField: this.eventService.eventSearchRequest.sortField
      })
    }
    this.eventService.searchEvent();
    this.searchEventList$ = this.eventService.searchEventList$;
    this.eventService.defaultSearch = true;
    this.organizerService.getOrganizersForCreateEvent().subscribe({
      next: (resp: any) => {
        if (resp.meta.code == 200) {
          this.listOrganizer = resp.data.listOrganizer || [];
        }
      }
    })
  }

  ngOnDestroy(): void {
    this.pageNo = 1;
    this.searchEventList$.subscribe();
  }

  onSearch() {
    this.eventService.isEndEventList = false;
    this.pageNo = 1;
    const searchKey = this.searchEventForm.get('searchKey').value;
    const organizerId = this.searchEventForm.get('organizerId').value || '-1';
    const status = this.searchEventForm.get('eventStatus').value;
    const type = this.searchEventForm.get('eventType').value;
    const sortField = this.searchEventForm.get('sortField').value || 'startTime';
    const direction = this.searchEventForm.get('direction').value || 'desc';
    const eventSearchRequest = new EventSearchRequest(this.pageNo, 2, sortField, direction, type, status, searchKey, organizerId);
    this.eventService.eventSearchRequest = eventSearchRequest;
    this.eventService.searchEvent();
    this.searchEventList$ = this.eventService.searchEventList$;
  }

  onScroll(event$ : any) {
    if (!this.eventService.isEndEventList) {
      this.pageNo++;
      const searchKey = this.searchEventForm.get('searchKey').value;
      const organizerId = this.searchEventForm.get('organizerId').value || '-1';
      const status = this.searchEventForm.get('eventStatus').value;
      const type = this.searchEventForm.get('eventType').value;
      const sortField = this.searchEventForm.get('sortField').value || 'startTime';
      const direction = this.searchEventForm.get('direction').value || 'desc';
      const eventSearchRequest = new EventSearchRequest(this.pageNo, 2, sortField, direction, type, status, searchKey, organizerId);
      this.eventService.eventSearchRequest = eventSearchRequest;
      this.eventService.fetchEvent();
      this.searchEventList$ = this.eventService.searchEventList$;
    }
  } 

  isTargetElement(eventId: number, searchEventList$: Observable<EventGet[]>) : boolean{
    var eventList: EventGet | any[] =[];
    searchEventList$.subscribe(list => {
      eventList = list;
    });
    return eventList[eventList.length - 1].id == eventId;
  }

}
