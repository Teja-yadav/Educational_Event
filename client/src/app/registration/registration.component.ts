import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpService } from '../../services/http.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  itemForm!: FormGroup;
  showMessage: boolean = false;
  responseMessage: any = '';

  constructor(
    private fb: FormBuilder,
    private http: HttpService
  ) {}

  ngOnInit(): void {
    this.itemForm = this.fb.group({
      username: ['', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(20),
        Validators.pattern(/^[a-zA-Z0-9_]+$/)
      ]],
      password: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(
          /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*()_+\-={}[\]|:;"'<>,.?]).+$/
        )
      ]],
      role: ['', Validators.required],
      email: ['', [
        Validators.required,
        Validators.email
      ]]
    });
  }

  onRegister() {
    if (this.itemForm.invalid) {
      this.showMessage = true;
      this.responseMessage = 'Please fill all fields correctly';
      return;
    }

    this.http.registerUser(this.itemForm.value).subscribe({
      next: () => {
        this.showMessage = true;
        this.responseMessage = 'User Registered Successfully';
        this.itemForm.reset();
      },
      error: () => {
        this.showMessage = true;
        this.responseMessage = 'Registration Failed';
      }
    });
  }
}