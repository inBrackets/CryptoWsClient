import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ApiResponse, OrderHistoryResult} from '../model/dto';
import {environment} from '../../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class OrderHistoryService {

  apiUrl: string = `http://localhost:${environment.springBootPort}/private`;

  constructor(private http: HttpClient) { }

  getOrderHistory(): Observable<ApiResponse<OrderHistoryResult>> {
    return this.http.get<ApiResponse<OrderHistoryResult>>(`${this.apiUrl}/get-order-history`)
  }
}
