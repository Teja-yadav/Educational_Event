import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
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
 
  constructor(
    private fb: FormBuilder,
    private http: HttpService,
    private router: Router
  ) {}
 
  ngOnInit(): void {
    this.itemForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      materials: ['', Validators.required]
    });
 
    this.getEvents();
  }
 
  getEvents() {
    this.http.GetAllevents().subscribe({
      next: (res) => this.eventList = res,
      error: () => this.eventList = []
    });
  }
 
  onSubmit() {
    if (this.itemForm.invalid) {
      this.showError = true;
      this.errorMessage = 'All fields are required';
      return;
    }
 
    const payload = {
      name: this.itemForm.value.name,
      description: this.itemForm.value.description,
      materials: this.itemForm.value.materials
    };
 
    this.http.createEvent(payload).subscribe({
      next: () => {
        this.showMessage = true;
        this.responseMessage = 'Event Created Successfully';
        this.showError = false;
        this.itemForm.reset();
        this.getEvents();
      },
      error: (err) => {
        console.error(err);
        this.showError = true;
        this.errorMessage = err?.error || 'Failed to create event';
      }
    });
  }
}