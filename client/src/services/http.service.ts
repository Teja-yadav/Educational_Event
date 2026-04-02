import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment.development';

const BASE_URL = environment.apiUrl

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  serverName = BASE_URL;

  constructor(private http: HttpClient) { }

  private getAuthHeaders() {
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer mockToken'
    });
  }

  private getJsonHeaders() {
    return new HttpHeaders({
      'Content-Type': 'application/json'
    });
  }

  // ---------------- STUDENT ----------------
  getBookingDetails(studentId: any): Observable<any> {
    return this.http.get<any>(
      `${this.serverName}/api/student/registration-status/${studentId}`,
      { headers: this.getAuthHeaders() }
    );
  }

  registerForEvent(eventId: any, details: any): Observable<any> {
    return this.http.post<any>(
      `${this.serverName}/api/student/register/${eventId}`,
      details,
      { headers: this.getAuthHeaders() }
    );
  }

  // ---------------- EDUCATOR ----------------
  getAllEventAgenda(): Observable<any> {
    return this.http.get<any>(
      `${this.serverName}/api/educator/agenda`,
      { headers: this.getAuthHeaders() }
    );
  }

  updateEvent(details: any, eventId: any): Observable<any> {
    return this.http.put<any>(
      `${this.serverName}/api/educator/update-material/${eventId}`,
      details,
      { headers: this.getAuthHeaders() }
    );
  }

  // ---------------- INSTITUTION ----------------
  GetAllevents(): Observable<any> {
    return this.http.get<any>(
      `${this.serverName}/api/institution/events`,
      { headers: this.getAuthHeaders() }
    );
  }

  GetAllResources(): Observable<any> {
    return this.http.get<any>(
      `${this.serverName}/api/institution/resources`,
      { headers: this.getAuthHeaders() }
    );
  }

  createEvent(details: any): Observable<any> {
    return this.http.post<any>(
      `${this.serverName}/api/institution/event`,
      details,
      { headers: this.getAuthHeaders() }
    );
  }

  addResource(details: any): Observable<any> {
    return this.http.post<any>(
      `${this.serverName}/api/institution/resource`,
      details,
      { headers: this.getAuthHeaders() }
    );
  }

  allocateResources(eventId: any, resourceId: any, details: any): Observable<any> {
    return this.http.post<any>(
      `${this.serverName}/api/institution/event/allocate-resources?eventId=${eventId}&resourceId=${resourceId}`,
      details,
      { headers: this.getAuthHeaders() }
    );
  }

  // ---------------- PUBLIC ----------------
  Login(details: any): Observable<any> {
    return this.http.post<any>(
      `${this.serverName}/api/user/login`,
      details,
      { headers: this.getJsonHeaders() }
    );
  }

  registerUser(details: any): Observable<any> {
    return this.http.post<any>(
      `${this.serverName}/api/user/register`,
      details,
      { headers: this.getJsonHeaders() }
    );
  }

}