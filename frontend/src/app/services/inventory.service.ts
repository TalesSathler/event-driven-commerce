import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Inventory {
  productId: string;
  quantity: number;
}

@Injectable({ providedIn: 'root' })
export class InventoryService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.api.inventoryService}/inventory`;

  getByProductId(productId: string): Observable<Inventory> {
    return this.http.get<Inventory>(`${this.apiUrl}/${productId}`);
  }

  update(productId: string, quantity: number): Observable<Inventory> {
    return this.http.put<Inventory>(`${this.apiUrl}/${productId}`, { quantity });
  }
}
