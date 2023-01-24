import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { EventGet, EventSearchRequest } from 'app/model/event.model';
import { EventUserService } from 'app/services/event-user.service';
import { Constants } from 'app/utils/constants';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  popularEventList$: Observable<EventGet[]> = new Observable<EventGet[]>();

  upComingEventList$: Observable<EventGet[]> = new Observable<EventGet[]>();

  liveEventList$: Observable<EventGet[]> = new Observable<EventGet[]>();

  BASE_API: string = environment.host;

  constructor(private eventService: EventUserService, private _router: Router) { }

  ngOnInit(): void {
    this.eventService.fetchPopularEventList();
    this.eventService.fetchUpComingEventList();
    this.eventService.fetchLiveEventList();
    this.popularEventList$ = this.eventService.popularEventList$;
    this.upComingEventList$ = this.eventService.upComingEventList$;
    this.liveEventList$ = this.eventService.liveEventList$;
  }

  viewAllPopular() {
    this.eventService.defaultSearch = false;
    this.eventService.isEndEventList = false;
    this.eventService.eventSearchRequest = new EventSearchRequest(1, 2, 'soldSlot', 'desc', '', Constants.EVENT_STATUS_OPEN, '', -1);
    this._router.navigate(['/event']);
  }

  viewAllUpcomingSearch() {
    this.eventService.defaultSearch = false;
    this.eventService.isEndEventList = false;
    this.eventService.eventSearchRequest = new EventSearchRequest(1, 2, 'startTime', 'asc', '', Constants.EVENT_STATUS_OPEN, '', -1);
    this._router.navigate(['/event']);
  }  

  viewAllLiveSearch() {
    this.eventService.defaultSearch = false;
    this.eventService.isEndEventList = false;
    this.eventService.eventSearchRequest = new EventSearchRequest(1, 2, 'startTime', 'desc', '', Constants.EVENT_STATUS_LIVE, '', -1);
    this._router.navigate(['/event']);
  }  
}
