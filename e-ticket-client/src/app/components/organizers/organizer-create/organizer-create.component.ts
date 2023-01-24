import { Component, OnInit} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MessageService, PrimeNGConfig } from 'primeng/api';
import { OrganizerCreateRequest } from 'app/model/organizer.model';
import { OrganizerService } from 'app/services/organizer.service';
import { NoWhitespaceValidator } from 'app/utils/no-whitespace.validator';


@Component({
  selector: 'app-organizer-create',
  templateUrl: './organizer-create.component.html',
  styleUrls: ['./organizer-create.component.css'],
  providers: [MessageService]
})
export class OrganizerCreateComponent implements OnInit {
  blockedDocument: boolean = false;
  createOrganizerForm: any = new FormGroup({})

  constructor(
    private fb: FormBuilder, 
    private organizerService: OrganizerService, 
    private _router: Router,
    private primengConfig: PrimeNGConfig,
    private messageService: MessageService) { }

  ngOnInit(): void {
    this.primengConfig.ripple = true;
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
    this.blockedDocument = true;
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
          this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Create organizer successfully!' });
          setTimeout(()=> {
            this._router.navigate(["admin/organizers"]);
          }, 500)
        } else {
          this.blockedDocument = false;
          this.messageService.add({ severity: 'error', summary: 'Failure', detail: 'Create organizer failure!' });
        }
      },
      error: (err) => {
        this.blockedDocument = false;
        this.messageService.add({ severity: 'error', summary: 'Failure', detail: 'Create organizer failure!' });
      }
    });
  }
}
