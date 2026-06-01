import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { tap } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const token = auth.token;

  let cloned = req;
  if (token) {
    cloned = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` },
    });
  }

  return next(cloned).pipe(
    tap({
      error: (err) => {
        if (err instanceof HttpErrorResponse && err.status === 401) {
          auth.logout();
          router.navigateByUrl('/login');
        }
      },
    }),
  );
};
