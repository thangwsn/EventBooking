import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { EventDetail, EventGet } from "../model/event.model";
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
    private displayEventDetailSubject: BehaviorSubject<EventDetail> = new BehaviorSubject<EventDetail>(new EventDetail(0, '', '', '', '', '', [], '', 0, 0, 0, 0, 0, 0, 0, 0, 0, new OrganizerGet(0, '', '', '', '', '', '', ''), '', [], [], false, false));
    event$: Observable<EventDetail> = this.displayEventDetailSubject.asObservable();

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
            sortDirection: 'desc',
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