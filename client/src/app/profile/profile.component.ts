import { Component, OnInit } from '@angular/core';
import {HttpClientService, User} from '../service/httpclient.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  user: User;

  constructor(private httpClientService: HttpClientService,
              private router: Router) { }

  ngOnInit() {
    this.httpClientService.getProfile().subscribe(
      response => {
        this.handleSuccessfulResponse(response);
        sessionStorage.setItem('userId', String(this.user.id));
      }
    );
  }


  handleSuccessfulResponse(response) {
    this.user = response;
  }

  delete() {
    this.httpClientService.deleteAccount().subscribe(
      () => {
        this.router.navigate(['/logout']);
      }
    );
  }

}
