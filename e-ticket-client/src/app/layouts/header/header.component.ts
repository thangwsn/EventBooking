import { Component, OnInit, Inject } from '@angular/core';
import { DOCUMENT } from '@angular/common'
import { TokenStorageService } from 'app/services/token-storage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  loggedInUser: any = ""; 

  constructor(@Inject(DOCUMENT) private document: Document, private tokenStorageService: TokenStorageService, private _router: Router) { }

  ngOnInit(): void {
    this.loggedInUser = this.tokenStorageService.getUser()
    console.log(this.loggedInUser);
  }

  sidebarToggle()
  {
    //toggle sidebar function
    this.document.body.classList.toggle('toggle-sidebar');
  }

  signOut(): void {
    this.tokenStorageService.signOut();
    this._router.navigate(['login']);
  }
}
