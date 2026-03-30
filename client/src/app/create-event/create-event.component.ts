import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpService } from '../../services/http.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-create-event',
  templateUrl: './create-event.component.html',
  styleUrls: ['./create-event.component.scss']
})
export class CreateEventComponent implements OnInit {

  itemForm!: FormGroup;
  formModel: any = {};
  showError: boolean = false;
  errorMessage: any = '';
  eventList: any = [];
  assignModel: any = {};
  showMessage: boolean = false;
  responseMessage: any = '';

  constructor(
    private fb: FormBuilder,
    private http: HttpService,
    private auth: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.itemForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      materials: ['', Validators.required]
    });

    this.getEvent();
  }

  getEvent() {
    this.http.GetAllevents().subscribe({
      next: (res) => {
        this.eventList = res;
      },
      error: () => {
        this.eventList = [];
      }
    });
  }

  onSubmit() {
    if (this.itemForm.invalid) {
      this.showError = true;
      this.errorMessage = 'All fields are required';
      return;
    }

    this.formModel = this.itemForm.value;

    this.http.createEvent(this.formModel).subscribe({
      next: (res) => {
        this.showMessage = true;
        this.responseMessage = 'Event Created Successfully';
        this.itemForm.reset();
        this.getEvent();
      },
      error: () => {
        this.showError = true;
        this.errorMessage = 'Failed to create event';
      }
    });
  }

}