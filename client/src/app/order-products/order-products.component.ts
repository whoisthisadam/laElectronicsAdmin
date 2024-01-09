import { Component, OnInit } from '@angular/core';
import {Product} from '../service/httpclient.service';
import {OrderService} from '../service/order.service';

@Component({
  selector: 'app-order-products',
  templateUrl: './order-products.component.html',
  styleUrls: ['./order-products.component.css']
})
export class OrderProductsComponent implements OnInit {

  products: Product[];

  displayedColumns: string[] = ['category', 'brand', 'name', 'price'];

  constructor(private orderService: OrderService) { }

  ngOnInit() {
    this.orderService.selectedProducts
      .subscribe(value => this.products = value);
  }

  back() {
    this.orderService.setIsProduct(false);
  }

}
