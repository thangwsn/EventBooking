import { Component, OnInit } from '@angular/core';
import { EventGet } from 'app/model/event.model';
import { EventUserService } from 'app/services/event-user.service';
import { PrimeNGConfig } from 'primeng/api';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-followed-event-list',
  templateUrl: './followed-event-list.component.html',
  styleUrls: ['./followed-event-list.component.css']
})
export class FollowedEventListComponent implements OnInit {
  blockedDocument: boolean = false;
  followedEventList$: Observable<EventGet[]> = new Observable<EventGet[]>();

  constructor(private eventService: EventUserService,
    private primengConfig: PrimeNGConfig) { }

  ngOnInit(): void {
    this.primengConfig.ripple = true;
    this.eventService.getFollowedEventList();
    this.followedEventList$ = this.eventService.followedEventList$
  }

}
