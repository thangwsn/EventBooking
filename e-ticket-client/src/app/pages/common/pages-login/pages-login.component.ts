import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Route, Router } from '@angular/router';
import { UserLoginRequest } from 'app/model/account.model';
import { AccountService } from 'app/services/account.service';
import { TokenStorageService } from 'app/services/token-storage.service';
import { NoWhitespaceValidator } from 'app/utils/no-whitespace.validator';

@Component({
  selector: 'app-pages-login',
  templateUrl: './pages-login.component.html',
  styleUrls: ['./pages-login.component.css']
})
export class PagesLoginComponent implements OnInit {

  signInForm: FormGroup = new FormGroup({});
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  role: string = '';

  constructor(private fb: FormBuilder, 
    private accountService: AccountService, 
    private tokenStorageService: TokenStorageService, 
    private _router: Router) { }

  ngOnInit(): void {
    if (this.tokenStorageService.getToken()) {
      this.isLoggedIn = true;
      this.role = this.tokenStorageService.getUser().role;
      if (this.role === 'ADMINISTRATOR' || this.role === 'OPERATOR') {
        this._router.navigate(['dashboard']);
      } else if (this.role === 'USER') {
        this._router.navigate(['']);
      }
      return;
    }

    this.signInForm = this.fb.group({
      username: [
        '',
        Validators.compose([
          NoWhitespaceValidator(),
          Validators.required,
          Validators.minLength(6),
          // Validators.pattern(/^[a-z]{6,32}$/i),
        ]),
      ],
      password: [
        '',
        Validators.compose([
          Validators.required,
          Validators.minLength(6),
          // Validators.pattern(/^(?=.*[!@#$%^&*]+)[a-z0-9!@#$%^&*]{6,32}$/),
        ]),
      ],
      rememberMe: false,
    });
  }


  onSubmit(): void {
    const username = this.signInForm.get("username")?.value;
    const password = this.signInForm.get("password")?.value;
    const userLoginRequest = new UserLoginRequest(username, password);
    this.accountService.login(userLoginRequest).subscribe({
      next: (response: { data: { jwtToken: string; username: any; role: any; }; }) => {
        this.tokenStorageService.saveToken(response.data.jwtToken);
        this.tokenStorageService.saveUser({ username: response.data.username, role: response.data.role });
        this.isLoggedIn = true;
        this.isLoginFailed = false;
        this.role = this.tokenStorageService.getUser().role;
        if (this.role === 'ADMINISTRATOR' || this.role === 'OPERATOR') {
          this._router.navigate(['dashboard']);
        } else if (this.role === 'USER') {
          this._router.navigate(['']);
        }
        
      },
      error: (err: { error: { message: string; }; }) => {
        console.log(err);
        this.errorMessage = err.error.message;
        this.isLoginFailed = true;
      }
    }
    )
  }

}
