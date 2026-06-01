import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import Swal from 'sweetalert2';
import { ZardButtonComponent } from '../../shared/components/button';
import { ZardCardComponent } from '../../shared/components/card';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-product-form',
  imports: [FormsModule, RouterLink, ZardButtonComponent, ZardCardComponent],
  templateUrl: './product-form.component.html',
  styleUrl: './product-form.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ProductFormComponent {
  private readonly productService = inject(ProductService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  readonly isEdit = signal(false);
  readonly productId = signal<string | null>(null);
  readonly name = signal('');
  readonly description = signal('');
  readonly price = signal<number | null>(null);
  readonly quantity = signal<number | null>(null);
  readonly loading = signal(false);
  readonly submitting = signal(false);
  readonly error = signal('');
  readonly fieldErrors = signal<Record<string, string>>({});
  readonly descLength = computed(() => this.description().length);

  constructor() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit.set(true);
      this.productId.set(id);
      this.loadProduct(id);
    }
  }

  private loadProduct(id: string): void {
    this.loading.set(true);
    this.productService.getById(id).subscribe({
      next: (product) => {
        this.name.set(product.name);
        this.description.set(product.description);
        this.price.set(product.price);
        this.quantity.set(product.quantity);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Failed to load product');
        this.loading.set(false);
      },
    });
  }

  onSubmit(): void {
    this.fieldErrors.set({});
    this.error.set('');

    const errors: Record<string, string> = {};
    if (!this.name().trim()) {
      errors['name'] = 'Name is required';
    }
    if (this.price() === null || this.price() <= 0) {
      errors['price'] = 'Price must be positive';
    }
    if (this.quantity() !== null && this.quantity() < 0) {
      errors['quantity'] = 'Quantity must be non-negative';
    }

    if (Object.keys(errors).length > 0) {
      this.fieldErrors.set(errors);
      return;
    }

    this.submitting.set(true);

    const data = {
      name: this.name().trim(),
      description: this.description().trim(),
      price: this.price() as number,
      quantity: this.quantity() ?? 0,
    };

    const request = this.isEdit()
      ? this.productService.update(this.productId() as string, data)
      : this.productService.create(data);

    request.subscribe({
      next: () => {
        Swal.fire({
          title: 'Success!',
          text: `Product ${this.isEdit() ? 'updated' : 'created'} successfully.`,
          icon: 'success',
          timer: 1500,
          showConfirmButton: false,
        }).then(() => this.router.navigateByUrl('/products'));
      },
      error: () => {
        this.error.set('Failed to save product');
        this.submitting.set(false);
      },
    });
  }

  cancel(): void {
    this.router.navigateByUrl('/products');
  }
}
