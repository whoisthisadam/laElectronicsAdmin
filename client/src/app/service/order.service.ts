import {Injectable} from '@angular/core';
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

  private datedProduct = new BehaviorSubject<boolean>(false);
  isDatedProduct = this.datedProduct.asObservable();

  private total = new BehaviorSubject<number>(0);
  calculatedTotal = this.total.asObservable();

  private id = new BehaviorSubject<number>(0);
  selectedId = this.id.asObservable();

  private type = new BehaviorSubject<string>(null);
  selectedType = this.type.asObservable();

  setIsProduct(isProduct: boolean) {
    this.product.next(isProduct);
  }

  setIsDatedProduct(isProduct: boolean) {
    this.datedProduct.next(isProduct);
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

  setTotal(total: number) {
    this.total.next(total);
  }

  setId(id: number) {
    this.id.next(id);
  }

  setType(type: string) {
    this.type.next(type);
  }

  constructor() {
  }
}
