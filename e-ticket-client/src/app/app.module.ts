
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ToastrModule } from 'ngx-toastr';
import { ButtonModule } from 'primeng/button';
import { MessagesModule } from 'primeng/messages';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import { BlockUIModule } from 'primeng/blockui';


import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './layouts/header/header.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { SidebarComponent } from './layouts/sidebar/sidebar.component';
import { DashboardComponent } from './pages/admin/dashboard/dashboard.component';
import { AlertsComponent } from './components/alerts/alerts.component';
import { AccordionComponent } from './components/accordion/accordion.component';
import { BadgesComponent } from './components/badges/badges.component';
import { BreadcrumbsComponent } from './components/breadcrumbs/breadcrumbs.component';
import { ButtonsComponent } from './components/buttons/buttons.component';
import { CardsComponent } from './components/cards/cards.component';
import { CarouselComponent } from './components/carousel/carousel.component';
import { ListGroupComponent } from './components/list-group/list-group.component';
import { ModalComponent } from './components/modal/modal.component';
import { TabsComponent } from './components/tabs/tabs.component';
import { PaginationComponent } from './components/pagination/pagination.component';
import { ProgressComponent } from './components/progress/progress.component';
import { SpinnersComponent } from './components/spinners/spinners.component';
import { TooltipsComponent } from './components/tooltips/tooltips.component';
import { FormsElementsComponent } from './components/forms-elements/forms-elements.component';
import { FormsLayoutsComponent } from './components/forms-layouts/forms-layouts.component';
import { FormsEditorsComponent } from './components/forms-editors/forms-editors.component';
import { TablesGeneralComponent } from './components/tables-general/tables-general.component';
import { TablesDataComponent } from './components/tables-data/tables-data.component';
import { ChartsChartjsComponent } from './components/charts-chartjs/charts-chartjs.component';
import { ChartsApexchartsComponent } from './components/charts-apexcharts/charts-apexcharts.component';
import { IconsBootstrapComponent } from './components/icons-bootstrap/icons-bootstrap.component';
import { IconsRemixComponent } from './components/icons-remix/icons-remix.component';
import { IconsBoxiconsComponent } from './components/icons-boxicons/icons-boxicons.component';
import { UsersProfileComponent } from './pages/user/users-profile/users-profile.component';
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

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    SidebarComponent,
    DashboardComponent,
    AlertsComponent,
    AccordionComponent,
    BadgesComponent,
    BreadcrumbsComponent,
    ButtonsComponent,
    CardsComponent,
    CarouselComponent,
    ListGroupComponent,
    ModalComponent,
    TabsComponent,
    PaginationComponent,
    ProgressComponent,
    SpinnersComponent,
    TooltipsComponent,
    FormsElementsComponent,
    FormsLayoutsComponent,
    FormsEditorsComponent,
    TablesGeneralComponent,
    TablesDataComponent,
    ChartsChartjsComponent,
    ChartsApexchartsComponent,
    IconsBootstrapComponent,
    IconsRemixComponent,
    IconsBoxiconsComponent,
    UsersProfileComponent,
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
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
    ConfirmDialogModule,
    ButtonModule,
    MessagesModule,
    ToastModule,
    BlockUIModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
