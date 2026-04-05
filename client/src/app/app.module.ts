import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RegistrationComponent } from './registration/registration.component';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { HttpService } from '../services/http.service';
import { DashboardComponent } from './dashboard/dashboard.component';
import { CreateEventComponent } from './create-event/create-event.component';
import { AddResourceComponent } from './add-resource/add-resource.component';
import { ResourceAllocateComponent } from './resource-allocate/resource-allocate.component';
import { ViewEventsComponent } from './view-events/view-events.component';
import { BookingDetailsComponent } from './booking-details/booking-details.component';

import { RegisterForEventComponent } from './register-for-event/register-for-event.component';
import { NavbarComponent } from './navbar/navbar.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegistrationComponent,
    DashboardComponent,
    CreateEventComponent,
    AddResourceComponent,
    ResourceAllocateComponent,
    ViewEventsComponent,
    BookingDetailsComponent,
    NavbarComponent,
    RegisterForEventComponent,
    ForgotPasswordComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [HttpService, HttpClientModule],
  bootstrap: [AppComponent]
})
export class AppModule { }
