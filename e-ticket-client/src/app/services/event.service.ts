import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable, Subject } from "rxjs";
import { EventCreateRequest, EventDetail, EventEditRequest, EventGet, LocationDTO } from "../model/event.model";
import { OrganizerGet } from "../model/organizer.model";
import { ConvertToDate } from "../utils/time-convert";
import { TokenStorageService } from "./token-storage.service";
import { environment } from 'environments/environment';

const BASE_API_ADMIN = environment.host + "/api/emp/event";

@Injectable({
    providedIn: 'root'
})
export class EventService {
    private httpOptions: any;
    private eventList: EventGet[] = [];
    private displayItemSubject: BehaviorSubject<EventGet[]> = new BehaviorSubject<EventGet[]>([]);

    private event!: EventDetail;
    private displayEventDetailSubject: BehaviorSubject<EventDetail> = new BehaviorSubject<EventDetail>(new EventDetail(0, '', '', '', '', '', [], '', 0, 0, 0, 0, 0, 0, 0, 0, 0, new OrganizerGet(0, '', '', '', '', '', '', '', ''), new LocationDTO('', '', '', ''), [], [], false, false));

    private eventStatusList: string[] = [];
    private displayEventStatusListSubject: BehaviorSubject<string[]> = new BehaviorSubject<string[]>([]);

    eventList$: Observable<EventGet[]> = this.displayItemSubject.asObservable();
    event$: Observable<EventDetail> = this.displayEventDetailSubject.asObservable();
    eventStatusList$: Observable<string[]> = this.displayEventStatusListSubject.asObservable();
    constructor(
        private tokenService: TokenStorageService,
        private http: HttpClient
    ) { }

    fetchEventListForAdmin(): void {
        this.httpOptions = this.getHeader();
        this.http.get(BASE_API_ADMIN, this.httpOptions).subscribe({
            next: (resp: any) => {
                if (resp.meta.code == 200) {
                    this.eventList = resp.data.listEvent;
                    this.updateEventListData();
                }
            },
            error: (err) => {
                console.log(err);
            }
        })
    }

    createEvent(eventCreateRequest: EventCreateRequest, selectedFiles: any[]): Observable<any> {
        this.httpOptions = this.getHeaderMultipart();
        const formData: FormData = new FormData();
        for (let i = 0; i < selectedFiles.length; i++) {
            formData.append("files", selectedFiles[i], selectedFiles[i].name);
        }
        formData.append("data", JSON.stringify(eventCreateRequest));
        return this.http.post(BASE_API_ADMIN, formData, this.httpOptions);
    }

    getListEventType(): Observable<any> {
        this.httpOptions = this.getHeader();
        return this.http.get(BASE_API_ADMIN + "/type", this.httpOptions);
    }
    
    getListEventStatus(eventId: number) {
        this.httpOptions = this.getHeader();
        this.http.get(`${BASE_API_ADMIN}/status/${eventId}`, this.httpOptions).subscribe({
            next: (resp: any) => {
                this.eventStatusList = resp;
                this.updateEventStatusListData();
            }
        });
    }

    changeEventStatus(request: any): Observable<number> {
        var subject = new Subject<number>();
        this.httpOptions = this.getHeader();
        this.http.post(`${BASE_API_ADMIN}/change-status`, request, this.httpOptions).subscribe({
            next: (resp: any) => {
                if (resp.meta.code === 200) {
                    this.event.statusString = request.targetStatus
                    this.updateEventDetailData()
                    this.getListEventStatus(request.eventId)
                }
                subject.next(resp.meta.code)
            },
        });
        return subject.asObservable();
    }

    fetchEventDetail(eventId: number) {
        this.httpOptions = this.getHeader();
        this.http.get(`${BASE_API_ADMIN}/${eventId}`, this.httpOptions).subscribe({
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

    createTicketCatalog(ticketCreateRequest: any, eventId: number): Observable<any> {
        return this.http.post(`${BASE_API_ADMIN}/${eventId}/ticket-catalog`, ticketCreateRequest, this.getHeader());
    }

    editTicketCatalog(ticketEditRequest: any, eventId: number): Observable<any> {
        return this.http.put(`${BASE_API_ADMIN}/${eventId}/ticket-catalog/${ticketEditRequest.id}`, ticketEditRequest, this.getHeader())
    }

    removeTicketCatalog(eventId: number, ticketCatalogId: number): Observable<any> {
        return this.http.delete(`${BASE_API_ADMIN}/${eventId}/ticket-catalog/${ticketCatalogId}`, this.getHeader());
    }

    removeEvent(eventId: any): Observable<any> {
        return this.http.delete(`${BASE_API_ADMIN}/${eventId}`, this.getHeader());
    }

    editEvent(event: EventEditRequest, eventId: number): Observable<any> {
        return this.http.put(`${BASE_API_ADMIN}/${eventId}`, event, this.getHeader());
    }

    private updateEventListData() {
        this.displayItemSubject.next(this.eventList);
    }

    private updateEventDetailData() {
        this.displayEventDetailSubject.next(this.event);
    }

    private updateEventStatusListData() {
        this.displayEventStatusListSubject.next(this.eventStatusList);
    }

    private getHeader() {
        return {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*',
                'Authorization': 'Bearer ' + this.tokenService.getToken()
            })
        }
    }

    private getHeaderMultipart() {
        return {
            headers: new HttpHeaders({
                // 'Content-Type': 'multipart/form-data',
                'Access-Control-Allow-Origin': '*',
                'Authorization': 'Bearer ' + this.tokenService.getToken()
            })
        }
    }
}