import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './pages/admin/dashboard/dashboard.component';
import { AlertsComponent } from './components/alerts/alerts.component';
import { AccordionComponent } from './components/accordion/accordion.component';
import { BadgesComponent } from './components/badges/badges.component';
import { BreadcrumbsComponent } from './components/breadcrumbs/breadcrumbs.component';
import { ButtonsComponent } from './components/buttons/buttons.component';
import { CardsComponent } from './components/cards/cards.component';
import { CarouselComponent } from './components/carousel/carousel.component';
import { ChartsApexchartsComponent } from './components/charts-apexcharts/charts-apexcharts.component';
import { ChartsChartjsComponent } from './components/charts-chartjs/charts-chartjs.component';
import { FormsEditorsComponent } from './components/forms-editors/forms-editors.component';
import { FormsElementsComponent } from './components/forms-elements/forms-elements.component';
import { FormsLayoutsComponent } from './components/forms-layouts/forms-layouts.component';
import { IconsBootstrapComponent } from './components/icons-bootstrap/icons-bootstrap.component';
import { IconsBoxiconsComponent } from './components/icons-boxicons/icons-boxicons.component';
import { IconsRemixComponent } from './components/icons-remix/icons-remix.component';
import { ListGroupComponent } from './components/list-group/list-group.component';
import { ModalComponent } from './components/modal/modal.component';
import { PaginationComponent } from './components/pagination/pagination.component';
import { ProgressComponent } from './components/progress/progress.component';
import { SpinnersComponent } from './components/spinners/spinners.component';
import { TablesDataComponent } from './components/tables-data/tables-data.component';
import { TablesGeneralComponent } from './components/tables-general/tables-general.component';
import { TabsComponent } from './components/tabs/tabs.component';
import { TooltipsComponent } from './components/tooltips/tooltips.component';
import { PagesBlankComponent } from './pages/common/pages-blank/pages-blank.component';
import { PagesContactComponent } from './pages/pages-contact/pages-contact.component';
import { PagesError404Component } from './pages/common/pages-error404/pages-error404.component';
import { PagesFaqComponent } from './pages/pages-faq/pages-faq.component';
import { PagesLoginComponent } from './pages/common/pages-login/pages-login.component';
import { PagesRegisterComponent } from './pages/user/pages-register/pages-register.component';
import { UsersProfileComponent } from './pages/user/users-profile/users-profile.component';
import { OrganizerCreateComponent } from './components/organizers/organizer-create/organizer-create.component';
import { OrganizerDetailComponent } from './components/organizers/organizer-detail/organizer-detail.component';
import { EventListComponent } from './components/events/event-list/event-list.component';
import { UserListComponent } from './components/users/user-list/user-list.component';
import { EmployeeListComponent } from './components/employees/employee-list/employee-list.component';
import { EventCreateComponent } from './components/events/event-create/event-create.component';
import { EventDetailComponent } from './components/events/event-detail/event-detail.component';
import { OrganizerListComponent } from './components/organizers/organizer-list/organizer-list.component';
import { HomeComponent } from './components/home/home.component';
import { EventDetailUserComponent } from './components/events/event-detail-user/event-detail-user.component';
import { CheckoutComponent } from './components/bookings/checkout/checkout.component';
import { BookingListUserComponent } from './components/bookings/booking-list-user/booking-list-user.component';
import { BookingDetailUserComponent } from './components/bookings/booking-detail-user/booking-detail-user.component';
import { BookingListComponent } from './components/bookings/booking-list/booking-list.component';
import { BookingDetailComponent } from './components/bookings/booking-detail/booking-detail.component';
import { VerifyComponent } from './pages/user/verify/verify.component';

const routes: Routes = [
  { path: '', component: HomeComponent},
  { path: 'dashboard', component: DashboardComponent },
  { path: 'alerts', component: AlertsComponent },
  { path: 'accordion', component: AccordionComponent },
  { path: 'badges', component: BadgesComponent },
  { path: 'breadcrumbs', component: BreadcrumbsComponent },
  { path: 'buttons', component: ButtonsComponent },
  { path: 'cards', component: CardsComponent },
  { path: 'carousel', component: CarouselComponent },
  { path: 'charts-apexcharts', component: ChartsApexchartsComponent },
  { path: 'charts-chartjs', component: ChartsChartjsComponent },
  { path: 'form-editors', component: FormsEditorsComponent },
  { path: 'form-elements', component: FormsElementsComponent },
  { path: 'form-layouts', component: FormsLayoutsComponent },
  { path: 'icons-bootstrap', component: IconsBootstrapComponent },
  { path: 'icons-boxicons', component: IconsBoxiconsComponent },
  { path: 'icons-remix', component: IconsRemixComponent },
  { path: 'list-group', component: ListGroupComponent },
  { path: 'modal', component: ModalComponent },
  { path: 'pagination', component: PaginationComponent },
  { path: 'progress', component: ProgressComponent },
  { path: 'spinners', component: SpinnersComponent },
  { path: 'tables-data', component: TablesDataComponent },
  { path: 'tables-general', component: TablesGeneralComponent },
  { path: 'tabs', component: TabsComponent },
  { path: 'tooltips', component: TooltipsComponent },
  { path: 'pages-blank', component: PagesBlankComponent },
  { path: 'pages-contact', component: PagesContactComponent },
  { path: 'pages-error404', component: PagesError404Component },
  { path: 'pages-faq', component: PagesFaqComponent },
  
  { path: 'login', component: PagesLoginComponent },
  { path: 'register', component: PagesRegisterComponent },
  { path: 'verify-register', component: VerifyComponent},
  { path: 'user-profile', component: UsersProfileComponent },
  { path: 'admin/organizers', component: OrganizerListComponent},
  { path: 'admin/organizers/create', component: OrganizerCreateComponent},
  { path: 'admin/organizers/:organizerId', component: OrganizerDetailComponent},
  { path: 'admin/events', component: EventListComponent},
  { path: 'admin/events/create', component: EventCreateComponent},
  { path: 'admin/events/:eventId', component: EventDetailComponent},
  { path: 'admin/users', component: UserListComponent},
  { path: 'admin/employees', component: EmployeeListComponent},
  { path: 'events/:eventId', component: EventDetailUserComponent},
  { path: 'checkout', component: CheckoutComponent},
  { path: 'booking', component: BookingListUserComponent},
  { path: 'booking/:bookingId', component: BookingDetailUserComponent},
  { path: 'admin/bookings', component: BookingListComponent}, 
  { path: 'admin/bookings/:bookingId', component: BookingDetailComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
