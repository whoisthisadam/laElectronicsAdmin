import { Component, OnInit } from '@angular/core';
import {OrderService} from '../service/order.service';
import {OrderCreateDto} from '../shopping-cart/shopping-cart.component';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClientService} from '../service/httpclient.service';
import {Router} from '@angular/router';

export class PaymentCreateDto {
  constructor(public provider: string) {
  }
}
@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})
export class PaymentComponent implements OnInit {

  form: FormGroup;

  cost: number;

  order: OrderCreateDto;

  constructor(private orderService: OrderService,
              private formBuilder: FormBuilder,
              private httpClientService: HttpClientService,
              private router: Router) { }

  ngOnInit() {

    this.form = this.formBuilder.group  ({
      cardNumber: [null, [Validators.required]],
      cardHolder: [null, [Validators.required]],
      cvv: [null, [Validators.required]]
    });

    this.orderService.currentOrder.subscribe(
      value => this.order = value
    );

    this.orderService.currentCost.subscribe(
      value => this.cost = value
    );
  }


  payment() {
    this.httpClientService.completeOrder(this.order).subscribe(
      () => this.router.navigate(['/'])
    );
  }
}
