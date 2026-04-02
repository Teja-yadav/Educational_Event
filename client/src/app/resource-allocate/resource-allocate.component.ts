import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpService } from '../../services/http.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-resource-allocate',
  templateUrl: './resource-allocate.component.html',
  styleUrls: ['./resource-allocate.component.scss']
})
export class ResourceAllocateComponent implements OnInit {

  itemForm!: FormGroup;
  formModel: any = {};
  showError: boolean = false;
  errorMessage: any = '';
  resourceList: any = [];
  assignModel: any = {};
  showMessage: boolean = false;
  responseMessage: any = '';
  eventList: any = [];

  constructor(
    private fb: FormBuilder,
    private http: HttpService,
    private auth: AuthService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.itemForm = this.fb.group({
      eventId: ['', Validators.required],
      resourceId: ['', Validators.required]
    });

    this.getEvent();
    this.getResources();
  }

  onSubmit() {
    if (this.itemForm.invalid) {
      this.showError = true;
      this.errorMessage = 'Event and Resource fields are required';
      return;
    }

    this.formModel = this.itemForm.value;

    this.http.allocateResources(
      this.formModel.eventId,
      this.formModel.resourceId,
      {}
    ).subscribe({
      next: (res) => {
        this.showMessage = true;
        this.responseMessage = 'Resource Allocated Successfully';
        this.itemForm.reset();
      },
      error: () => {
        this.showError = true;
        this.errorMessage = 'Failed to allocate resource';
      }
    });
  }

  getEvent() {
    this.http.GetAllevents().subscribe({
      next: (res) => { this.eventList = res; },
      error: () => { this.eventList = []; }
    });
  }

  getResources() {
    this.http.GetAllResources().subscribe({
      next: (res) => { this.resourceList = res; },
      error: () => { this.resourceList = []; }
    });
  }
}