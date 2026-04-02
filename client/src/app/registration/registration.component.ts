import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpService } from '../../services/http.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  itemForm!: FormGroup;
  formModel: any = {};
  showMessage: boolean = false;
  responseMessage: any = '';

  constructor(
    private fb: FormBuilder,
    private http: HttpService,
    private router: Router
  ) {}

  // ngOnInit(): void {
  //   this.itemForm = this.fb.group({
  //     username: [
  //       '',
  //       [
  //         Validators.required,
  //         Validators.minLength(3),
  //         Validators.maxLength(20),
  //         Validators.pattern(/^[a-zA-Z0-9_]+$/)   // letters, numbers, underscore
  //       ]
  //     ],

  //     password: [
  //       '',
  //       [
  //         Validators.required,
  //         Validators.minLength(8),
  //         Validators.maxLength(30),
  //         Validators.pattern(
  //           /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*()_+\-={}[\]|:;"'<>,.?/]).+$/
  //         )  // strong password
  //       ]
  //     ],

  //     role: ['', Validators.required],

  //     email: [
  //       '',
  //       [
  //         Validators.required,
  //         Validators.pattern(/^[^\s@]+@[^\s@]+\.[^\s@]+$/) // strict email format
  //       ]
  //     ]
  //   });
  // }

   ngOnInit(): void {
    this.itemForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      role: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]]
    });
  }
  onRegister() {
    if (this.itemForm.invalid) {
      this.showMessage = true;
      this.responseMessage = 'Please fill all required fields correctly';
      return;
    }

    this.formModel = this.itemForm.value;

    this.http.registerUser(this.formModel).subscribe({
      next: () => {
        this.showMessage = true;
        this.responseMessage = 'User Registered Successfully';
        this.itemForm.reset();
      },
      error: () => {
        this.showMessage = true;
        this.responseMessage = 'Failed to register user';
      }
    });
  }

}