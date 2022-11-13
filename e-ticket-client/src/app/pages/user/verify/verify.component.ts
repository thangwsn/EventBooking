import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService, PrimeNGConfig } from 'primeng/api';
import { AccountService } from 'src/app/services/account.service';

@Component({
  selector: 'app-verify',
  templateUrl: './verify.component.html',
  styleUrls: ['./verify.component.css'],
  providers: [MessageService]
})
export class VerifyComponent implements OnInit {
  backRegisterBtn: boolean = true;
  content?: string;
  success: boolean = true;
  constructor(
    private primengConfig: PrimeNGConfig,
    private _route: ActivatedRoute,
    private _router: Router,
    private accountService: AccountService,
    private messageService: MessageService) { }

  ngOnInit(): void {
    this.primengConfig.ripple = true;

    var userId = -1;
    var activeCode = '';
    this._route.queryParams.subscribe(params => {
      userId = params['user_id'];
      activeCode = params['active_code'];
    })
    if (userId !== undefined && activeCode !== undefined) {
      this.backRegisterBtn = false;
      this.accountService.verifyRegisterUser(userId, activeCode).subscribe({
        next: (resp: any) => {
          if (resp.meta.code === 200) {
            this.content = "Verify successfully!";
            this.messageService.add({key:'bc', severity: 'success', summary: 'Success', detail: 'Verify successfully!' });
          } else if (resp.meta.code === 100) {
            this.content = "Fail";
            this.messageService.add({key:'bc', severity: 'error', summary: 'Error', detail: 'Verify Failure' });
          }
        },
        error: () => {
          this.content = "Fail";
          this.messageService.add({key:'bc', severity: 'error', summary: 'Error', detail: 'Verify Failure' });
        }
      })
    } else {
      this.content = 'Please check mail to verify register account'  
    }
  }
}
