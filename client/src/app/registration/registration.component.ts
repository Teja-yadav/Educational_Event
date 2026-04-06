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

  showMessage: boolean = false;
  responseMessage: string = '';

  // ✅ Step-wise password checks (same as login)
  passwordChecks = {
    minLen: false,
    upper: false,
    lower: false,
    number: false,
    special: false
  };

  constructor(
    private fb: FormBuilder,
    private http: HttpService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.itemForm = this.fb.group({
      username: [
        '',
        [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(20),
          Validators.pattern(/^[a-zA-Z0-9_]+$/)
        ]
      ],
      password: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          Validators.maxLength(30),
          Validators.pattern(/^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*()_+\-={}[\]|:;"'<>,.?/]).+$/)
        ]
      ],
      role: ['', Validators.required],
      email: [
        '',
        [
          Validators.required,
          Validators.pattern(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)
        ]
      ]
    });

    // ✅ clear status message when user types
    this.itemForm.valueChanges.subscribe(() => {
      if (this.showMessage) {
        this.showMessage = false;
        this.responseMessage = '';
      }
    });

    // ✅ update password checks on any password change (typing/paste/autofill)
    this.itemForm.get('password')?.valueChanges.subscribe((val) => {
      const value = (val || '').toString();
      this.passwordChecks = this.evaluatePassword(value);
    });

    // ✅ initialize once
    this.onPasswordInput();
  }

  // ✅ called on input as well (safe)
  onPasswordInput() {
    const value = (this.itemForm.get('password')?.value || '').toString();
    this.passwordChecks = this.evaluatePassword(value);
  }

  private evaluatePassword(value: string) {
    return {
      minLen: value.length >= 8,
      upper: /[A-Z]/.test(value),
      lower: /[a-z]/.test(value),
      number: /\d/.test(value),
      special: /[!@#$%^&*()_+\-={}[\]|:;"'<>,.?/]/.test(value)
    };
  }

  onRegister(): void {
    if (this.itemForm.invalid) {
      this.itemForm.markAllAsTouched();
      this.showMessage = true;
      this.responseMessage = 'Please fill all required fields correctly';
      return;
    }

    const payload = this.itemForm.value;

    this.http.registerUser(payload).subscribe({
      next: () => {
        this.showMessage = true;
        this.responseMessage = 'User Registered Successfully';

        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 1500);
      },
      error: (err: any) => {
        this.showMessage = true;
        this.responseMessage = err?.error?.message || err?.error || 'Failed to register user';
      }
    });
  }
}