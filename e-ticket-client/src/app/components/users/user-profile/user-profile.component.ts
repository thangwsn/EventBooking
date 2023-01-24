import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PrimeNGConfig, MessageService, ConfirmationService, } from 'primeng/api';

import { Address, ChangePasswordRequest, UpdateInformationRequest, UserProfile } from 'app/model/account.model';
import { AccountService } from 'app/services/account.service';
import { UserService } from 'app/services/user.service';
import {  Observable, Subscription } from 'rxjs';
import { TokenStorageService } from 'app/services/token-storage.service';
import { Router } from '@angular/router';
import { NoWhitespaceValidator } from 'app/utils/no-whitespace.validator';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css'],
  providers: [MessageService, ConfirmationService]
})
export class UserProfileComponent implements OnInit {
  blockedDocument: boolean = true;
  @ViewChild('overview', {static: false, read: ElementRef}) overviewTab!: ElementRef<HTMLButtonElement>;
  clickedElement: Subscription = new Subscription();
  userProfile$: Observable<UserProfile> = new Observable<UserProfile>();
  changePasswordForm: any = new FormGroup({});
  updateInformationForm: any = new FormGroup({});
  selectedFileArr: any[] = [];

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private accountService: AccountService,
    private primengConfig: PrimeNGConfig,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private tokenStorageService: TokenStorageService,
    private _router: Router
  ) { }

  ngOnInit(): void {
    this.primengConfig.ripple = true;
    this.userService.getUserProfile();
    this.userProfile$ = this.userService.userProfile$;
    this.changePasswordForm = this.fb.group({
      currentPassword: ['', [Validators.required, NoWhitespaceValidator(), Validators.minLength(8)]],
      newPassword: ['', [Validators.required,  NoWhitespaceValidator(), Validators.minLength(8)]],
      againPassword: ['', [Validators.required,  NoWhitespaceValidator(), Validators.minLength(8)]]
    });
    this.updateInformationForm = this.fb.group({
      fullName: [],
      gender: [],
      dateOfBirth: [],
      street: [],
      ward: [],
      district: [],
      city: []
    })
    this.userProfile$.subscribe({
      next: (userProfile: UserProfile) => {
        this.updateInformationForm.setValue({
          fullName: userProfile.fullName,
          gender: userProfile.gender,
          dateOfBirth: new Date(userProfile.dateOfBirth),
          street: userProfile.address.street,
          ward: userProfile.address.ward,
          district: userProfile.address.district,
          city: userProfile.address.city
        })
      }
    })
    
  }

  ngAfterViewInit(): void {
    this.blockedDocument = false;
    
  }

  changePasswordSubmit() {
    this.blockedDocument = true;
    let changePassword = new ChangePasswordRequest(this.changePasswordForm.get('currentPassword').value, this.changePasswordForm.get('newPassword').value);
    this.accountService.changePassword(changePassword).subscribe({
      next: (resp: any) => {
        this.blockedDocument = false;
        if (resp.meta.code == 200) {
          this.changePasswordForm.setValue({
            currentPassword: '',
            newPassword: '',
            againPassword: ''
          })
          this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Change password successfully!' });
          this.confirmationService.confirm({
            message: 'Keep login?',
            header: 'Confirmation',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
              this.accountService.updateToken().subscribe({
                next: (resp: any) => {
                  if (resp.meta.code == 200) {
                    this.tokenStorageService.saveToken(resp.data.jwtToken);
                    this.tokenStorageService.saveUser({ username: resp.data.username, role: resp.data.role });
                  }
                }
              })
              this.overviewTab.nativeElement.click();
            },
            reject: () => {
              this.tokenStorageService.signOut();
              this._router.navigate(['login']);
            }
          });
        } else if (resp.meta.data == 400) {

        }
      },

    })

  }

  selectFiles(event: any) {
    for(let file of event.files) {
      this.selectedFileArr.push(file);
    }
  }

  updateInformationSubmit() {
    let updateInformationRequest = new UpdateInformationRequest(
      this.updateInformationForm.get('fullName').value,
      this.updateInformationForm.get('gender').value,
      this.updateInformationForm.get('dateOfBirth').value.getTime(),
      new Address(this.updateInformationForm.get('street').value, this.updateInformationForm.get('ward').value, 
                  this.updateInformationForm.get('district').value, this.updateInformationForm.get('city').value)
    )
    this.accountService.updateInformation(updateInformationRequest).subscribe({
      next: (resp: any) => {
        if (resp.meta.code == 200) {
          this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Update information successfully!' });
          this.userService.getUserProfile();
          this.userProfile$ = this.userService.userProfile$

          
        }
      }
    })
  }

}
