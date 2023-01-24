import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Injectable } from "@angular/core";
import { Employee, EmployeeCreate, EmployeeEdit } from "../model/account.model";
import { TokenStorageService } from "./token-storage.service";
import { environment } from 'environments/environment';

const BASE_API = environment.host + "/api";

@Injectable({
    providedIn: 'root'
})

export class EmployeeService {
    employeeList: Employee[] = [];
    private displayItemSubject: BehaviorSubject<Employee[]> = new BehaviorSubject<Employee[]>([]);
    employeeList$: Observable<Employee[]> = this.displayItemSubject.asObservable();

    constructor(
        private http: HttpClient,
        private tokenService: TokenStorageService
    ) { }

    getAllEmployee() {
        this.http.get(`${BASE_API}/employees`, this.getHeader()).subscribe({
            next: (resp: any) => {
                if (resp.meta.code == 200) {
                    this.employeeList = resp.data.employeeList;
                    this.updateEmployeeListData();
                }
            }
        })
    }

    getEmployeeDetail(employeeId: number): Observable<any> {
        return this.http.get(`${BASE_API}/employees/${employeeId}`, this.getHeader());
    }

    getEmployeeEdit(employeeId: number): Observable<any> {
        return this.http.get(`${BASE_API}/employees/edit/${employeeId}`, this.getHeader());
    }

    updateEmployee(employeeEditRequest: EmployeeEdit): Observable<any> {
        return this.http.put(`${BASE_API}/employees/edit/${employeeEditRequest.id}`, employeeEditRequest, this.getHeader());
    }

    removeEmployee(id: number): Observable<any> {
        return this.http.delete(`${BASE_API}/employees/${id}`, this.getHeader());
    }

    createEmployee(employeeCreateRequest: EmployeeCreate): Observable<any> {
        return this.http.post(`${BASE_API}/employees`, employeeCreateRequest, this.getHeader());
    }

    private updateEmployeeListData() {
        this.displayItemSubject.next(this.employeeList);
    }

    private getHeader() {
        return {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*',
                'Authorization': 'Bearer ' + this.tokenService.getToken()
            })
        }
    }

}