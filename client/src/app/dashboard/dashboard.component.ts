import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

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