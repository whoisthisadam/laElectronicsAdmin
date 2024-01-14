import {Component, OnInit} from '@angular/core';
import {OrderService} from '../service/order.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClientService} from '../service/httpclient.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-approve-order',
  templateUrl: './approve-order.component.html',
  styleUrls: ['./approve-order.component.css']
})
export class ApproveOrderComponent implements OnInit {

  total: number;

  form: FormGroup;

  discountPercent?: number;

  isFormVisible: boolean;

  id: number;

  constructor(private orderService: OrderService,
              private formBuilder: FormBuilder,
              private httpService: HttpClientService,
              private router: Router) {
  }

  ngOnInit() {
    this.orderService.selectedId.subscribe(value => this.id = value);
    this.isFormVisible = false;
    this.orderService.calculatedTotal.subscribe(
      value => {
        this.total = value;
      }
    );
    this.form = this.formBuilder.group({
      percent: [null, [Validators.required]]
    });
  }

  applyDiscount() {
    this.discountPercent = this.form.get('percent').value;
    this.total = Math.round(this.total - this.total / 100 * this.discountPercent);
    this.isFormVisible = false;
  }

  approveOrder(id: number) {
    this.httpService.approveOrder(id, this.discountPercent)
      .subscribe(() => {
        window.location.reload();
      });
  }

  setFormVisible(isFormVisible: boolean) {
    this.isFormVisible = isFormVisible;
  }
}
