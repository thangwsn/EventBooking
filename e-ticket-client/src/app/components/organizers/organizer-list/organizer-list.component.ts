import { Component, OnInit } from '@angular/core';
import { Notifications } from '@mobiscroll/angular';
import { ToastrService } from 'ngx-toastr';
import { Observable, Subject } from 'rxjs';
import { OrganizerGet } from 'src/app/model/organizer.model';
import { OrganizerService } from 'src/app/services/organizer.service';

@Component({
  selector: 'app-organizer-list',
  templateUrl: './organizer-list.component.html',
  styleUrls: ['./organizer-list.component.css']
})
export class OrganizerListComponent implements OnInit {

  organizerList$: Observable<OrganizerGet[]> = new Observable<OrganizerGet[]>();

  constructor(private organizerService: OrganizerService, private notify: Notifications, private toastr: ToastrService) { }

  ngOnInit(): void {
    this.organizerService.fetchOrganizers();
    this.organizerList$ = this.organizerService.organizerList$;
  }

  ngOnDestroy(): void {

  }

  openRemoveConfirm(organizer: OrganizerGet) {
    // Using callback function
    this.notify.confirm({
      title: 'Remove Confirm',
      message: 'Are your sure remove this item?',
      okText: 'OK',
      cancelText: 'Cancel'
    }).then((result) => {
      if (result == true) {
        this.handleRemove(organizer.id)
      } 
    })
    .catch((err) => {
      console.log(err);
    });
  }

  private handleRemove(organizerId: number) {
    this.organizerService.removeOrganizer(organizerId).subscribe({
      next: (res: any) => {
        this.organizerService.fetchOrganizers();
        if (res.meta.code == 200) {
          this.toastr.success("Remove successfully!", "", {timeOut: 1000, newestOnTop: true, tapToDismiss: true});
        }
      }
    });
  }

}
