import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { EventDetail, EventGet, EventSearchRequest, LocationDTO } from "../model/event.model";
import { OrganizerGet } from "../model/organizer.model";
import { Constants } from "../utils/constants";
import { ConvertToDate } from "../utils/time-convert";
import { TokenStorageService } from "./token-storage.service";
import { environment } from 'environments/environment';

const BASE_API = environment.host + "/api/event";

@Injectable({
    providedIn: 'root'
})
export class EventUserService {
    private popularEventList: EventGet[] = [];
    private displayPopularEventListSubject: BehaviorSubject<EventGet[]> = new BehaviorSubject<EventGet[]>([]);
    popularEventList$: Observable<EventGet[]> = this.displayPopularEventListSubject.asObservable();

    private upComingEventList: EventGet[] = [];
    private displayUpComingEventListSubject: BehaviorSubject<EventGet[]> = new BehaviorSubject<EventGet[]>([]);
    upComingEventList$: Observable<EventGet[]> = this.displayUpComingEventListSubject.asObservable();

    private liveEventList: EventGet[] = [];
    private displayLiveEventListSubject: BehaviorSubject<EventGet[]> = new BehaviorSubject<EventGet[]>([]);
    liveEventList$: Observable<EventGet[]> = this.displayLiveEventListSubject.asObservable();

    private event!: EventDetail;
    private displayEventDetailSubject: BehaviorSubject<EventDetail> = new BehaviorSubject<EventDetail>(new EventDetail(0, '', '', '', '', '', [], '', 0, 0, 0, 0, 0, 0, 0, 0, 0, new OrganizerGet(0, '', '', '', '', '', '', '', ''), new LocationDTO('', '', '', ''), [], [], false, false));
    event$: Observable<EventDetail> = this.displayEventDetailSubject.asObservable();

    public eventSearchRequest!: EventSearchRequest;
    public defaultSearch: boolean = true;
    private searchEventList: EventGet[] = [];
    private displaySearchEventListSubject: BehaviorSubject<EventGet[]> = new BehaviorSubject<EventGet[]>([]);
    searchEventList$: Observable<EventGet[]> = this.displaySearchEventListSubject.asObservable();

    private followedEventList: EventGet[] = [];
    private displayFollowedEventListSubject: BehaviorSubject<EventGet[]> = new BehaviorSubject<EventGet[]>([]);
    followedEventList$: Observable<EventGet[]> = this.displayFollowedEventListSubject.asObservable();

    private fromOrganizerEventList: EventGet[] = [];
    private displayFromOrganizerEventListSubject: BehaviorSubject<EventGet[]> = new BehaviorSubject<EventGet[]>([]);
    fromOrganizerEventList$: Observable<EventGet[]> = this.displayFromOrganizerEventListSubject.asObservable();

    public isEndEventList: boolean = false;


    constructor(
        private http: HttpClient,
        private tokenService: TokenStorageService
    ) { }

    fetchPopularEventList() {
        const request = {
            pageNo: 1,
            pageSize: 4,
            searchKey: '',
            sortDirection: 'desc',
            sortField: 'soldSlot',
            status: Constants.EVENT_STATUS_OPEN,
            type: ''
        }
        this.http.post(`${BASE_API}/get-events`, request).subscribe({
            next: (resp: any) => {
                this.popularEventList = resp.data.listEvent;
                this.updatePopularEventList();
            }
        })
    }

    fetchUpComingEventList() {
        const request = {
            pageNo: 1,
            pageSize: 4,
            searchKey: '',
            sortDirection: 'asc',
            sortField: 'startTime',
            status: Constants.EVENT_STATUS_OPEN,
            type: ''
        }
        this.http.post(`${BASE_API}/get-events`, request).subscribe({
            next: (resp: any) => {
                this.upComingEventList = resp.data.listEvent;
                this.updateUpComingEventList();
            }
        })
    }

    fetchLiveEventList() {
        const request = {
            pageNo: 1,
            pageSize: 4,
            searchKey: '',
            sortDirection: 'desc',
            sortField: 'startTime',
            status: Constants.EVENT_STATUS_LIVE,
            type: ''
        }
        this.http.post(`${BASE_API}/get-events`, request).subscribe({
            next: (resp: any) => {
                this.liveEventList = resp.data.listEvent;
                this.updateLiveEventList();
            }
        })
    }

    fetchEventDetail(eventId: number) {
        this.http.get(`${BASE_API}/${eventId}`, this.getHeader()).subscribe({
            next: (resp: any) => {
                if (resp.meta.code === 200) {
                    this.event = resp.data
                    this.event.startTime = ConvertToDate(this.event.startTime)
                    this.event.launchTime = ConvertToDate(this.event.launchTime)
                    this.event.closeTime = ConvertToDate(this.event.closeTime)
                    this.updateEventDetailData();
                }
            }
        });
    }

    toggleFollow() {
        this.http.get(`${BASE_API}/${this.event.id}/toggle-follow`, this.getHeader()).subscribe({
            next: (resp: any) => {
                this.event.followed = !this.event.followed;
                (this.event.followed) ? this.event.followerNum++ : this.event.followerNum--
                this.updateEventDetailData();
            }
        })
    }

    handleRealtimeEvent(event: any) {
        this.event.title = event.title;
        this.event.statusString = event.statusString;
        this.event.totalSlot = event.totalSlot;
        this.event.soldSlot = event.soldSlot;
        this.event.remainSlot = event.remainSlot;
        this.event.followerNum = event.followerNum;
        this.event.ticketCatalogList = event.ticketCatalogList;
        this.updateEventDetailData();
    }

    searchEvent() {
        this.http.post(`${BASE_API}/get-events`, this.eventSearchRequest).subscribe({
            next: (resp: any) => {
                this.searchEventList = resp.data.listEvent;
                this.updateSearchEventList();
            }
        })
    }

    fetchEvent() {
        this.http.post(`${BASE_API}/get-events`, this.eventSearchRequest).subscribe({
            next: (resp: any) => {
                if (resp.data.listEvent.length > 0) {
                    this.searchEventList = this.searchEventList.concat(resp.data.listEvent);
                    this.updateSearchEventList();
                    if (resp.data.listEvent.length < 2) {
                        this.isEndEventList = true
                    }
                } else {
                    this.isEndEventList = true;
                }
            }
        })
    }

    getFollowedEventList() {
        this.http.get(`${BASE_API}/followed`, this.getHeader()).subscribe({
            next: (resp: any) => {
                this.followedEventList = resp.data.listEvent;
                this.updateFollowedEventList();
            }
        })
    }

    fetchFromOrganizerEventList(organizerId: number) {
        const request = new EventSearchRequest(1, 8, 'startTime', 'desc', '', '', '', organizerId);
        this.http.post(`${BASE_API}/get-events`, request).subscribe({
            next: (resp: any) => {
                this.fromOrganizerEventList = resp.data.listEvent;
                this.updateFromOrganizerEventList();
            }
        })
    }

    private updateFromOrganizerEventList() {
        this.displayFromOrganizerEventListSubject.next(this.fromOrganizerEventList);
    }

    private updateFollowedEventList() {
        this.displayFollowedEventListSubject.next(this.followedEventList);
    }

    private updateSearchEventList() {
        this.displaySearchEventListSubject.next(this.searchEventList);
    }

    private updatePopularEventList() {
        this.displayPopularEventListSubject.next(this.popularEventList);
    }
    private updateUpComingEventList() {
        this.displayUpComingEventListSubject.next(this.upComingEventList);
    }
    private updateLiveEventList() {
        this.displayLiveEventListSubject.next(this.liveEventList);
    }
    private updateEventDetailData() {
        this.displayEventDetailSubject.next(this.event);
    }
    private getHeader() {
        if (this.tokenService.getToken() != null) {
            return {
                headers: new HttpHeaders({
                    'Content-Type': 'application/json',
                    'Access-Control-Allow-Origin': '*',
                    'Authorization': 'Bearer ' + this.tokenService.getToken()
                })
            }
        }
        return {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*'
            })
        }
    }

}