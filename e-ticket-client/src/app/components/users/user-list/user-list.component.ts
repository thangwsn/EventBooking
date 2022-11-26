import { Component, OnInit, ViewChild } from '@angular/core';
import { User, UserDetail } from 'app/model/account.model';
import { UserService } from 'app/services/user.service';
import { Table } from 'primeng/table';
import { Observable } from 'rxjs';
import { UserDetailComponent } from '../user-detail/user-detail.component';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  @ViewChild('dt') dt!: Table;
  userDetailDialog: boolean = false;
  
  userList$:  Observable<User[]> = new Observable<User[]>();
  userDetail!: UserDetail;

  constructor(
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.userService.getAllUser();
    this.userList$ = this.userService.userList$;
  }

  applyFilterGlobal($event: any, stringVal: any) {
    this.dt.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
  }

  detailUser(userId: number) {
    this.userService.getUserDetail(userId).subscribe({
      next: (resp: any) => {
        if (resp.meta.code == 200) {
          this.userDetail = resp.data;
          this.userDetailDialog = true;
        }
        else {

        }
        
      },
      error: () => {

      }
    })
  }

  closeDialog() {
    this.userDetailDialog = false;
  }

}
