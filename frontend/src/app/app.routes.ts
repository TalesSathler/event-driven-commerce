import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import { guestGuard } from './guards/guest.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent),
  },
  {
    path: 'login',
    canActivate: [guestGuard],
    loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent),
  },
  {
    path: 'register',
    canActivate: [guestGuard],
    loadComponent: () => import('./pages/register/register.component').then(m => m.RegisterComponent),
  },
  {
    path: 'products',
    canActivate: [authGuard],
    children: [
      {
        path: '',
        loadComponent: () => import('./pages/product-list/product-list.component').then(m => m.ProductListComponent),
      },
      {
        path: 'new',
        loadComponent: () => import('./pages/product-form/product-form.component').then(m => m.ProductFormComponent),
      },
      {
        path: ':id',
        loadComponent: () => import('./pages/product-detail/product-detail.component').then(m => m.ProductDetailComponent),
      },
      {
        path: ':id/edit',
        loadComponent: () => import('./pages/product-form/product-form.component').then(m => m.ProductFormComponent),
      },
    ],
  },
  {
    path: '**',
    redirectTo: '',
  },
];
