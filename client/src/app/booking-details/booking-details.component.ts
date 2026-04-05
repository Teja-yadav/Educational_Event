import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpService } from '../../services/http.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-booking-details',
  templateUrl: './booking-details.component.html',
  styleUrls: ['./booking-details.component.scss']
})
export class BookingDetailsComponent implements OnInit {

  itemForm!: FormGroup;
  showError: boolean = false;
  errorMessage: any = '';
  showMessage: boolean = false;
  responseMessage: any = '';
  eventList: any[] = [];

  private studentIdPattern = /^LTM\d{2}$/;

  constructor(
    private fb: FormBuilder,
    private http: HttpService,
    private auth: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.itemForm = this.fb.group({
      studentId: ['', [Validators.required, Validators.pattern(this.studentIdPattern)]]
    });
  }

  private formatDate(value: any): string | null {
    if (!value) return null;

    const d = new Date(value);
    if (isNaN(d.getTime())) return String(value);

    const months = ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];
    return `${months[d.getMonth()]} ${d.getDate()}, ${d.getFullYear()}`;
  }

  searchEvent() {
    this.showError = false;
    this.errorMessage = '';
    this.showMessage = false;
    this.responseMessage = '';
    this.eventList = [];

    this.itemForm.markAllAsTouched();

    const studentIdRaw = (this.itemForm.value.studentId || '').toString().trim();
    this.itemForm.patchValue({ studentId: studentIdRaw }, { emitEvent: false });

    if (this.itemForm.invalid) {
      this.showError = true;
      const ctrl = this.itemForm.get('studentId');

      if (ctrl?.errors?.['required']) {
        this.errorMessage = 'Student ID is required';
      } else if (ctrl?.errors?.['pattern']) {
        this.errorMessage = 'Student ID must be like LTM01, LTM09, LTM11 (starts with LTM + 2 digits)';
      } else {
        this.errorMessage = 'Invalid Student ID';
      }
      return;
    }

    this.http.getBookingDetails(studentIdRaw).subscribe({
      next: (res) => {
        const data = Array.isArray(res) ? res : [];

        this.eventList = data.map((x: any) => {
          const rawDate = x.registeredOn ?? x.createdAt ?? x.registrationDate ?? null;

          return {
            ...x,
            studentId: x.studentId ?? x.student?.studentId ?? studentIdRaw,
            eventName: x.event?.name ?? x.eventName ?? x.event?.eventName ?? null,

            // ✅ add venue for UI
            venue: x.event?.venue ?? x.venue ?? null,

            registeredOnDisplay: this.formatDate(rawDate)
          };
        });

        if (this.eventList.length > 0) {
          this.showMessage = true;
          this.responseMessage = 'Records fetched successfully';
        } else {
          this.showError = true;
          this.errorMessage = 'No records found for this Student ID';
        }
      },
      error: () => {
        this.showError = true;
        this.errorMessage = 'Unable to fetch details';
        this.eventList = [];
      }
    });
  }
}
