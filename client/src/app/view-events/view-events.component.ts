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

  isUpdate = false;
  isEducator = false;

  showError = false;
  errorMessage = '';
  showMessage = false;
  responseMessage = '';

  constructor(
    private fb: FormBuilder,
    private http: HttpService
  ) { }

  ngOnInit(): void {
    const role = localStorage.getItem('role');
    this.isEducator = role === 'EDUCATOR';

    this.itemForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      materials: ['', Validators.required]
    });

    this.loadEvents();
  }

  loadEvents() {
    if (this.isEducator) {
      this.http.getAllEducatorAgenda().subscribe({
        next: res => {
          this.eventList = res
          console.log(res);
        },
        error: () => this.eventList = []
      });
    } else {
      this.http.GetAllevents().subscribe({
        next: res => {
          this.eventList = res
          console.log(res);
        }, error: () => this.eventList = []
      });
    }
  }

  edit(event: any) {
    this.isUpdate = true;
    this.eventObj = event;
    this.itemForm.patchValue({
      name: event.name,
      description: event.description,
      materials: event.materials
    });
  }

  onSubmit() {
    if (this.itemForm.invalid) return;

    this.http.updateEvent(this.itemForm.value, this.eventObj.id).subscribe({
      next: () => {
        this.showMessage = true;
        this.responseMessage = 'Event updated successfully';
        this.isUpdate = false;
        this.loadEvents();
      },
      error: () => {
        this.showError = true;
        this.errorMessage = 'Error updating event';
      }
    });
  }
}
