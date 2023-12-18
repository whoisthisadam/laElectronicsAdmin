import {Component, Input, OnInit} from '@angular/core';
import {HttpClientService, Product} from '../service/httpclient.service';
import {ShoppingCartComponent} from '../shopping-cart/shopping-cart.component';
import {ProductService} from '../service/product.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {
  public selectedProducts: Product[] = [];
  products: Product[];
  displayedColumns: string[] = ['category', 'brand', 'name', 'price', 'cart', 'edit', 'delete'];

  @Input() error: string | null;

  constructor(private httpClientService: HttpClientService,
              private productsService: ProductService,
              private router: Router) {

}

  ngOnInit() {
    this.httpClientService
      .getProducts()
      .subscribe(response => this.handleSuccessfulResponse(response));
  }

  handleSuccessfulResponse(response) {
    this.products = response;
  }

  deleteProduct(product: Product): void {
    this.httpClientService.deleteProduct(product.id).subscribe(data => {
      this.products = this.products.filter(u => u !== product);
    },
      () => {
      this.error = 'Unauthorized!';
    });
  }

  addToCart(product: Product) {
    this.productsService.selectedProduct.subscribe(value => this.selectedProducts = value);
    this.selectedProducts.push(product);
    this.productsService.setProduct(this.selectedProducts);
  }

  editProduct(product: Product) {
    this.productsService.setProdId(product.id);
    this.router.navigate(['/edit-product']);
  }
}
