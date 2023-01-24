import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService, PrimeNGConfig } from 'primeng/api';
import { OrganizerService } from 'app/services/organizer.service';
import { NoWhitespaceValidator } from 'app/utils/no-whitespace.validator';
import { OrganizerEdit } from 'app/model/organizer.model';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-organizer-edit',
  templateUrl: './organizer-edit.component.html',
  styleUrls: ['./organizer-edit.component.css'],
  providers: [MessageService]
})
export class OrganizerEditComponent implements OnInit {
  blockedDocument: boolean = false;
  editOrganizerForm: any = new FormGroup({});
  organizer!: OrganizerEdit;

  constructor(private fb: FormBuilder,
    private organizerService: OrganizerService,
    private _router: Router,
    private _route: ActivatedRoute,
    private primengConfig: PrimeNGConfig,
    private messageService: MessageService) { }

  ngOnInit(): void {
    this.primengConfig.ripple = true;
    let paramValue = this._route.snapshot.paramMap.get('organizerId');
    if (paramValue !== null) {
      let organizerId = parseInt(paramValue);
      this.editOrganizerForm = this.fb.group({
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
      this.organizerService.getOrganizerForEditing(organizerId).subscribe({
        next: (resp: any) => {
          this.organizer = resp.data;
          this.editOrganizerForm.setValue({
            name: this.organizer.name,
            email: this.organizer.email,
            mobile: this.organizer.mobile,
            representative: this.organizer.representative,
            taxCode: this.organizer.taxCode,
            address: this.organizer.address,
            summary: this.organizer.summary
          })
        }
      })



    }

    else {
      this._router.navigate(["admin/organizers"]);
    }

  }

  onSubmit() {
   this.blockedDocument = true;
    let organizerEditRequest = new OrganizerEdit(this.organizer.id, this.editOrganizerForm.get('name').value,
      this.editOrganizerForm.get('email').value, this.editOrganizerForm.get('mobile').value,
      this.editOrganizerForm.get('representative').value, this.editOrganizerForm.get('taxCode').value,
      this.editOrganizerForm.get('address').value, this.editOrganizerForm.get('summary').value);
    this.organizerService.updateOrganizer(organizerEditRequest).subscribe({
      next: (resp: any) => {
        if (resp.meta.code == 200) {
          this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Edit organizer successfully!' });
          setTimeout(() => {
            this._router.navigate(["admin/organizers"]);
          }, 500)
        } else {
          this.blockedDocument = false;
          this.messageService.add({ severity: 'error', summary: 'Failure', detail: 'Edit organizer failure!' });
        }
      },
      error: (err) => {
        this.blockedDocument = false;
      }
    })
  }

}


