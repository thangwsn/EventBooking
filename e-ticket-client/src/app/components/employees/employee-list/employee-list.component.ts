import { Component, OnInit, ViewChild } from '@angular/core';
import { ConfirmationService, Message, MessageService, PrimeNGConfig } from 'primeng/api';
import { Table } from 'primeng/table';
import { Observable } from 'rxjs';

import { Employee, EmployeeDetail } from 'app/model/account.model';
import { EmployeeService } from 'app/services/employee.service';


@Component({
  selector: 'app-employee-list',
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.css'],
  providers: [ConfirmationService, MessageService]
})
export class EmployeeListComponent implements OnInit {
  @ViewChild('dt') dt!: Table;
  employeeDialog: boolean = false;

  employeeList$:  Observable<Employee[]> = new Observable<Employee[]>();
  employeeDetail!: EmployeeDetail;

  constructor(
    private employeeService: EmployeeService,
    private confirmationService: ConfirmationService,
    private primengConfig: PrimeNGConfig,
    private messageService: MessageService
  ) { }

  ngOnInit(): void {
    this.primengConfig.ripple = true;
    this.employeeService.getAllEmployee();
    this.employeeList$ = this.employeeService.employeeList$
  }

  applyFilterGlobal($event: any, stringVal: any) {
    this.dt.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
  }

  openRemoveConfirm(employee: Employee) {
    this.confirmationService.confirm({
      message: 'Are you sure that you want to delete this item?',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.handleRemove(employee.id)
      },
      reject: () => {
        this.messageService.add({severity:'info', summary: 'Success', detail: 'Cancel removing!'});
      }
    });
  }

  private handleRemove(id: number) {
    this.employeeService.removeEmployee(id).subscribe({
      next: (res: any) => {
        this.employeeService.getAllEmployee();
        if (res.meta.code == 200) {
          this.messageService.add({severity:'success', summary: 'Cancel', detail: 'Remove successfully!'});
        }
      },
      error: () => {
        this.messageService.add({severity:'error', summary: 'Error', detail: 'Error!'});
      }
    });
  }

  openEmployeeDetail(employeeId: number) {
    this.employeeService.getEmployeeDetail(employeeId).subscribe({
      next: (resp: any) => {
        if (resp.meta.code == 200) {
          this.employeeDetail = resp.data;
          this.employeeDialog = true;
        }
        else {

        }
        
      },
      error: () => {
        
      }
    })
  }

}
