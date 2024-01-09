import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Category, HttpClientService, Product} from '../service/httpclient.service';
import {Router} from '@angular/router';
import {ProductService} from '../service/product.service';

@Component({
  selector: 'app-edit-product',
  templateUrl: './edit-product.component.html',
  styleUrls: ['./edit-product.component.css']
})
export class EditProductComponent implements OnInit {

  form: FormGroup;

  product: Product;

  error: string;

  constructor(private formBuilder: FormBuilder,
              private httpClientService: HttpClientService,
              private productService: ProductService,
              private router: Router) { }

  ngOnInit() {
    this.form = this.formBuilder.group  ({
      manufacturerName: [null, [Validators.required]],
      name: [null, [Validators.required]],
      price: [null, [Validators.required]]
    });
  }

  edit() {
    this.product = this.form.getRawValue();
    this.productService.currentId.subscribe(value => this.product.id = value);
    this.httpClientService.editProduct(this.product).subscribe(
      () => this.router.navigate(['/products']),
      error => {
        if (error.status === 403) {
          this.error = 'Forbidden!(Only for Admin)';
        }
      }
    );
  }


}
