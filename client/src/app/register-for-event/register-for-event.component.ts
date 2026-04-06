import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpService } from '../../services/http.service';

@Component({
  selector: 'app-register-for-event',
  templateUrl: './register-for-event.component.html',
  styleUrls: ['./register-for-event.component.scss']
})
export class RegisterForEventComponent implements OnInit {

  itemForm!: FormGroup;
  showError = false;
  errorMessage: any = '';
  showMessage = false;
  responseMessage: any = '';
  eventList: any[] = [];
  loggedInUsername = '';

  constructor(private fb: FormBuilder, private http: HttpService) {}

  ngOnInit(): void {
    this.loggedInUsername = (localStorage.getItem('username') || '').trim();

    this.itemForm = this.fb.group({
      studentId: [this.loggedInUsername, [Validators.required]],
      eventId: ['', Validators.required],
      venue: ['']
    });

    this.getEvents();

    this.itemForm.get('eventId')?.valueChanges.subscribe((eventId) => {
      const selected = this.eventList.find(e => String(e.id) === String(eventId));
      this.itemForm.get('venue')?.setValue(selected?.venue || '');
    });
  }

  getEvents() {
    this.http.getStudentEvents().subscribe({
      next: (res) => {
        const now = new Date().getTime();
        this.eventList = (Array.isArray(res) ? res : []).filter((e: any) => {
          const t = new Date(e.eventDateTime).getTime();
          return !isNaN(t) && t >= now;
        });
      },
      error: () => {
        this.eventList = [];
      }
    });
  }

  clearForm() {
    this.itemForm.reset({
      studentId: this.loggedInUsername,
      eventId: '',
      venue: ''
    });
  }

  Submit() {
    this.showError = false;
    this.errorMessage = '';
    this.showMessage = false;
    this.responseMessage = '';

    if (this.itemForm.invalid) {
      this.showError = true;
      this.errorMessage = 'All fields are required';
      return;
    }

    const entered = (this.itemForm.value.studentId || '').trim();

    if (!this.loggedInUsername || entered.toLowerCase() !== this.loggedInUsername.toLowerCase()) {
      this.showError = true;
      this.errorMessage = 'You can register only using your own username';
      return;
    }

    const payload = {
      studentId: entered,
      status: 'REGISTERED'
    };

    const eventId = this.itemForm.value.eventId;

    this.http.registerForEvent(eventId, payload).subscribe({
      next: () => {
        this.showMessage = true;
        this.responseMessage = 'Registered Successfully';
        this.clearForm();

        setTimeout(() => {
          this.showMessage = false;
          this.responseMessage = '';
        }, 3000);
      },
      error: (err) => {
        this.showError = true;
        this.errorMessage = err?.error?.message || err?.error || 'Failed to register';
      }
    });
  }
}