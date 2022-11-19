import { Constants } from "../utils/constants";
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Injectable } from "@angular/core";
import { AccountInfo, UserLoginRequest, UserSignUpRequest } from "../model/account.model";
import { TokenStorageService } from "./token-storage.service";
import { environment } from 'environments/environment';

const BASE_API = environment.host + "/api";

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*' })
};

@Injectable({
  providedIn: 'root'
})

export class AccountService {
  private accountInfo!: AccountInfo;
  private displayAccountInfoSubject: BehaviorSubject<AccountInfo> = new BehaviorSubject<AccountInfo>(new AccountInfo('', ''));
  accountInfo$: Observable<AccountInfo> = this.displayAccountInfoSubject.asObservable();

  constructor(
    private http: HttpClient,
    private tokenService: TokenStorageService
  ) { }

  login(userLoginRequest: UserLoginRequest): Observable<any> {
    return this.http.post(BASE_API + '/login', JSON.stringify(userLoginRequest), httpOptions);
  }

  register(userSignUpRequest: UserSignUpRequest): Observable<any> {
    return this.http.post(BASE_API + '/signup-user', JSON.stringify(userSignUpRequest), httpOptions);
  }

  getUserInfo() {
    return this.http.get(BASE_API + '/account-info', this.getHeader()).subscribe({
      next: (resp: any) => {
        if (resp.meta.code === 200) {
          this.accountInfo = resp.data;
          this.displayAccountInfoSubject.next(this.accountInfo);
        }
      }
    });
  }

  verifyRegisterUser(userId: number, activeCode: string): Observable<any> {
    return this.http.get(`${BASE_API}/verify-register?user_id=${userId}&active_code=${activeCode}`);
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