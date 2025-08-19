import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BookRestService {

  private backendUrl = `http://localhost:${environment.springBootPort}/api/ws/connect`;

  constructor(private http: HttpClient) {}

  connectToChannel(channel: string): Observable<any> {
    return this.http.post(this.backendUrl + `?channel=${channel}`, {}, { responseType: 'text' });
  }
}
