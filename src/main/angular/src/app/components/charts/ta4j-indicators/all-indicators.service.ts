import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../../environments/environment';
import {CandlestickWithInstrumentName} from '../../../pages/rest-channels/candlestick/model/dto';

@Injectable({
  providedIn: 'root'
})
export class AllIndicatorsService {

  constructor(private http: HttpClient) {
  }

  apiUrl: string = `http://localhost:${environment.springBootPort}`

  getRsiSeries(timeframe: string, barCount: number): Observable<{ timestamp: number; rsi: number }[]> {
    return this.http.get<{ timestamp: number; rsi: number }[]>(
      `${this.apiUrl}/api/candlestick/get-rsi?instrument_name=CRO_USD&time_frame=${timeframe}&barCount=${barCount}`
    );
  }

    getCandleSticksSeries(timeframe: string): Observable<CandlestickWithInstrumentName[]> {
      return this.http.get<CandlestickWithInstrumentName[]>(
        `${this.apiUrl}/api/candlestick/get-all?instrument_name=CRO_USD&time_frame=${timeframe}`
      );
  }

}
