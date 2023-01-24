import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Address, EmployeeEdit } from 'app/model/account.model';
import { EmployeeService } from 'app/services/employee.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-employee-edit',
  templateUrl: './employee-edit.component.html',
  styleUrls: ['./employee-edit.component.css']
})
export class EmployeeEditComponent implements OnInit {
  @Input() employeeEdit!: EmployeeEdit;
  updateEmployeeForm: any = new FormGroup({})
  constructor(
    private fb: FormBuilder,
    private employeeService: EmployeeService
  ) { }

  ngOnInit(): void {
    this.updateEmployeeForm = this.fb.group({
      fullName: [''],
      mobile: ['', Validators.compose([Validators.pattern("\\d{10}")])],
      dateOfBirth: [''],
      gender: [''],
      street: [''],
      ward: [''],
      district: [''],
      city: [''],
      joinDate: [''],
      position: [''],
      role: ['']
    })
    if (!this.employeeEdit.address) {
      this.employeeEdit.address = new Address('', '', '', '')
    }
    this.updateEmployeeForm.patchValue({
      fullName: this.employeeEdit.fullName,
      mobile: this.employeeEdit.mobile,
      gender: this.employeeEdit.gender,
      position: this.employeeEdit.position,
      dateOfBirth: new Date(this.employeeEdit.dateOfBirth),
      joinDate: new Date(this.employeeEdit.joinDate),
      street: this.employeeEdit.address.street,
      ward: this.employeeEdit.address.ward,
      district: this.employeeEdit.address.district,
      city: this.employeeEdit.address.city,
      role: this.employeeEdit.role
    })

  }

  onSubmit(): Observable<any> {
    let employeeEditRequest = new EmployeeEdit(
      this.employeeEdit.id,
      this.employeeEdit.username,
      this.employeeEdit.email,
      this.updateEmployeeForm.get('mobile').value,
      this.updateEmployeeForm.get('fullName').value,
      this.employeeEdit.employeeCode,
      this.updateEmployeeForm.get('gender').value,
      this.updateEmployeeForm.get('dateOfBirth').value,
      '',
      new Address(this.updateEmployeeForm.get('street').value, this.updateEmployeeForm.get('ward').value,
        this.updateEmployeeForm.get('district').value, this.updateEmployeeForm.get('city').value),
      this.updateEmployeeForm.get('position').value,
      this.updateEmployeeForm.get('joinDate').value,
      this.updateEmployeeForm.get('role').value
    )
    return this.employeeService.updateEmployee(employeeEditRequest);
  }

}
