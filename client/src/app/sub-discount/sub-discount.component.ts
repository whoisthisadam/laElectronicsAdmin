import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Discount, HttpClientService} from '../service/httpclient.service';
import {ProductService} from '../service/product.service';
import {OrderService} from '../service/order.service';
import {Router} from '@angular/router';


@Component({
  selector: 'app-sub-discount',
  templateUrl: './sub-discount.component.html',
  styleUrls: ['./sub-discount.component.css']
})
export class SubDiscountComponent implements OnInit {

  total: number;

  form: FormGroup;

  discountPercent?: number;

  id: number;

  constructor(private httpService: HttpClientService,
              private formBuilder: FormBuilder,
              private subService: ProductService,
              private orderService: OrderService,
              private router: Router) {
  }


  ngOnInit() {
    this.orderService.calculatedTotal.subscribe(
      value => {
        this.total = value;
      }
    );
    this.subService.currentId.subscribe(value => {
      this.id = value;
    });
    this.form = this.formBuilder.group({
      percent: [null, [Validators.required]]
    });
  }


  applyDiscount() {
    this.discountPercent = this.form.get('percent').value;
    this.total = Math.round(this.total - this.total / 100 * this.discountPercent);
    const discount = new Discount('SUB_DISCOUNT', this.discountPercent.toString(), this.id);
    return this.httpService.addSubDiscount(discount).subscribe(
      value => this.router.navigate(['/products'])
    );
  }

}
