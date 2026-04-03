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
  formModel: any = {};
  showError: boolean = false;
  errorMessage: any = '';
  eventObj: any = {};
  assignModel: any = {};
  showMessage: boolean = false;
  responseMessage: any = '';
  isUpdate: boolean = false;
  eventList: any = [];

  private studentIdPattern = /^LTM\d{2}$/;

  constructor(
    private fb: FormBuilder,
    private http: HttpService,
    private auth: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.itemForm = this.fb.group({
      studentId: [
        '',
        [
          Validators.required,
          Validators.pattern(this.studentIdPattern)
        ]
      ]
    });
  }

  searchEvent() {
    this.showError = false;
    this.errorMessage = '';
    this.showMessage = false;
    this.responseMessage = '';
    this.eventList = [];

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

    this.formModel = this.itemForm.value;

    this.http.getBookingDetails(this.formModel.studentId).subscribe({
      next: (res) => {
        const data = Array.isArray(res) ? res : [];
        this.eventList = data;

        if (data.length > 0) {
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