import { Injectable } from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {OrderCreateDto} from '../shopping-cart/shopping-cart.component';
import {Product} from './httpclient.service';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private order = new BehaviorSubject<OrderCreateDto>(undefined);
  currentOrder = this.order.asObservable();

  private products = new BehaviorSubject<Product[]>(undefined);
  selectedProducts = this.products.asObservable();

  private cost = new BehaviorSubject<number>(0);
  currentCost = this.cost.asObservable();

  private product = new BehaviorSubject<boolean>(false);
  isProduct = this.product.asObservable();

  setIsProduct(isProduct: boolean) {
    this.product.next(isProduct);
  }
  setOrder(order: OrderCreateDto) {
    this.order.next(order);
  }

  setCost(cost: number) {
    this.cost.next(cost);
  }

  setProducts(products: Product[]) {
    this.products.next(products);
  }

  constructor() { }
}
