import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpService } from '../../services/http.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register-for-event',
  templateUrl: './register-for-event.component.html',
  styleUrls: ['./register-for-event.component.scss']
})
export class RegisterForEventComponent implements OnInit {

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
      studentId: ['', [Validators.required, Validators.pattern(this.studentIdPattern)]],
      eventId: ['', Validators.required],
      //status: ['', Validators.required]
    });

    this.getEvents();
  }

getEvents() {
  this.http.getStudentEvents().subscribe({
    next: (res) => {
      const now = new Date().getTime();

      this.eventList = (res || []).filter((e: any) => {
        const t = new Date(e.eventDateTime).getTime();
        return !isNaN(t) && t >= now;
      });
    },
    error: () => {
      this.eventList = [];
    }
  });
}

  Submit() {
    this.showError = false;
    this.errorMessage = '';
    this.showMessage = false;
    this.responseMessage = '';

    if (this.itemForm.invalid) {
      this.showError = true;

      const ctrl = this.itemForm.get('studentId');
      if (ctrl?.errors?.['required']) {
        this.errorMessage = 'Student ID is required';
      } else if (ctrl?.errors?.['pattern']) {
        this.errorMessage = 'Student ID must be like LTM01, LTM09, LTM90 (starts with LTM + 2 digits)';
      } else {
        this.errorMessage = 'All fields are required';
      }

      return;
    }

    const payload = {
      studentId: (this.itemForm.value.studentId as string).toUpperCase(),
      eventId: this.itemForm.value.eventId,
     // status: this.itemForm.value.status
    };

    this.http.registerForEvent(payload.eventId, payload).subscribe({
      next: () => {
        this.showMessage = true;
        this.responseMessage = 'Registered Successfully';
        this.itemForm.reset();
      },
      error: (err) => {
        this.showError = true;
        this.errorMessage = err?.error?.message || 'Failed to register';
      }
    });
  }
}
