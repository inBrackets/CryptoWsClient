import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ApiResponse, OrderHistoryResult} from '../model/dto';

@Injectable({
  providedIn: 'root'
})
export class OrderHistoryService {

  apiUrl: string = 'http://localhost:8080/private';

  constructor(private http: HttpClient) { }

  getOrderHistory(): Observable<ApiResponse<OrderHistoryResult>> {
    return this.http.get<ApiResponse<OrderHistoryResult>>(`${this.apiUrl}/get-order-history`)
  }
}
