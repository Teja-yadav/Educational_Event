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

          Validators.pattern(

            /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*()_+\-={}[\]|:;"'<>,.?/]).+$/

          )

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
 
    // Clear messages when user edits form again

    this.itemForm.valueChanges.subscribe(() => {

      if (this.showMessage) {

        this.showMessage = false;

        this.responseMessage = '';

      }

    });

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
 
        // Optional: redirect to login after successful registration

        setTimeout(() => {

          this.router.navigate(['/login']);

        }, 1500);

      },

      error: (err: any) => {

        this.showMessage = true;

        this.responseMessage =

          err?.error?.message || 'Failed to register user';

      }

    });

  }

}

 