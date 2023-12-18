import { Injectable } from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {OrderCreateDto} from '../shopping-cart/shopping-cart.component';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private order = new BehaviorSubject<OrderCreateDto>(undefined);
  currentOrder = this.order.asObservable();

  private cost = new BehaviorSubject<number>(0);
  currentCost = this.cost.asObservable();

  setOrder(order: OrderCreateDto) {
    this.order.next(order);
  }

  setCost(cost: number) {
    this.cost.next(cost);
  }

  constructor() { }
}
