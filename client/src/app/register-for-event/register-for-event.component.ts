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

  constructor(
    private fb: FormBuilder,
    private http: HttpService,
    private auth: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.itemForm = this.fb.group({
      studentId: ['', Validators.required],
      eventId: ['', Validators.required],
      status: ['', Validators.required]
    });

    this.getEvents();
  }

  getEvents() {
    this.http.GetAllevents().subscribe({
      next: (res) => {
        this.eventList = res;
      },
      error: () => {
        this.eventList = [];
      }
    });
  }

  Submit() {
    if (this.itemForm.invalid) {
      this.showError = true;
      this.errorMessage = 'All fields are required';
      return;
    }

    this.formModel = this.itemForm.value;

    this.http.registerForEvent(this.formModel.eventId, this.formModel).subscribe({
      next: (res) => {
        this.showMessage = true;
        this.responseMessage = 'Registered Successfully';
        this.itemForm.reset();
      },
      error: () => {
        this.showError = true;
        this.errorMessage = 'Failed to register';
      }
    });
  }

}