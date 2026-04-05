import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { CreateEventComponent } from './create-event/create-event.component';
import { AddResourceComponent } from './add-resource/add-resource.component';
import { ResourceAllocateComponent } from './resource-allocate/resource-allocate.component';
import { ViewEventsComponent } from './view-events/view-events.component';
import { BookingDetailsComponent } from './booking-details/booking-details.component';
import { RegisterForEventComponent } from './register-for-event/register-for-event.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'forgot-password', component: ForgotPasswordComponent},
  { path: 'registration', component: RegistrationComponent },

  { path: 'dashboard', component: DashboardComponent },
  { path: 'create-event', component: CreateEventComponent },
  { path: 'add-resource', component: AddResourceComponent },
  { path: 'resource-allocate', component: ResourceAllocateComponent },
  { path: 'view-events', component: ViewEventsComponent },
  { path: 'booking-details', component: BookingDetailsComponent },
  { path: 'register-for-event', component: RegisterForEventComponent },

  { path: '**', redirectTo: '' }                
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}