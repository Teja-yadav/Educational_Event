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

        // ✅ If you WANT redirect later, uncomment:
        // setTimeout(() => {
        //   this.router.navigate(['/login']);
        // }, 1500);
      },
      error: () => {
        this.showMessage = true;
        this.responseMessage = 'Failed to register user';
      }
    });
  }

}