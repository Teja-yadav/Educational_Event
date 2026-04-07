import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpService } from '../../services/http.service';

@Component({
  selector: 'app-resource-allocate',
  templateUrl: './resource-allocate.component.html',
  styleUrls: ['./resource-allocate.component.scss']
})
export class ResourceAllocateComponent implements OnInit {

  itemForm!: FormGroup;

  eventList: any[] = [];
  resourceList: any[] = [];
  allocationList: any[] = [];

  showError = false;
  errorMessage = '';

  showMessage = false;
  responseMessage = '';

  constructor(private fb: FormBuilder, private http: HttpService) {}

  ngOnInit(): void {
    this.itemForm = this.fb.group({
      eventId: ['', Validators.required],
      resourceId: ['', Validators.required]
    });

    this.getEvents();
    this.getResources();
  }

  onSubmit(): void {
    this.showError = false;
    this.showMessage = false;
    this.errorMessage = '';
    this.responseMessage = '';

    if (this.itemForm.invalid) {
      this.showError = true;
      this.errorMessage = 'Please select event and resource';
      return;
    }

    const eventId = this.itemForm.value.eventId;
    const resourceId = this.itemForm.value.resourceId;

    // ✅ Match your service method name: allocateResources(eventId, resourceId, details)
    this.http.allocateResources(eventId, resourceId).subscribe({
      next: () => {
        this.showMessage = true;
        this.responseMessage = 'Resource allocated successfully';
        this.itemForm.reset();
      },
      error: () => {
        this.showError = true;
        this.errorMessage = 'Error allocating resource';
      }
    });
  }

  getEvents(): void {
    // ✅ Match your service method name: GetAllevents()
    this.http.GetAllevents().subscribe({
      next: (res: any) => this.eventList = res || [],
      error: () => this.eventList = []
    });
  }

  getResources(): void {
    this.http.GetAllResources().subscribe({
      next: (res: any) => this.resourceList = res || [],
      error: () => this.resourceList = []
    });
  }

}