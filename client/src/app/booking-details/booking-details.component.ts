import { Component, OnInit } from '@angular/core';
import { HttpService } from '../../services/http.service';

@Component({
  selector: 'app-booking-details',
  templateUrl: './booking-details.component.html',
  styleUrls: ['./booking-details.component.scss']
})
export class BookingDetailsComponent implements OnInit {

  role = '';
  eventList: any[] = [];

  showError = false;
  errorMessage: any = '';
  showMessage = false;
  responseMessage: any = '';

  constructor(private http: HttpService) {}

  ngOnInit(): void {
    this.role = (localStorage.getItem('role') || '').toUpperCase();

    if (this.role === 'INSTITUTION') {
      this.loadInstitutionBookings();
    } else {
      this.loadMyBookings();
    }
  }

  loadMyBookings() {
    this.resetMsgs();
    this.eventList = [];

    this.http.getMyBookings().subscribe({
      next: (res) => {
        this.eventList = Array.isArray(res) ? res : [];
        if (this.eventList.length === 0) {
          this.showMessage = true;
          this.responseMessage = 'You have not registered for any events yet.';
        }
      },
      error: (err) => {
        this.eventList = [];
        this.showError = true;
        this.errorMessage =
          err?.error?.message || err?.error || 'Failed to load bookings';
      }
    });
  }

  loadInstitutionBookings() {
    this.resetMsgs();
    this.eventList = [];

    this.http.getInstitutionRegistrations().subscribe({
      next: (res) => {
        this.eventList = Array.isArray(res) ? res : [];
        if (this.eventList.length === 0) {
          this.showMessage = true;
          this.responseMessage = 'No registrations found for your events.';
        }
      },
      error: (err) => {
        this.eventList = [];
        this.showError = true;
        this.errorMessage =
          err?.error?.message || err?.error || 'Failed to load registrations';
      }
    });
  }

  private resetMsgs() {
    this.showError = false;
    this.errorMessage = '';
    this.showMessage = false;
    this.responseMessage = '';
  }
}