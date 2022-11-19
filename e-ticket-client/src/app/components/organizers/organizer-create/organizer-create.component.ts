import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { OrganizerCreateRequest } from 'app/model/organizer.model';
import { OrganizerService } from 'app/services/organizer.service';
import { NoWhitespaceValidator } from 'app/utils/no-whitespace.validator';

@Component({
  selector: 'app-organizer-create',
  templateUrl: './organizer-create.component.html',
  styleUrls: ['./organizer-create.component.css']
})
export class OrganizerCreateComponent implements OnInit {

  createOrganizerForm: any = new FormGroup({})

  constructor(
    private fb: FormBuilder, 
    private organizerService: OrganizerService, 
    private _router: Router,
    private toastr: ToastrService) { }

  ngOnInit(): void {
    this.createOrganizerForm = this.fb.group({
      name: [
        '',
        Validators.compose([
          NoWhitespaceValidator(),
          Validators.required
        ])
      ],
      email: [
        '',
        Validators.compose([
          Validators.required,
          Validators.email,
        ])
      ],
      mobile: [
        '',
        Validators.compose([
          NoWhitespaceValidator(),
          Validators.required,
          // Validators.pattern("\d{10}")
        ])
      ],
      representative: [
        '',
        Validators.compose([
          NoWhitespaceValidator(),
          Validators.required,
        ])
      ],
      taxCode: [
        '',
        Validators.compose([
          NoWhitespaceValidator(),
          Validators.required,
          // Validators.pattern("\d*")
        ])
      ],
      address: [
        '',
        Validators.compose([
          NoWhitespaceValidator(),
          Validators.required,
        ])
      ],
      summary: [
        ''
      ],
    })

  }

  onSubmit(): void {
    let organizerCreateRequest = new OrganizerCreateRequest(
      this.createOrganizerForm.get('name').value,
      this.createOrganizerForm.get('email').value,
      this.createOrganizerForm.get('mobile').value,
      this.createOrganizerForm.get('representative').value,
      this.createOrganizerForm.get('taxCode').value,
      this.createOrganizerForm.get('address').value,
      this.createOrganizerForm.get('summary').value
    )
    this.organizerService.create(organizerCreateRequest).subscribe({
      next: (response: any) => {
        if (response.meta.code == 200) {
          this.toastr.success('', 'Create organizer successfully!', {timeOut: 1000, newestOnTop: true, tapToDismiss: true});
          this._router.navigate(["admin/organizers"]);
        } else {
          this.toastr.error('', 'Create organizer failure!', {timeOut: 1000, newestOnTop: true, tapToDismiss: true});
        }
      },
      error: (resp) => {
        this.toastr.error('', 'Create organizer failure!', {timeOut: 1000, newestOnTop: true, tapToDismiss: true});
      }
    });
  }
}
