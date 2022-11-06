import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { OrganizerCreateRequest, OrganizerGet } from "../model/organizer.model";
import { Constants } from "../utils/constants";
import { TokenStorageService } from "./token-storage.service";

const BASE_API = Constants.HOST + "/api/organizer";

// const httpOptions = {
//     headers: new HttpHeaders({
//         'Content-Type': 'application/json',
//         'Access-Control-Allow-Origin': '*'
//     })
// };

@Injectable({
    providedIn: "root"
})

export class OrganizerService {
    private httpOptions: any;
    private organizerList: OrganizerGet[] = [];
    private displayItemSubject: BehaviorSubject<OrganizerGet[]> = new BehaviorSubject<OrganizerGet[]>([]);

    organizerList$: Observable<OrganizerGet[]> = this.displayItemSubject.asObservable();

    constructor(private http: HttpClient, private tokenService: TokenStorageService) {
    }

    create(organizerCreateRequest: OrganizerCreateRequest): Observable<any> {
        this.httpOptions = this.getHeader();
        return this.http.post(BASE_API, JSON.stringify(organizerCreateRequest), this.httpOptions);
    }

    fetchOrganizers() {
        this.httpOptions = this.getHeader();
        this.http.get(BASE_API, this.httpOptions).subscribe({
            next: (resp: any) => {
                if (resp.meta.code == 200) {
                    this.organizerList = resp.data.listOrganizer || [];
                    this.updateOrganizerListData();
                }
            },
            error: (err) => {
                console.log(err);
            }
        });
    }

    getOrganizersForCreateEvent(): Observable<any> {
        this.httpOptions = this.getHeader();
        return this.http.get(BASE_API, this.httpOptions);
    }

    removeOrganizer(organizerId: number): Observable<any> {
        this.httpOptions = this.getHeader();
        return this.http.delete(BASE_API + "/" + organizerId, this.httpOptions);
    }

    private updateOrganizerListData() {
        this.displayItemSubject.next(this.organizerList);
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
}