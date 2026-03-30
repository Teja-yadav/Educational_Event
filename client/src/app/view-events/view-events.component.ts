import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpService } from '../../services/http.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-view-events',
  templateUrl: './view-events.component.html',
  styleUrls: ['./view-events.component.scss']
})
export class ViewEventsComponent implements OnInit {

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
      name: ['', Validators.required],
      description: ['', Validators.required],
      materials: ['', Validators.required]
    });

    this.getEvents();
  }

  getEvents() {
    this.http.GetAllevents().subscribe({
      next: (res) => { this.eventList = res; },
      error: () => { this.eventList = []; }
    });
  }

  edit(val: any) {
    this.isUpdate = true;
    this.eventObj = val;
    this.itemForm.patchValue({
      name: val.name,
      description: val.description,
      materials: val.materials
    });
  }

  onSubmit() {
    if (this.itemForm.invalid) {
      this.showError = true;
      this.errorMessage = 'All fields are required';
      return;
    }

    this.formModel = this.itemForm.value;

    this.http.updateEvent(this.formModel, this.eventObj.id).subscribe({
      next: () => {
        this.showMessage = true;
        this.responseMessage = 'Event updated successfully';
        this.isUpdate = false;
        this.itemForm.reset();
        this.getEvents();
      },
      error: () => {
        this.showError = true;
        this.errorMessage = 'Error updating event';
      }
    });
  }
}