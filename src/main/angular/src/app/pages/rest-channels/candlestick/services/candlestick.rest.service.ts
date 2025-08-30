import {Injectable} from '@angular/core';
import {environment} from '../../../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ApiResponse, Candlestick, CandlestickResult} from '../model/dto';

@Injectable({
  providedIn: 'root'
})
export class CandlestickRestService {

  apiUrl: string = `http://localhost:${environment.springBootPort}/api/candlestick`;

  constructor(private http: HttpClient) {
  }

  getCandlesticks(instrumentName: string, timeFrame: string): Observable<ApiResponse<CandlestickResult>> {
    return this.http.get<ApiResponse<CandlestickResult>>(`${this.apiUrl}/get-all?instrument_name=${instrumentName}&time_frame=${timeFrame}`);
  }
}
