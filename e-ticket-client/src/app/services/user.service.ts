import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Injectable } from "@angular/core";
import { Address, User, UserProfile } from "../model/account.model";
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

    userProfile!: UserProfile;
    private displayUserProfileSubject: BehaviorSubject<UserProfile> = new BehaviorSubject<UserProfile>(new UserProfile(-1, '', '','','','','',-1,'',new Address('', '', '', '')));
    userProfile$: Observable<UserProfile> = this.displayUserProfileSubject.asObservable();

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

    getUserProfile() {
        this.http.get(`${BASE_API}/user-profile`, this.getHeader()).subscribe({
            next: (resp: any) => {
                if(resp.meta.code == 200) {
                    this.userProfile = resp.data;
                    if (this.userProfile.address == null) {
                        this.userProfile.address = new Address('', '', '', '')
                    }
                    this.updateUserProfileData();
                }
            },
            error: () => {

            }
        })
    }

    private updateUserListData() {
        this.displayItemSubject.next(this.userList);
    }

    private updateUserProfileData() {
        this.displayUserProfileSubject.next(this.userProfile);
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