import { Component, OnInit } from '@angular/core';import { FormBuilder, FormGroup, Validators } from '@angular/forms';

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
 
  showLanding: boolean = true;
 
  // ✅ Step-wise password checks

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

    private auth: AuthService,

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

      ]

    });
 
    // ✅ initialize step checks

    this.onPasswordInput();

  }
 
  showLoginPage() {

    this.showLanding = false;

  }
 
  // ✅ update step-wise validation while typing

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
 
  registration() {

    if (this.itemForm.invalid) {

      this.showError = true;

      this.errorMessage = 'Username and Password are required';

      return;

    }
 
    this.formModel = {

      username: (this.itemForm.value.username || '').trim(),

      password: (this.itemForm.value.password || '').trim()

    };
 
    this.http.Login(this.formModel).subscribe({

      next: (res: any) => {

        this.auth.saveToken(res.token);

        this.auth.SetRole(res.role);

        localStorage.setItem('username', res.username || this.formModel.username);

        this.router.navigate(['/dashboard']);

      },

      error: () => {

        this.showError = true;

        this.errorMessage = 'Invalid Credentials';

      }

    });

  }

}

 