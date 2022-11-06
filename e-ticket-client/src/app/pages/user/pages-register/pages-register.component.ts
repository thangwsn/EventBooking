import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserSignUpRequest } from 'src/app/model/account.model';
import { AccountService } from 'src/app/services/account.service';
import { NoWhitespaceValidator } from 'src/app/utils/no-whitespace.validator';

@Component({
  selector: 'app-pages-register',
  templateUrl: './pages-register.component.html',
  styleUrls: ['./pages-register.component.css']
})
export class PagesRegisterComponent implements OnInit {
  signUpForm: any = new FormGroup({});

  constructor(private fb: FormBuilder, private accountService: AccountService, private _router: Router) { }

  ngOnInit(): void {
    this.signUpForm = this.fb.group({
      username: [
        '',
        Validators.compose([
          NoWhitespaceValidator(),
          Validators.required,
          Validators.minLength(6),
          // Validators.pattern(/^[a-z]{6,32}$/i),
        ]),
      ],
      email: [
        '',
        Validators.compose([
          Validators.required,
          Validators.email,
        ])
      ],
      password: [
        '',
        Validators.compose([
          Validators.required,
          Validators.minLength(6),
        ]),
      ],
      againPassword: [
        '',
        Validators.compose([
          Validators.required,
          Validators.minLength(6),
        ]),
      ],
      acceptTerm: [
        false,
        Validators.required
      ],
    })
  }

  onSubmit() {
    const username = this.signUpForm.get("username").value;
    const email = this.signUpForm.get("email").value;
    const password = this.signUpForm.get("password").value;
    const userSignUpRequest = new UserSignUpRequest(username, email, password);
    this.accountService.register(userSignUpRequest).subscribe({
      next: (response: any) => {
        if (response.meta.code == 200) {
          // this._router.navigate(["verify-account"]);
          this._router.navigate(["login"]);
        }
        console.log(response);
      }
    });
  }

}
