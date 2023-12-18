import { Injectable } from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {Product} from './httpclient.service';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private product = new BehaviorSubject<Product[]>([]);
  selectedProduct = this.product.asObservable();

  private prodId = new BehaviorSubject<number>(0);
  currentId = this.prodId.asObservable();

  constructor() { }

  setProduct(product: any) {
    this.product.next(product);
  }

  setProdId(id: number) {
    this.prodId.next(id);
  }
}
