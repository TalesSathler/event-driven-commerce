import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { ZardButtonComponent } from '../../shared/components/button';
import { ZardCardComponent } from '../../shared/components/card';

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink, ZardButtonComponent, ZardCardComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LoginComponent {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  readonly email = signal('');
  readonly password = signal('');
  readonly loading = signal(false);
  readonly error = signal('');

  async onSubmit(): Promise<void> {
    if (!this.email() || !this.password()) {
      this.error.set('Email and password are required');
      return;
    }

    this.loading.set(true);
    this.error.set('');

    this.auth.login({ email: this.email(), password: this.password() }).subscribe({
      next: () => this.router.navigateByUrl('/'),
      error: () => {
        this.error.set('Invalid email or password');
        this.loading.set(false);
      },
    });
  }
}
