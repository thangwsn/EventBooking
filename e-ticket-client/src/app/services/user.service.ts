import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Injectable } from "@angular/core";
import { User } from "../model/account.model";
import { TokenStorageService } from "./token-storage.service";
import { environment } from 'environments/environment';

const BASE_API = environment.host + "/api";

@Injectable({
    providedIn: 'root'
})

export class UserService {
    userList: User[] = [];
    private displayItemSubject: BehaviorSubject<User[]> = new BehaviorSubject<User[]>([]);
    userList$: Observable<User[]> = this.displayItemSubject.asObservable();

    constructor(
        private http: HttpClient,
        private tokenService: TokenStorageService
    ) { }

    getAllUser() {
        this.http.get(`${BASE_API}/users`, this.getHeader()).subscribe({
            next: (resp: any) => {
                if (resp.meta.code == 200) {
                    this.userList = resp.data.userList;
                    this.updateUserListData();
                }
            }
        })
    }

    getUserDetail(userId: number): Observable<any> {
       return this.http.get(`${BASE_API}/users/${userId}`,this.getHeader());
    }

    private updateUserListData() {
        this.displayItemSubject.next(this.userList);
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