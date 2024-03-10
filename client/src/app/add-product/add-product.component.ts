import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Category, HttpClientService, Product} from '../service/httpclient.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
  styleUrls: ['./add-product.component.css']
})
export class AddProductComponent implements OnInit {

  form: FormGroup;

  product: Product;

  error: string;

  constructor(private formBuilder: FormBuilder,
              private httpClientService: HttpClientService,
              private router: Router) { }

  ngOnInit() {
    this.form = this.formBuilder.group  ({
      manufacturerName: [null, [Validators.required]],
      name: [null, [Validators.required]],
      price: [null, [Validators.required]]
    });
  }

  addProduct() {
    this.product = this.form.getRawValue();
    this.httpClientService.addProduct(this.product).subscribe(
      () => this.router.navigate(['/products']),
      error => {
        if (error.status === 403) {
          this.error = 'Forbidden!(Only for Admin)';
        }
      }
    );
  }

}
