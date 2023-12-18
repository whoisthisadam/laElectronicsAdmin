import {Component, OnInit} from '@angular/core';
import {HttpClientService, User} from '../service/httpclient.service';
import {UserService} from '../service/user.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-more-user-info',
  templateUrl: './more-user-info.component.html',
  styleUrls: ['./more-user-info.component.css']
})
export class MoreUserInfoComponent implements OnInit {

  user: User;

  constructor(private httpClientService: HttpClientService,
              private userService: UserService,
              private router: Router) { }

    ngOnInit() {
      this.userService.selectedUser
        .subscribe(value => this.user = value);
    }

  delete() {
    this.httpClientService.deleteUser(this.user.id.toString()).subscribe(
      () => this.router.navigate(['/all-users'])
    );
  }

}
