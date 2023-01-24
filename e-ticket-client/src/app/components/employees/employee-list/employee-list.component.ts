import { Component, OnInit, ViewChild } from '@angular/core';
import { ConfirmationService, MessageService, PrimeNGConfig } from 'primeng/api';
import { Table } from 'primeng/table';
import { Observable } from 'rxjs';

import { Employee, EmployeeDetail, EmployeeEdit } from 'app/model/account.model';
import { EmployeeService } from 'app/services/employee.service';
import { EmployeeEditComponent } from '../employee-edit/employee-edit.component';
import { EmployeeCreateComponent } from '../employee-create/employee-create.component';


@Component({
  selector: 'app-employee-list',
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.css'],
  providers: [ConfirmationService, MessageService]
})
export class EmployeeListComponent implements OnInit {
  @ViewChild('dt') dt!: Table;
  @ViewChild('employee_edit', { static: false }) employeeEditComponent!: EmployeeEditComponent;
  @ViewChild('employee_create', {static: false}) employeeCreateComponent!: EmployeeCreateComponent; 
  employeeDetailDialog: boolean = false;
  employeeEditDialog: boolean = false;
  employeeCreateDialog: boolean = false;

  employeeList$: Observable<Employee[]> = new Observable<Employee[]>();
  employeeDetail!: EmployeeDetail;
  employeeEdit!: EmployeeEdit;

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
        this.messageService.add({ severity: 'info', summary: 'Success', detail: 'Cancel removing!' });
      }
    });
  }

  private handleRemove(id: number) {
    this.employeeService.removeEmployee(id).subscribe({
      next: (res: any) => {
        this.employeeService.getAllEmployee();
        if (res.meta.code == 200) {
          this.employeeService.getAllEmployee();
          this.messageService.add({ severity: 'success', summary: 'Cancel', detail: 'Remove successfully!' });
        }
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error!' });
      }
    });
  }

  detailEmployee(employeeId: number) {
    this.employeeService.getEmployeeDetail(employeeId).subscribe({
      next: (resp: any) => {
        if (resp.meta.code == 200) {
          this.employeeDetail = resp.data;
          this.employeeDetailDialog = true;
        }
        else {

        }

      },
      error: () => {

      }
    })
  }

  openEditEmployeeDialog(employeeId: number) {
    this.employeeService.getEmployeeEdit(employeeId).subscribe({
      next: (resp: any) => {
        if (resp.meta.code == 200) {
          this.employeeEdit = resp.data;
          this.employeeEditDialog = true;
        }
        else {

        }

      },
      error: () => {

      }
    })
  }

  closeDetailDialog() {
    this.employeeDetailDialog = false;
  }

  closeEditDialog() {
    this.employeeEditDialog = false;
  }

  submitUpdateEmployee() {
    if (this.employeeEditComponent.updateEmployeeForm.valid) {
      this.employeeEditComponent.onSubmit().subscribe({
        next: (resp: any) => {
          if (resp.meta.code == 200) {
            this.closeEditDialog();
            this.employeeService.getAllEmployee();
            this.messageService.add({ severity: 'success', summary: 'Update', detail: 'Update successfully!' });
          }
        },
        error: (err) => {

        }
      });
    }
  }

  openEmployeeCreateDialog() {
    this.employeeCreateDialog = true;
  }

  closeCreateDialog() {
    this.employeeCreateDialog = false;
  }

  submitCreateEmployee() {
    if (this.employeeCreateComponent.createEmployeeForm.valid) {
      this.employeeCreateComponent.onSubmit().subscribe({
        next: (resp: any) => {
          if (resp.meta.code == 200) {
            this.closeCreateDialog();
            this.employeeService.getAllEmployee();
            this.messageService.add({ severity: 'success', summary: 'Update', detail: 'Create successfully!' });
          }
        },
        error: (err) => {

        }
      })
    }
  }
}
