import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { EventGet } from 'app/model/event.model';
import { EventUserService } from 'app/services/event-user.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  popularEventList$: Observable<EventGet[]> = new Observable<EventGet[]>();

  upComingEventList$: Observable<EventGet[]> = new Observable<EventGet[]>();

  liveEventList$: Observable<EventGet[]> = new Observable<EventGet[]>();

  constructor(private eventService: EventUserService) { }

  ngOnInit(): void {
    this.eventService.fetchPopularEventList();
    this.eventService.fetchUpComingEventList();
    this.eventService.fetchLiveEventList();
    this.popularEventList$ = this.eventService.popularEventList$;
    this.upComingEventList$ = this.eventService.upComingEventList$;
    this.liveEventList$ = this.eventService.liveEventList$;
  }

}
