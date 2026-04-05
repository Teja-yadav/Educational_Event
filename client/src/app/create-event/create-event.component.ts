import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpService } from '../../services/http.service';

@Component({
  selector: 'app-create-event',
  templateUrl: './create-event.component.html',
  styleUrls: ['./create-event.component.scss']
})
export class CreateEventComponent implements OnInit {

  itemForm!: FormGroup;
  eventList: any[] = [];

  showError = false;
  errorMessage = '';
  showMessage = false;
  responseMessage = '';
  minDateTime = '';

  constructor(
    private fb: FormBuilder,
    private http: HttpService
  ) {}

  ngOnInit(): void {
    this.minDateTime = this.getNowForDateTimeLocal();

    this.itemForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      materials: ['', Validators.required],
      eventDateTime: ['', Validators.required],
      venue: ['', Validators.required]
    });

    this.getEvent();
  }

  private getNowForDateTimeLocal(): string {
    const now = new Date();
    const pad = (n: number) => (n < 10 ? '0' + n : n);
    return `${now.getFullYear()}-${pad(now.getMonth() + 1)}-${pad(now.getDate())}T${pad(now.getHours())}:${pad(now.getMinutes())}`;
  }

  getEvent() {
    this.http.GetAllevents().subscribe({
      next: (res) => {
        this.eventList = Array.isArray(res) ? res : [];
      },
      error: () => {
        this.eventList = [];
      }
    });
  }

  onSubmit() {
    this.showError = false;
    this.errorMessage = '';
    this.showMessage = false;
    this.responseMessage = '';

    if (this.itemForm.invalid) {
      this.showError = true;
      this.errorMessage = 'Please fill all required fields (including Date & Time and Venue)';
      return;
    }

    const payload = {
      name: this.itemForm.value.name,
      description: this.itemForm.value.description,
      materials: this.itemForm.value.materials,
      eventDateTime: this.itemForm.value.eventDateTime,
      venue: this.itemForm.value.venue
    };

    this.http.createEvent(payload).subscribe({
      next: () => {
        this.showMessage = true;
        this.responseMessage = 'Event Created Successfully';
        this.itemForm.reset();
        this.minDateTime = this.getNowForDateTimeLocal();
        this.getEvent();
      },
      error: (err) => {
        this.showError = true;
        this.errorMessage = err?.error?.message || err?.error || 'Failed to create event';
      }
    });
  }
}