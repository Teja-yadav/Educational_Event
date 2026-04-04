import { Component, OnInit } from '@angular/core';

import { HttpService } from '../../services/http.service';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

type Section =
  | 'home'
  | 'create-event'
  | 'add-resource'
  | 'resource-allocate'
  | 'view-events'
  | 'booking-details'
  | 'register-for-event'
  | 'educator-update'
  | 'educator-agenda';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  role: string | null = null;
  username: string = 'User';

  // sidebar selected item
  active: Section = 'home';

  // dashboard stats
  activeEvents = 0;
  registrations = 0;
  resources = 0;
  totalUsers = 0;

  upcoming: any[] = [];

  constructor(
    private router: Router,
    private auth: AuthService,
    private http: HttpService
  ) {}

  ngOnInit(): void {
    this.role = (localStorage.getItem('role') || '').toUpperCase();
    this.username = (localStorage.getItem('username') || 'User');

    // default landing per role
    if (this.role === 'INSTITUTION') this.active = 'home';
    if (this.role === 'EDUCATOR') this.active = 'educator-agenda';
    if (this.role === 'STUDENT') this.active = 'register-for-event';

    if (this.role === 'INSTITUTION') {
      this.loadInstitutionStats();
      this.loadUpcoming();
    }
  }

  setSection(s: Section) {
    // clear any UI messages if needed later
    this.active = s;
  }

  getGreeting(): string {
    const h = new Date().getHours();
    if (h < 12) return 'Good morning';
    if (h < 17) return 'Good afternoon';
    return 'Good evening';
  }

  loadInstitutionStats() {
    this.http.GetAllevents().subscribe({
      next: (res) => {
        const list = Array.isArray(res) ? res : [];
        const now = Date.now();
        this.activeEvents = list.filter((e: any) =>
          e.eventDateTime ? new Date(e.eventDateTime).getTime() >= now : true
        ).length;
      },
      error: () => this.activeEvents = 0
    });

    this.http.GetAllResources().subscribe({
      next: (res) => {
        const list = Array.isArray(res) ? res : [];
        this.resources = list.length;
      },
      error: () => this.resources = 0
    });

    // If you don’t have backend endpoints for these counts, keep 0 for now
    this.registrations = 0;
    this.totalUsers = 0;
  }

  loadUpcoming() {
    this.http.GetAllevents().subscribe({
      next: (res) => {
        const list = Array.isArray(res) ? res : [];
        const now = Date.now();
        this.upcoming = list
          .filter((e: any) => e.eventDateTime && new Date(e.eventDateTime).getTime() >= now)
          .sort((a: any, b: any) => new Date(a.eventDateTime).getTime() - new Date(b.eventDateTime).getTime())
          .slice(0, 4);
      },
      error: () => this.upcoming = []
    });
  }

  logout() {
    const ok = window.confirm('Are you sure you want to logout?');
    if (ok) {
      this.auth.logout();
      this.router.navigate(['/login']);
    }
  }
}
