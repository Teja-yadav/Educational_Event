import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpService } from '../../services/http.service';

@Component({
  selector: 'app-view-events',
  templateUrl: './view-events.component.html',
  styleUrls: ['./view-events.component.scss']
})
export class ViewEventsComponent implements OnInit {

  itemForm!: FormGroup;
  eventList: any[] = [];
  eventObj: any = {};

  role = '';
  isEducator = false;
  isInstitution = false;

  showError = false;
  errorMessage = '';
  showMessage = false;
  responseMessage = '';

  showModal = false;

  constructor(private fb: FormBuilder, private http: HttpService) {}

  ngOnInit(): void {
    this.role = (localStorage.getItem('role') || '').toUpperCase();
    this.isEducator = this.role === 'EDUCATOR';
    this.isInstitution = this.role === 'INSTITUTION';

    this.itemForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      materials: ['', Validators.required],
      eventDateTime: [''],
      venue: ['']   // ✅ NEW
    });

    this.loadEvents();
  }

  loadEvents() {
    if (this.isEducator) {
      this.http.getAllEducatorAgenda().subscribe({
        next: (res) => this.eventList = Array.isArray(res) ? res : [],
        error: () => this.eventList = []
      });
    } else {
      this.http.GetAllevents().subscribe({
        next: (res) => this.eventList = Array.isArray(res) ? res : [],
        error: () => this.eventList = []
      });
    }
  }

  openUpdateModal(event: any) {
    this.resetMsgs();

    this.eventObj = event;

    const dt = event.eventDateTime ? this.toDateTimeLocal(event.eventDateTime) : '';

    this.itemForm.patchValue({
      name: event.name,
      description: event.description,
      materials: event.materials,
      eventDateTime: dt,
      venue: event.venue || '' 
    });

    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
    this.itemForm.reset();
  }

  onSubmit() {
    this.resetMsgs();

    if (this.itemForm.invalid) return;

    const payload: any = {
      name: this.itemForm.value.name,
      description: this.itemForm.value.description,
      materials: this.itemForm.value.materials
    };

    if (this.itemForm.value.eventDateTime) {
      payload.eventDateTime = this.itemForm.value.eventDateTime;
    }

    if (this.itemForm.value.venue && this.itemForm.value.venue.trim() !== '') {
      payload.venue = this.itemForm.value.venue.trim();
    }

    this.http.updateEvent(payload, this.eventObj.id).subscribe({
      next: () => {
        this.showMessage = true;
        this.responseMessage = 'Event updated successfully';
        this.showModal = false;
        this.itemForm.reset();
        this.loadEvents();

        setTimeout(() => {
          this.showMessage = false;
          this.responseMessage = '';
        }, 3000);
      },
      error: () => {
        this.showError = true;
        this.errorMessage = 'Error updating event';
      }
    });
  }

  deleteEvent(eventId: any) {
    this.resetMsgs();

    const ok = window.confirm('Are you sure you want to delete this event?');
    if (!ok) return;

    this.http.deleteEvent(eventId).subscribe({
      next: () => {
        this.showMessage = true;
        this.responseMessage = 'Event deleted successfully';
        this.loadEvents();

        setTimeout(() => {
          this.showMessage = false;
          this.responseMessage = '';
        }, 3000);
      },
      error: (err) => {
        console.error(err);
        this.showError = true;
        this.errorMessage = err?.error || err?.message || 'Error deleting event';
      }
    });
  }

  private resetMsgs() {
    this.showError = false;
    this.errorMessage = '';
    this.showMessage = false;
    this.responseMessage = '';
  }

  private toDateTimeLocal(value: any): string {
    try {
      const d = new Date(value);
      const pad = (n: number) => (n < 10 ? '0' + n : n);
      return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`;
    } catch {
      return '';
    }
  }
}