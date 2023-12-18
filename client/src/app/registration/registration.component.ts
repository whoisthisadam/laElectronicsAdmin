import { Component, OnInit } from '@angular/core';
import {Credentials, HttpClientService, User} from '../service/httpclient.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';


export class UserCreateDto {
  constructor(
    public credentials: Credentials,
    public firstName: string,
    public lastName: string,
    public mobilePhone: string,
    public email: string,
    public address: AddressCreateDto,
  ) {
  }
}

export class AddressCreateDto {
  constructor(
    public lineOne: string,
    public lineTwo: string,
    public city: string,
    public province: string,
    public postCode: string,
    public country: string
  ) {
  }
}
@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  form: FormGroup;

  user: UserCreateDto;


  constructor(
    private formBuilder: FormBuilder,
    private httpClientService: HttpClientService,
    private router: Router
  ) { }

  ngOnInit() {
    this.form = this.formBuilder.group  ({
      firstName: [null, [Validators.required]],
      lastName: [null, [Validators.required]],
      mobilePhone: [null, [Validators.required]],
      email: [null, [Validators.required]],
      login: [null, [Validators.required]],
      password: [null, [Validators.required]],
      lineOne: [null, [Validators.required]],
      lineTwo: [null, [Validators.required]],
      city: [null, [Validators.required]],
      province: [null, [Validators.required]],
      country: [null, [Validators.required]],
      postCode: [null, [Validators.required]],
    });
  }

  register() {
    this.user = this.form.getRawValue();
    this.user.address = new AddressCreateDto(
      this.form.get('lineOne').value,
      this.form.get('lineTwo').value,
      this.form.get('city').value,
      this.form.get('province').value,
      this.form.get('country').value,
      this.form.get('postCode').value
    );
    this.user.credentials = new Credentials(
      this.form.get('login').value,
      this.form.get('password').value
    );
    this.httpClientService.registerUser(this.user).subscribe(
      () => {
        this.router.navigate(['/login']);
      }
    );
  }

}
