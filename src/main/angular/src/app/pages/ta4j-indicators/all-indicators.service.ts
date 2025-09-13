import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AllIndicatorsService {

  constructor(private http: HttpClient) {
  }

  apiUrl: string = `http://localhost:${environment.springBootPort}`

  getRsiSeries(): Observable<{ timestamp: number; rsi: number }[]> {
    return this.http.get<{ timestamp: number; rsi: number }[]>(`${this.apiUrl}/api/candlestick/get-rsi`);
  }


}
