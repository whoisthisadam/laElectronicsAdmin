import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Credentials, HttpClientService, User} from '../service/httpclient.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-update-profile',
  templateUrl: './update-profile.component.html',
  styleUrls: ['./update-profile.component.css']
})
export class UpdateProfileComponent implements OnInit {

  form: FormGroup;

  user: User;

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
    });
  }

  update() {
    this.user = this.form.getRawValue();
    this.user.credentials = new Credentials(this.user.login, null);
    this.user.email = sessionStorage.getItem('username');
    this.httpClientService.updateProfile(this.user).subscribe(
      () => {
        this.router.navigate(['/profile']);
      }
    );
  }

}
