import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './pages/admin/dashboard/dashboard.component';
import { PagesBlankComponent } from './pages/common/pages-blank/pages-blank.component';
import { PagesContactComponent } from './pages/pages-contact/pages-contact.component';
import { PagesError404Component } from './pages/common/pages-error404/pages-error404.component';
import { PagesFaqComponent } from './pages/pages-faq/pages-faq.component';
import { PagesLoginComponent } from './pages/common/pages-login/pages-login.component';
import { PagesRegisterComponent } from './pages/user/pages-register/pages-register.component';
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
import { UserProfileComponent } from './components/users/user-profile/user-profile.component';

const routes: Routes = [
  { path: '', component: HomeComponent},
  { path: 'dashboard', component: DashboardComponent },
  { path: 'pages-blank', component: PagesBlankComponent },
  { path: 'pages-contact', component: PagesContactComponent },
  { path: 'pages-error404', component: PagesError404Component },
  { path: 'pages-faq', component: PagesFaqComponent },
  
  { path: 'login', component: PagesLoginComponent },
  { path: 'register', component: PagesRegisterComponent },
  { path: 'verify-register', component: VerifyComponent},
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
  { path: 'admin/bookings/:bookingId', component: BookingDetailComponent},
  { path: 'admin/users', component: UserListComponent},
  { path: 'admin/employees', component: EmployeeListComponent},
  { path: 'profile', component: UserProfileComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
