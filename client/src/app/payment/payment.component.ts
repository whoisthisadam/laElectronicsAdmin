import {Component, OnInit} from '@angular/core';
import {OrderService} from '../service/order.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClientService, Product} from '../service/httpclient.service';
import {Router} from '@angular/router';

export class PaymentCreateDto {
  constructor(public provider: string,
              public amount: number) {
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

  sub: Product;

  constructor(private orderService: OrderService,
              private formBuilder: FormBuilder,
              private httpClientService: HttpClientService,
              private router: Router) {
  }

  ngOnInit() {

    this.form = this.formBuilder.group({
      cardNumber: [null, [Validators.required]],
      cardHolder: [null, [Validators.required]],
      cvv: [null, [Validators.required]]
    });
    //
    // this.orderService.currentOrder.subscribe(
    //   value => this.order = value
    // );

    this.orderService.selectedSub.subscribe(
      value => {
        this.sub = value;
        this.cost = value.price;
      }
    );
  }


  payment() {
    const payment = new PaymentCreateDto('BANK_CARD', this.cost);
    this.httpClientService.addSubscription(this.sub.id, sessionStorage.getItem('username'), payment).subscribe(
      () => this.router.navigate(['/'])
    );
  }
}
