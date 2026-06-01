import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  name: string;
  email: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.api.productService}/auth`;

  private readonly TOKEN_KEY = 'edc_token';
  private readonly NAME_KEY = 'edc_name';
  private readonly EMAIL_KEY = 'edc_email';

  get token(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  get name(): string | null {
    return localStorage.getItem(this.NAME_KEY);
  }

  get email(): string | null {
    return localStorage.getItem(this.EMAIL_KEY);
  }

  get isAuthenticated(): boolean {
    return !!this.token;
  }

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(res => this.setSession(res)),
    );
  }

  register(data: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, data).pipe(
      tap(res => this.setSession(res)),
    );
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.NAME_KEY);
    localStorage.removeItem(this.EMAIL_KEY);
  }

  private setSession(res: AuthResponse): void {
    localStorage.setItem(this.TOKEN_KEY, res.token);
    localStorage.setItem(this.NAME_KEY, res.name);
    localStorage.setItem(this.EMAIL_KEY, res.email);
  }
}
