import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { ZardButtonComponent } from '../../shared/components/button';
import { ZardCardComponent } from '../../shared/components/card';

@Component({
  selector: 'app-register',
  imports: [FormsModule, RouterLink, ZardButtonComponent, ZardCardComponent],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class RegisterComponent {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  readonly name = signal('');
  readonly email = signal('');
  readonly password = signal('');
  readonly loading = signal(false);
  readonly error = signal('');

  async onSubmit(): Promise<void> {
    if (!this.name() || !this.email() || !this.password()) {
      this.error.set('All fields are required');
      return;
    }

    this.loading.set(true);
    this.error.set('');

    this.auth.register({ name: this.name(), email: this.email(), password: this.password() }).subscribe({
      next: () => this.router.navigateByUrl('/'),
      error: () => {
        this.error.set('Registration failed. Please try again.');
        this.loading.set(false);
      },
    });
  }
}
