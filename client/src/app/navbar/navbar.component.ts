import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  role: string | null = null;

  constructor(private router: Router, private auth: AuthService) {}

  ngOnInit(): void {
    this.role = localStorage.getItem('role');
  }

  logout() {
    const confirmLogout = window.confirm('Are you sure you want to logout?');

    if (confirmLogout) {
      this.auth.logout();
      this.router.navigate(['/login']);
    }
  }
}