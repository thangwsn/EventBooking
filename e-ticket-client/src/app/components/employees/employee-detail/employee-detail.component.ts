import { Component, Input, OnInit } from '@angular/core';
import { EmployeeDetail } from 'app/model/account.model';

@Component({
  selector: 'app-employee-detail',
  templateUrl: './employee-detail.component.html',
  styleUrls: ['./employee-detail.component.css']
})
export class EmployeeDetailComponent implements OnInit {
  @Input() employeeDetail!: EmployeeDetail;

  constructor() { }

  ngOnInit(): void {
  }

}
