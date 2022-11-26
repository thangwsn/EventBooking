import { Component, Input, OnInit } from '@angular/core';
import { UserDetail } from 'app/model/account.model';

@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.css']
})
export class UserDetailComponent implements OnInit {
  @Input() userDetail!: UserDetail;

  constructor() { }

  ngOnInit(): void {
  }

}
