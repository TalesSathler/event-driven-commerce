import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink, RouterOutlet, NavigationEnd } from '@angular/router';
import { AuthService } from './services/auth.service';
import { ZardButtonComponent } from './shared/components/button';
import { filter } from 'rxjs';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, ZardButtonComponent],
  template: `
    <div class="min-h-screen bg-background">
      @if (auth.isAuthenticated && !isHome()) {
        <nav class="sticky top-0 z-50 border-b bg-background/80 backdrop-blur-sm">
          <div class="mx-auto flex max-w-6xl items-center justify-between px-4 py-3">
            <a routerLink="/" class="text-sm font-semibold tracking-tight">Event-Driven Commerce</a>
            <div class="flex items-center gap-4">
              <span class="text-sm text-muted-foreground">{{ auth.name }}</span>
              <a routerLink="/products" class="text-sm font-medium text-muted-foreground hover:text-foreground transition-colors">Products</a>
              <button z-button zType="ghost" (click)="logout()">Sign Out</button>
            </div>
          </div>
        </nav>
      }
      <router-outlet />
    </div>
  `,
})
export class App {
  readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  readonly isHome = signal(true);

  constructor() {
    this.router.events.pipe(filter(e => e instanceof NavigationEnd)).subscribe(e => {
      this.isHome.set((e as NavigationEnd).url === '/');
    });
  }

  logout(): void {
    this.auth.logout();
    this.router.navigateByUrl('/');
  }
}
