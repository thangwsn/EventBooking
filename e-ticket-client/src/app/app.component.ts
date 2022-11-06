import { Component ,ElementRef} from '@angular/core';
import { Router } from '@angular/router';
import { TokenStorageService } from './services/token-storage.service';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  role: string ='';
  constructor(private elementRef: ElementRef,  public  _router: Router, private tokenStorageService: TokenStorageService) { }

  ngOnInit() {
    var s = document.createElement("script");
    s.type = "text/javascript";
    s.src = "../assets/js/main.js";
    this.elementRef.nativeElement.appendChild(s); 
  }

  isAdminSite(): boolean {
    this.role = this.tokenStorageService.getUser().role;
    return(this.role === 'ADMINISTRATOR' || this.role === 'OPERATOR') && !(this._router.url === '/register' || this._router.url === '/login' || this._router.url === '/pages-error404');
  }

  isUserSite(): boolean {
    this.role = this.tokenStorageService.getUser().role;
    return(this.role === undefined || this.role === 'USER') && !(this._router.url === '/register' || this._router.url === '/login' || this._router.url === '/pages-error404');
  }
}
