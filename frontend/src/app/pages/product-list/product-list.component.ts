import { CurrencyPipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ZardButtonComponent } from '../../shared/components/button';
import { ZardCardComponent } from '../../shared/components/card';
import { ZardBadgeComponent } from '../../shared/components/badge';
import { ProductService, Product } from '../../services/product.service';

type SortKey = 'name' | 'price-asc' | 'price-desc' | 'newest' | 'stock';

@Component({
  selector: 'app-product-list',
  imports: [RouterLink, ZardButtonComponent, ZardCardComponent, ZardBadgeComponent, CurrencyPipe, FormsModule],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ProductListComponent {
  private readonly productService = inject(ProductService);

  readonly products = signal<Product[]>([]);
  readonly loading = signal(true);
  readonly error = signal('');
  readonly search = signal('');
  readonly sort = signal<SortKey>('name');

  readonly filtered = computed(() => {
    const q = this.search().toLowerCase();
    const items = this.products().filter(p =>
      p.name.toLowerCase().includes(q) || p.description?.toLowerCase().includes(q)
    );
    switch (this.sort()) {
      case 'price-asc': return items.sort((a, b) => a.price - b.price);
      case 'price-desc': return items.sort((a, b) => b.price - a.price);
      case 'newest': return items.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
      case 'stock': return items.sort((a, b) => b.quantity - a.quantity);
      default: return items.sort((a, b) => a.name.localeCompare(b.name));
    }
  });

  readonly stockLevel = (q: number) => q > 10 ? 'high' : q > 0 ? 'low' : 'none';
  readonly Math = Math;

  constructor() {
    this.loadProducts();
  }

  loadProducts(): void {
    this.loading.set(true);
    this.error.set('');

    this.productService.list().subscribe({
      next: (products) => {
        this.products.set(products);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Failed to load products');
        this.loading.set(false);
      },
    });
  }
}
