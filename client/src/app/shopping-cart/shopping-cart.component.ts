import {Component, OnInit} from '@angular/core';
import {Product} from '../service/httpclient.service';
import {ProductService} from '../service/product.service';
import {PaymentCreateDto} from '../payment/payment.component';
import {OrderService} from '../service/order.service';
import {Router} from '@angular/router';

export class OrderCreateDto {
  constructor(
    public userId: number,
    public payment: PaymentCreateDto,
    public products: number[],
  ) {
  }
}

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.css']
})
export class ShoppingCartComponent implements OnInit {

  products: Product[];

  totalPrice: number;

  order: OrderCreateDto;

  displayedColumns: string[] = ['category', 'brand', 'name', 'price', 'delete'];

  constructor(private productService: ProductService,
              private orderService: OrderService,
              private router: Router) { }


  deleteFromCart(product: Product) {
    this.products = this.products.filter(p => p !== product);
    this.productService.setProduct(this.products);
    this.calculateTotalPrice();
  }

  ngOnInit(): void {
    this.productService.selectedProduct.subscribe(
      (value) => {
        this.products = value;
      }
    );
    this.calculateTotalPrice();
  }

  calculateTotalPrice() {
    let sum = 0;
    this.products.forEach(x => {
      sum += x.price;
    });
    this.totalPrice = sum;
  }

  submitOrder() {
    const numbers = [];
    this.products.forEach(x => {
      numbers.push(x.id);
    });

    this.order = new OrderCreateDto(
      parseInt(sessionStorage.getItem('userId'), 10),
      new PaymentCreateDto('BANK_CARD'),
      numbers
    );
    this.orderService.setOrder(this.order);
    this.orderService.setCost(this.totalPrice);

    this.router.navigate(['/payment']);
  }

}
