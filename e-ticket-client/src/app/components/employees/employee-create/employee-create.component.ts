import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Address, EmployeeCreate } from 'app/model/account.model';
import { EmployeeService } from 'app/services/employee.service';
import { NoWhitespaceValidator } from 'app/utils/no-whitespace.validator';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-employee-create',
  templateUrl: './employee-create.component.html',
  styleUrls: ['./employee-create.component.css']
})
export class EmployeeCreateComponent implements OnInit {
  createEmployeeForm: any = new FormGroup({});
  constructor(
    private fb: FormBuilder,
    private employeeService: EmployeeService
  ) { }

  ngOnInit(): void {
    this.createEmployeeForm = this.fb.group({
      username: ['', Validators.compose([Validators.required, NoWhitespaceValidator()])],
      email: ['', Validators.compose([Validators.required, NoWhitespaceValidator()])],
      fullName: [null],
      mobile: [null, Validators.compose([Validators.pattern("\\d{10}")])],
      dateOfBirth: [null],
      gender: [null],
      street: [''],
      ward: [''],
      district: [''],
      city: [''],
      joinDate: [null],
      position: [null],
      role: [null, Validators.required]
    })
  }

  onSubmit(): Observable<any> {
    let employeeCreateRequest = new EmployeeCreate(this.createEmployeeForm.get('username').value, this.createEmployeeForm.get('email').value,
      this.createEmployeeForm.get('mobile').value, this.createEmployeeForm.get('fullName').value,
      this.createEmployeeForm.get('gender').value, this.createEmployeeForm.get('dateOfBirth').value,
      '', new Address(this.createEmployeeForm.get('street').value, this.createEmployeeForm.get('ward').value,
        this.createEmployeeForm.get('district').value, this.createEmployeeForm.get('city').value),
      this.createEmployeeForm.get('position').value, this.createEmployeeForm.get('joinDate').value,
      this.createEmployeeForm.get('role').value);
    return this.employeeService.createEmployee(employeeCreateRequest);
  }

}
