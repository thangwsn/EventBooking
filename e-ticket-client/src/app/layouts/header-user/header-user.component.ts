import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TokenStorageService } from 'src/app/services/token-storage.service';

@Component({
  selector: 'app-header-user',
  templateUrl: './header-user.component.html',
  styleUrls: ['./header-user.component.css']
})
export class HeaderUserComponent implements OnInit {

  userLoggedIn: any;
  constructor(private tokenStorageService: TokenStorageService, private _router: Router) { }

  ngOnInit(): void {
  }

  isUserLoggedIn() {
    this.userLoggedIn = this.tokenStorageService.getUser();
    return this.userLoggedIn !== undefined && this.userLoggedIn.role === 'USER'
  }

  signOut(): void {
    this.tokenStorageService.signOut();
    this._router.navigate(['login']);
  }

}
