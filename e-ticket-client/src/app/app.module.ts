
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ButtonModule } from 'primeng/button';
import { MessagesModule } from 'primeng/messages';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import { BlockUIModule } from 'primeng/blockui';
import { CalendarModule } from 'primeng/calendar';
import {InputNumberModule} from 'primeng/inputnumber';
import { DropdownModule } from 'primeng/dropdown';
import {SelectButtonModule} from 'primeng/selectbutton';
import {FileUploadModule} from 'primeng/fileupload';
import {InputTextModule} from 'primeng/inputtext';
import {EditorModule} from 'primeng/editor';
import {ImageModule} from 'primeng/image';
import {GalleriaModule} from 'primeng/galleria';
import {TableModule} from 'primeng/table';
import {DialogModule} from 'primeng/dialog';
import { BadgeModule } from "primeng/badge";
import { AvatarModule } from "primeng/avatar";
import {TooltipModule} from 'primeng/tooltip';
import {ProgressSpinnerModule} from 'primeng/progressspinner';
import {RadioButtonModule} from 'primeng/radiobutton';


import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './layouts/header/header.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { SidebarComponent } from './layouts/sidebar/sidebar.component';
import { DashboardComponent } from './pages/admin/dashboard/dashboard.component';
import { PagesFaqComponent } from './pages/pages-faq/pages-faq.component';
import { PagesContactComponent } from './pages/pages-contact/pages-contact.component';
import { PagesRegisterComponent } from './pages/user/pages-register/pages-register.component';
import { PagesLoginComponent } from './pages/common/pages-login/pages-login.component';
import { PagesError404Component } from './pages/common/pages-error404/pages-error404.component';
import { PagesBlankComponent } from './pages/common/pages-blank/pages-blank.component';
import { OrganizerCreateComponent } from './components/organizers/organizer-create/organizer-create.component';
import { OrganizerListComponent } from './components/organizers/organizer-list/organizer-list.component';
import { OrganizerDetailComponent } from './components/organizers/organizer-detail/organizer-detail.component';
import { EventListComponent } from './components/events/event-list/event-list.component';
import { EventCreateComponent } from './components/events/event-create/event-create.component';
import { EventDetailComponent } from './components/events/event-detail/event-detail.component';
import { UserListComponent } from './components/users/user-list/user-list.component';
import { EmployeeListComponent } from './components/employees/employee-list/employee-list.component';
import { EmployeeCreateComponent } from './components/employees/employee-create/employee-create.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AddTicketDialogComponent } from './components/events/add-ticket-dialog/add-ticket-dialog.component';
import { HeaderUserComponent } from './layouts/header-user/header-user.component';
import { FooterUserComponent } from './layouts/footer-user/footer-user.component';
import { HomeComponent } from './components/home/home.component';
import { EventItemComponent } from './components/events/event-item/event-item.component';
import { EventDetailUserComponent } from './components/events/event-detail-user/event-detail-user.component';
import { TicketCatalogItemComponent } from './components/events/ticket-catalog-item/ticket-catalog-item.component';
import { CheckoutComponent } from './components/bookings/checkout/checkout.component';
import { BookingListUserComponent } from './components/bookings/booking-list-user/booking-list-user.component';
import { BookingDetailUserComponent } from './components/bookings/booking-detail-user/booking-detail-user.component';
import { BookingListComponent } from './components/bookings/booking-list/booking-list.component';
import { BookingDetailComponent } from './components/bookings/booking-detail/booking-detail.component';
import { TicketDetailComponent } from './components/bookings/ticket-detail/ticket-detail.component';
import { TicketDetailAdminComponent } from './components/bookings/ticket-detail-admin/ticket-detail-admin.component';
import { VerifyComponent } from './pages/user/verify/verify.component';
import { UserDetailComponent } from './components/users/user-detail/user-detail.component';
import { UserProfileComponent } from './components/users/user-profile/user-profile.component';
import { EmployeeDetailComponent } from './components/employees/employee-detail/employee-detail.component';
import { OrganizerEditComponent } from './components/organizers/organizer-edit/organizer-edit.component';
import { EmployeeEditComponent } from './components/employees/employee-edit/employee-edit.component';
import { EditTicketDialogComponent } from './components/events/edit-ticket-dialog/edit-ticket-dialog.component';
import { EventEditComponent } from './components/events/event-edit/event-edit.component';
import { EventSearchComponent } from './components/events/event-search/event-search.component';
import { AmIVisibleDirective } from './directives/am-ivisible.directive';
import { FollowedEventListComponent } from './components/events/followed-event-list/followed-event-list.component';
import { HorizontalEventItemComponent } from './components/events/horizontal-event-item/horizontal-event-item.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    SidebarComponent,
    DashboardComponent,
    PagesFaqComponent,
    PagesContactComponent,
    PagesRegisterComponent,
    PagesLoginComponent,
    PagesError404Component,
    PagesBlankComponent,
    OrganizerCreateComponent,
    OrganizerListComponent,
    OrganizerDetailComponent,
    EventListComponent,
    EventCreateComponent,
    EventDetailComponent,
    UserListComponent,
    EmployeeListComponent,
    EmployeeCreateComponent,
    AddTicketDialogComponent,
    HeaderUserComponent,
    FooterUserComponent,
    HomeComponent,
    EventItemComponent,
    EventDetailUserComponent,
    TicketCatalogItemComponent,
    CheckoutComponent,
    BookingListUserComponent,
    BookingDetailUserComponent,
    BookingListComponent,
    BookingDetailComponent,
    TicketDetailComponent,
    TicketDetailAdminComponent,
    VerifyComponent,
    UserDetailComponent,
    UserProfileComponent,
    EmployeeDetailComponent,
    OrganizerEditComponent,
    EmployeeEditComponent,
    EditTicketDialogComponent,
    EventEditComponent,
    EventSearchComponent,
    AmIVisibleDirective,
    FollowedEventListComponent,
    HorizontalEventItemComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    ConfirmDialogModule,
    ButtonModule,
    MessagesModule,
    ToastModule,
    BlockUIModule,
    CalendarModule,
    InputNumberModule,
    DropdownModule,
    SelectButtonModule,
    FileUploadModule,
    InputTextModule,
    EditorModule,
    ImageModule,
    GalleriaModule,
    TableModule,
    DialogModule,
    BadgeModule,
    AvatarModule,
    TooltipModule,
    ProgressSpinnerModule,
    RadioButtonModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
