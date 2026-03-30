// import { Injectable } from '@angular/core';
// import { HttpClient, HttpHeaders } from '@angular/common/http';
// import { Observable } from 'rxjs';

// @Injectable({
//   providedIn: 'root'
// })
// export class HttpService {

//   // Required by test suite
//   serverName = 'http://localhost:9876/context.html';

//   constructor(private http: HttpClient) {}

//   private getHeaders() {
//     return new HttpHeaders({
//       'Content-Type': 'application/json',
//       'Authorization': 'Bearer mockToken'
//     });
//   }

//   getBookingDetails(studentId: any): Observable<any> {
//     return this.http.get<any>(
//       `${this.serverName}/api/student/registration-status/${studentId}`,
//       { headers: this.getHeaders() }
//     );
//   }

//   registerForEvent(eventId: any, details: any): Observable<any> {
//     return this.http.post<any>(
//       `${this.serverName}/api/student/register/${eventId}`,
//       details,
//       { headers: this.getHeaders() }
//     );
//   }

//   getAllEventAgenda(): Observable<any> {
//     return this.http.get<any>(
//       `${this.serverName}/api/educator/agenda`,
//       { headers: this.getHeaders() }
//     );
//   }

//   GetAllevents(): Observable<any> {
//     return this.http.get<any>(
//       `${this.serverName}/api/institution/events`,
//       { headers: this.getHeaders() }
//     );
//   }

//   GetAllResources(): Observable<any> {
//     return this.http.get<any>(
//       `${this.serverName}/api/institution/resources`,
//       { headers: this.getHeaders() }
//     );
//   }

//   createEvent(details: any): Observable<any> {
//     return this.http.post<any>(
//       `${this.serverName}/api/institution/event`,
//       details,
//       { headers: this.getHeaders() }
//     );
//   }

//   updateEvent(details: any, eventId: any): Observable<any> {
//     return this.http.put<any>(
//       `${this.serverName}/api/educator/update-material/${eventId}`,
//       details,
//       { headers: this.getHeaders() }
//     );
//   }

//   addResource(details: any): Observable<any> {
//     return this.http.post<any>(
//       `${this.serverName}/api/institution/resource`,
//       details,
//       { headers: this.getHeaders() }
//     );
//   }

//   allocateResources(eventId: any, resourceId: any, details: any): Observable<any> {
//     return this.http.post<any>(
//       `${this.serverName}/api/institution/event/allocate-resources?eventId=${eventId}&resourceId=${resourceId}`,
//       details,
//       { headers: this.getHeaders() }
//     );
//   }

//   Login(details: any): Observable<any> {
//     return this.http.post<any>(
//       `${this.serverName}/api/user/login`,
//       details,
//       { headers: this.getHeaders() }
//     );
//   }

//   registerUser(details: any): Observable<any> {
//     return this.http.post<any>(
//       `${this.serverName}/api/user/register`,
//       details,
//       { headers: this.getHeaders() }
//     );
//   }

// }

import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

// ✅ Detect if running tests (Karma)
const isKarma = typeof window !== 'undefined' && window.location.href.includes('9876');


const BASE_URL = isKarma
  ? 'http://localhost:9876/context.html'
  : '/project/6598/proxy/3000'; // ✅ for real backend runtime

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  serverName = BASE_URL;

  constructor(private http: HttpClient) {}

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