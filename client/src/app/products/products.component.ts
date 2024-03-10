import {Component, Input, OnInit} from '@angular/core';
import {HttpClientService, Product} from '../service/httpclient.service';
import {ProductService} from '../service/product.service';
import {Router} from '@angular/router';
import {OrderService} from '../service/order.service';
import {AuthenticationService} from '../service/authentication.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {
  public selectedProducts: Product[] = [];
  products: Product[];
  displayedColumns: string[] = this.loginService.isUserModerator()
    ? ['provider', 'name', 'price', 'edit', 'delete', 'discount'] : ['provider', 'name', 'price', 'cart'];

  @Input() error: string | null;

  constructor(private httpClientService: HttpClientService,
              private productsService: ProductService,
              private orderService: OrderService,
              private loginService: AuthenticationService,
              private router: Router) {

  }

  ngOnInit() {
    this.httpClientService
      .getProducts()
      .subscribe(response => {
        console.log(response);
        this.handleSuccessfulResponse(response);
      });
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

  subscribe(product: Product) {
    this.orderService.setSub(product);
    this.router.navigate(['/payment']);
  }

  editProduct(product: Product) {
    this.productsService.setProdId(product.id);
    this.router.navigate(['/edit-product']);
  }

  discount(sub: Product) {
    this.productsService.setProdId(sub.id);
    this.orderService.setTotal(sub.price);
    this.router.navigate(['discount']);
  }
}
