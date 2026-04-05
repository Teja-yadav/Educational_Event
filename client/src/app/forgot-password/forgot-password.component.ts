import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpService } from '../../services/http.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent implements OnInit {

  step = 1;
  emailForm!: FormGroup;
  otpForm!: FormGroup;
  resetForm!: FormGroup;

  email = '';
  otp = '';

  showError = false;
  errorMessage = '';
  showMessage = false;
  responseMessage = '';

  constructor(private fb: FormBuilder, private http: HttpService, private router: Router) {}

  ngOnInit(): void {
    this.emailForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });

    this.otpForm = this.fb.group({
      otp: ['', [Validators.required, Validators.pattern(/^\d{6}$/)]]
    });

    this.resetForm = this.fb.group({
      newPassword: ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', [Validators.required]]
    });
  }

  sendOtp() {
    this.resetMsgs();

    if (this.emailForm.invalid) {
      this.showError = true;
      this.errorMessage = 'Enter a valid email';
      return;
    }

    this.email = (this.emailForm.value.email || '').toString().trim();

    this.http.forgotPassword({ email: this.email }).subscribe({
      next: (res: any) => {
        this.showMessage = true;
        this.responseMessage = res || 'OTP sent successfully';
        this.step = 2;
      },
      error: (err: any) => {
        this.showError = true;
        this.errorMessage = this.extractErrorMessage(err, 'Failed to send OTP');
      }
    });
  }

  verifyOtp() {
    this.resetMsgs();

    if (this.otpForm.invalid) {
      this.showError = true;
      this.errorMessage = 'Enter a valid 6-digit OTP';
      return;
    }

    this.otp = (this.otpForm.value.otp || '').toString().trim();

    this.http.verifyOtp({ email: this.email, otp: this.otp }).subscribe({
      next: (res: any) => {
        this.showMessage = true;
        this.responseMessage = res || 'OTP verified';
        this.step = 3;
      },
      error: (err: any) => {
        this.showError = true;
        this.errorMessage = this.extractErrorMessage(err, 'Invalid OTP');
      }
    });
  }

  resetPassword() {
    this.resetMsgs();

    if (this.resetForm.invalid) {
      this.showError = true;
      this.errorMessage = 'Enter valid password';
      return;
    }

    const newPassword = (this.resetForm.value.newPassword || '').toString();
    const confirmPassword = (this.resetForm.value.confirmPassword || '').toString();

    if (newPassword !== confirmPassword) {
      this.showError = true;
      this.errorMessage = 'Passwords do not match';
      return;
    }

    const payload = {
      email: this.email,
      otp: this.otp,
      newPassword
    };

    this.http.resetPassword(payload).subscribe({
      next: (res: any) => {
        this.showMessage = true;
        this.responseMessage = res || 'Password reset successful. Please login.';
        setTimeout(() => this.router.navigate(['/login']), 1000);
      },
      error: (err: any) => {
        this.showError = true;
        this.errorMessage = this.extractErrorMessage(err, 'Reset failed');
      }
    });
  }

  private resetMsgs() {
    this.showError = false;
    this.errorMessage = '';
    this.showMessage = false;
    this.responseMessage = '';
  }

  private extractErrorMessage(err: any, fallback: string): string {
    if (!err) return fallback;

    if (typeof err === 'string') return err;

    if (typeof err?.error === 'string') return err.error;

    if (typeof err?.error?.message === 'string') return err.error.message;

    if (typeof err?.message === 'string') return err.message;

    try {
      return JSON.stringify(err?.error ?? err);
    } catch {
      return fallback;
    }
  }
}
