import { CurrencyPipe, DatePipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import Swal from 'sweetalert2';
import { ZardButtonComponent } from '../../shared/components/button';
import { ZardCardComponent } from '../../shared/components/card';
import { ZardBadgeComponent } from '../../shared/components/badge';
import { ProductService, Product } from '../../services/product.service';

@Component({
  selector: 'app-product-detail',
  imports: [RouterLink, ZardButtonComponent, ZardCardComponent, ZardBadgeComponent, CurrencyPipe, DatePipe],
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ProductDetailComponent {
  private readonly productService = inject(ProductService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  readonly product = signal<Product | null>(null);
  readonly loading = signal(true);
  readonly error = signal('');
  readonly notFound = signal(false);
  readonly deleting = signal(false);
  readonly stockLevel = (q: number) => q > 10 ? 'high' : q > 0 ? 'low' : 'none';
  readonly Math = Math;
  private readonly productId = this.route.snapshot.paramMap.get('id') ?? '';

  constructor() {
    if (this.productId) {
      this.loadProduct();
    }
  }

  loadProduct(): void {
    const id = this.productId;
    this.loading.set(true);
    this.error.set('');

    this.productService.getById(id).subscribe({
      next: (product) => {
        this.product.set(product);
        this.loading.set(false);
      },
      error: (err) => {
        if (err.status === 404) {
          this.notFound.set(true);
        } else {
          this.error.set('Failed to load product');
        }
        this.loading.set(false);
      },
    });
  }

  deleteProduct(): void {
    const product = this.product();
    if (!product) return;

    Swal.fire({
      title: 'Delete product?',
      text: `Are you sure you want to delete "${product.name}"? This action cannot be undone.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#dc2626',
      cancelButtonColor: '#6b7280',
      confirmButtonText: 'Yes, delete it',
      cancelButtonText: 'Cancel',
    }).then((result) => {
      if (!result.isConfirmed) return;

      this.deleting.set(true);
      this.productService.delete(product.id).subscribe({
        next: () => {
          Swal.fire({
            title: 'Deleted!',
            text: 'Product has been deleted.',
            icon: 'success',
            timer: 1500,
            showConfirmButton: false,
          }).then(() => this.router.navigateByUrl('/products'));
        },
        error: () => {
          this.error.set('Failed to delete product');
          this.deleting.set(false);
        },
      });
    });
  }
}
