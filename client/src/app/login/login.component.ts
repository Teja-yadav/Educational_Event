import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpService } from '../../services/http.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  itemForm!: FormGroup;
  formModel: any = {};
  showError: boolean = false;
  errorMessage: any = '';

  constructor(
    private fb: FormBuilder,
    private http: HttpService,
    private auth: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.itemForm = this.fb.group({
      username: [
        '',
        [Validators.required, Validators.minLength(3),
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
          Validators.pattern(
            /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*()_+\-={}[\]|:;"'<>,.?/]).+$/
          )
        ]
      ]
    });
  }
  registration() {
    if (this.itemForm.invalid) {
      this.showError = true;
      this.errorMessage = 'Username and Password are required';
      return;
    }
    this.formModel = this.itemForm.value;
    this.http.Login(this.formModel).subscribe({
      next: (res) => {
        this.auth.saveToken(res.token);
        this.auth.SetRole(res.role);
        localStorage.setItem('username', res.username);
        this.router.navigate(['/dashboard']);
      },
      error: () => {
        this.showError = true;
        this.errorMessage = 'Invalid Credentials';
      }
    });
  }
}