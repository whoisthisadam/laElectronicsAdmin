import {Component, OnInit} from '@angular/core';
import {HttpClientService, Product} from '../service/httpclient.service';

@Component({
  selector: 'app-my-subscriptions',
  templateUrl: './my-subscriptions.component.html',
  styleUrls: ['./my-subscriptions.component.css']
})
export class MySubscriptionsComponent implements OnInit {

  displayedColumns: string[] = ['brand', 'name', 'price', 'delete'];

  products: Product[];

  totalPrice: number;

  constructor(private httpService: HttpClientService) {
  }

  ngOnInit() {
    this.httpService.getSubs(sessionStorage.getItem('username')).subscribe(
      value => {
        this.products = value;
        this.calculateTotalPrice();
      }
    );
  }


  calculateTotalPrice() {
    let sum = 0;
    this.products.forEach(x => {
      sum += x.price;
    });
    this.totalPrice = sum;
  }


  removeSub(product: Product) {
    this.httpService.removeUserSub(product.id).subscribe(
      value => {
        this.products = this.products.filter(item => item !== product);
        this.calculateTotalPrice();
      }
    );
  }

}
