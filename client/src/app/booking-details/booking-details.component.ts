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

  constructor(
    private fb: FormBuilder,
    private http: HttpService,
    private auth: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.itemForm = this.fb.group({
      studentId: ['', Validators.required]
    });
  }

  searchEvent() {
    if (this.itemForm.invalid) {
      this.showError = true;
      this.errorMessage = 'Student ID is required';
      return;
    }

    this.formModel = this.itemForm.value;

    this.http.getBookingDetails(this.formModel.studentId).subscribe({
      next: (res) => {
        this.eventList = res;
        this.showMessage = true;
        this.responseMessage = 'Records fetched successfully';
      },
      error: () => {
        this.showError = true;
        this.errorMessage = 'Unable to fetch details';
      }
    });
  }

}