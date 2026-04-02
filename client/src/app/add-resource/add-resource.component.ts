import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpService } from '../../services/http.service';

@Component({
  selector: 'app-add-resource',
  templateUrl: './add-resource.component.html',
  styleUrls: ['./add-resource.component.scss']
})
export class AddResourceComponent implements OnInit {

  itemForm!: FormGroup;
  formModel: any = {};
  showError: boolean = false;
  errorMessage: any = '';
  resourceList: any = [];
  assignModel: any = {};
  showMessage: any = false;
  responseMessage: any = '';

  constructor(private fb: FormBuilder, private http: HttpService) {}

  ngOnInit(): void {
    this.itemForm = this.fb.group({
      resourceType: ['', Validators.required],
      description: ['', Validators.required],
      availability: ['', Validators.required]
    });

    this.getResources();
  }

  onSubmit() {
    if (this.itemForm.invalid) {
      this.showError = true;
      this.errorMessage = 'Please fill all required fields';
      return;
    }

    this.formModel = this.itemForm.value;

    this.http.addResource(this.formModel).subscribe({
      next: (res) => {
        this.showMessage = true;
        this.responseMessage = 'Resource added successfully';
        this.getResources();
        this.itemForm.reset();
      },
      error: () => {
        this.showError = true;
        this.errorMessage = 'Error adding resource';
      }
    });
  }

  getResources() {
    this.http.GetAllResources().subscribe({
      next: (res) => {
        this.resourceList = res;
      },
      error: () => {
        this.resourceList = [];
      }
    });
  }

}