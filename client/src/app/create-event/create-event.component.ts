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
  educators: any[] = [];

  showError = false;
  errorMessage = '';
  showMessage = false;
  responseMessage = '';
  minDateTime = '';

  constructor(private fb: FormBuilder, private http: HttpService) {}

  ngOnInit(): void {
    this.minDateTime = this.getNowForDateTimeLocal();

    this.itemForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      materials: ['', Validators.required],
      eventDateTime: ['', Validators.required],
      venue: ['', Validators.required],
      educatorUsername: ['', Validators.required]
    });

    this.getEvent();
    this.loadEducators();
  }

  private getNowForDateTimeLocal(): string {
    const now = new Date();
    const pad = (n: number) => (n < 10 ? '0' + n : n);
    return `${now.getFullYear()}-${pad(now.getMonth() + 1)}-${pad(now.getDate())}T${pad(now.getHours())}:${pad(now.getMinutes())}`;
  }

  loadEducators() {
    this.http.getEducators().subscribe({
      next: (res) => {
        this.educators = Array.isArray(res) ? res : [];
      },
      error: () => {
        this.educators = [];
      }
    });
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
      this.errorMessage = 'Please fill all required fields';
      return;
    }

    const payload = {
      name: this.itemForm.value.name,
      description: this.itemForm.value.description,
      materials: this.itemForm.value.materials,
      eventDateTime: this.itemForm.value.eventDateTime,
      venue: this.itemForm.value.venue,
      educatorUsername: this.itemForm.value.educatorUsername
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
