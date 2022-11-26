import { Component, OnInit, ViewChild } from '@angular/core';
import { ConfirmationService, Message, MessageService, PrimeNGConfig } from 'primeng/api';
import { Observable } from 'rxjs';
import { OrganizerGet } from 'app/model/organizer.model';
import { OrganizerService } from 'app/services/organizer.service';
import { Table } from 'primeng/table';

@Component({
  selector: 'app-organizer-list',
  templateUrl: './organizer-list.component.html',
  styleUrls: ['./organizer-list.component.css'],
  styles: [`
  :host ::ng-deep button {
      margin-right: .25em;
  }`],
  providers: [ConfirmationService, MessageService]
})
export class OrganizerListComponent implements OnInit {
  @ViewChild('dt') dt!: Table;
  organizerList$: Observable<OrganizerGet[]> = new Observable<OrganizerGet[]>();
  msgs: Message[] = [];

  constructor(private organizerService: OrganizerService,
    private confirmationService: ConfirmationService,
    private primengConfig: PrimeNGConfig,
    private messageService: MessageService
    ) { }

  ngOnInit(): void {
    this.primengConfig.ripple = true;
    this.organizerService.fetchOrganizers();
    this.organizerList$ = this.organizerService.organizerList$;
  }

  applyFilterGlobal($event: any, stringVal: any) {
    this.dt.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
  }

  openRemoveConfirm(organizer: OrganizerGet) {
    this.confirmationService.confirm({
      message: 'Are you sure that you want to delete this item?',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.handleRemove(organizer.id)
      },
      reject: () => {
        this.messageService.add({severity:'info', summary: 'Success', detail: 'Cancel removing!'});
      }
    });
  }

  private handleRemove(organizerId: number) {
    this.organizerService.removeOrganizer(organizerId).subscribe({
      next: (res: any) => {
        this.organizerService.fetchOrganizers();
        if (res.meta.code == 200) {
          this.messageService.add({severity:'success', summary: 'Cancel', detail: 'Remove successfully!'});
        }
      }
    });
  }

}
