import { Component, OnInit } from '@angular/core';
import { HttpService } from '../../services/http.service';

@Component({
  selector: 'app-booking-details',
  templateUrl: './booking-details.component.html',
  styleUrls: ['./booking-details.component.scss']
})
export class BookingDetailsComponent implements OnInit {

  eventList: any[] = [];
  showError = false;
  errorMessage: any = '';
  showMessage = false;
  responseMessage: any = '';

  constructor(private http: HttpService) {}

  ngOnInit(): void {
    this.loadMyBookings();
  }

  loadMyBookings() {
    this.showError = false;
    this.showMessage = false;
    this.errorMessage = '';
    this.responseMessage = '';
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
}
