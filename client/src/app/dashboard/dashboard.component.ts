import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { HttpService } from '../../services/http.service';

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

  role: string = '';
  username: string = 'User';

  active: Section = 'home';

  activeEvents = 0;
  registrations = 0;
  resources = 0;
  totalUsers = 0;

  upcoming: any[] = [];

  eventCountBadge = 0;
  showTotalUsers = false;

  constructor(
    private router: Router,
    private auth: AuthService,
    private http: HttpService
  ) {}

  ngOnInit(): void {
    this.role = (localStorage.getItem('role') || '').toUpperCase();
    this.username = (localStorage.getItem('username') || 'User');

    if (this.role === 'INSTITUTION') this.active = 'home';
    if (this.role === 'EDUCATOR') this.active = 'educator-agenda';
    if (this.role === 'STUDENT') this.active = 'register-for-event';

    if (this.role === 'INSTITUTION') {
      this.loadInstitutionStats();
      this.loadUpcoming();
    }
  }

  setSection(s: Section) {
    this.active = s;

    if (this.role === 'INSTITUTION' && s === 'home') {
      this.loadInstitutionStats();
      this.loadUpcoming();
    }
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

        this.eventCountBadge = list.length;

        this.activeEvents = list.filter((e: any) => {
          const t = e.eventDateTime ? new Date(e.eventDateTime).getTime() : now;
          return !isNaN(t) && t >= now;
        }).length;
      },
      error: () => {
        this.eventCountBadge = 0;
        this.activeEvents = 0;
      }
    });

    this.http.GetAllResources().subscribe({
      next: (res) => {
        const list = Array.isArray(res) ? res : [];
        this.resources = list.length;
      },
      error: () => {
        this.resources = 0;
      }
    });

    this.http.getRegistrationsCount().subscribe({
      next: (res) => {
        this.registrations = Number(res) || 0;
      },
      error: () => {
        this.registrations = 0;
      }
    });

    this.totalUsers = 0;
    this.showTotalUsers = false;
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
      error: () => {
        this.upcoming = [];
      }
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