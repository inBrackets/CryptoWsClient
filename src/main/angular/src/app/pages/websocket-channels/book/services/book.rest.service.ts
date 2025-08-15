import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class BookRestService {

  private backendUrl = 'http://localhost:8080/api/ws/connect';

  constructor(private http: HttpClient) {}

  connectToChannel(channel: string): Observable<any> {
    return this.http.post(this.backendUrl + `?channel=${channel}`, {}, { responseType: 'text' });
  }
}
