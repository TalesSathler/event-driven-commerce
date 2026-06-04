import { Component, inject, AfterViewInit, PLATFORM_ID } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { ZardBadgeComponent } from '../../shared/components/badge';
import { ZardButtonComponent } from '../../shared/components/button';
import { ZardCardComponent } from '../../shared/components/card';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-home',
  imports: [
    RouterLink,
    ZardBadgeComponent,
    ZardButtonComponent,
    ZardCardComponent,
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent implements AfterViewInit {
  readonly auth = inject(AuthService);
  currentYear = new Date().getFullYear();
  menuOpen = false;
  readonly swaggerProduct = environment.swagger.productService;
  readonly swaggerInventory = environment.swagger.inventoryService;
  readonly swaggerAuth = environment.swagger.authService;

  readonly platformId = inject(PLATFORM_ID);

  ngAfterViewInit(): void {
    if (typeof window === 'undefined') return;

    const observer = new IntersectionObserver(
      (entries) => {
        for (const entry of entries) {
          if (entry.isIntersecting) {
            entry.target.classList.add('revealed');
            observer.unobserve(entry.target);
          }
        }
      },
      { threshold: 0.1 }
    );

    document.querySelectorAll('.reveal').forEach((el) => observer.observe(el));
  }
}
