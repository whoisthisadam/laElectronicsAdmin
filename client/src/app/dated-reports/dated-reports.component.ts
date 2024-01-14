import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClientService, Order, Product} from '../service/httpclient.service';
import {ProductService} from '../service/product.service';
import {Router} from '@angular/router';
import {OrderService} from '../service/order.service';


export class ApplyForm {
  constructor(
    public fromDay: string,
    public fromMonth: string,
    public fromYear: string,
    public toDay: string,
    public toMonth: string,
    public toYear: string) {
  }
}

@Component({
  selector: 'app-dated-reports',
  templateUrl: './dated-reports.component.html',
  styleUrls: ['./dated-reports.component.css'],
})

export class DatedReportsComponent implements OnInit {
  form: FormGroup;

  product: Product;

  error: string;

  applyForm: ApplyForm;

  isDatedOrders: boolean;

  isDatedProducts: boolean;

  isOrderProducts: boolean;

  products: Product[];

  orders: Order[];

  type: string;

  displayedColumnsOrders: string[] = ['email', 'total', 'products'];

  displayedColumnsProducts: string[] = ['category', 'brand', 'name', 'price'];


  constructor(private formBuilder: FormBuilder,
              private httpClientService: HttpClientService,
              private productService: ProductService,
              private orderService: OrderService,
              private router: Router) {
  }

  ngOnInit() {
    this.isOrderProducts = false;
    this.orderService.selectedType.subscribe(value => this.type = value);
    this.form = this.formBuilder.group({
      fromDay: [null, [Validators.required]],
      fromMonth: [null, [Validators.required]],
      fromYear: [null, [Validators.required]],
      toDay: [null, [Validators.required]],
      toMonth: [null, [Validators.required]],
      toYear: [null, [Validators.required]]
    });
    this.orderService.isProduct.subscribe(value => this.isOrderProducts = value);
  }

  fetchData() {
    this.applyForm = this.form.getRawValue();
    if (this.type === 'orders') {
      this.httpClientService.getDatedOrders(this.applyForm).subscribe(
        value => {
          this.orders = value;
          this.isDatedOrders = true;
        }
      )
      ;
    } else if (this.type === 'products') {
      this.httpClientService.getDatedProducts(this.applyForm).subscribe(
        value => {
          this.isOrderProducts = false;
          this.products = value;
          this.isDatedOrders = false;
          this.isDatedProducts = true;
        }
      );
    }
  }

  toProducts(order: Order) {
    this.isOrderProducts = true;
    this.orderService.setProducts(order.products);
  }


}
