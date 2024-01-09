import { Component, OnInit } from '@angular/core';
import {HttpClientService, Order, Product} from '../service/httpclient.service';
import {ProductService} from '../service/product.service';
import {Router} from '@angular/router';
import {OrderService} from '../service/order.service';

@Component({
  selector: 'app-pending-orders',
  templateUrl: './pending-orders.component.html',
  styleUrls: ['./pending-orders.component.css']
})
export class PendingOrdersComponent implements OnInit {

  orders: Order[];
  displayedColumns: string[] = ['email', 'total', 'products'];
  isProduct: boolean=false;

  constructor(private httpClientService: HttpClientService,
              private orderService: OrderService,
              private router: Router) {

  }

  ngOnInit() {
    this.httpClientService
      .getPendingOrders()
      .subscribe(response => {
        this.handleSuccessfulResponse(response);
      });
    this.orderService.isProduct.subscribe(value => this.isProduct = value);
  }

  handleSuccessfulResponse(response) {
    this.orders = response;
  }
  toProducts(order: Order) {
    this.isProduct = true;
    this.orderService.setProducts(order.products);
  }

}
