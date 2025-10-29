import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../../environments/environment';
import {Instrument} from '../model/dto';

@Injectable({
  providedIn: 'root'
})
export class InstrumentRestService {

  private apiUrl = `http://localhost:${environment.springBootPort}/api/instrument`;

  constructor(private http: HttpClient) {}

  getAllInstruments(): Observable<Instrument[]> {
    return this.http.get<Instrument[]>(`${this.apiUrl}/get-all`);
  }

  getCcyPairsInstruments(): Observable<Instrument[]> {
    return this.http.get<Instrument[]>(`${this.apiUrl}/get-ccy-pairs`);
  }


}
