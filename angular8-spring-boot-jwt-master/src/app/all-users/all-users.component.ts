import {Component, Input, OnInit} from '@angular/core';
import {HttpClientService, Product, User} from '../service/httpclient.service';
import {ProductService} from '../service/product.service';
import {Router} from '@angular/router';
import {UserService} from '../service/user.service';

@Component({
  selector: 'app-all-users',
  templateUrl: './all-users.component.html',
  styleUrls: ['./all-users.component.css']
})
export class AllUsersComponent implements OnInit {

  users: User[];
  displayedColumns: string[] = ['firstName', 'lastName', 'email', 'more-info'];

  @Input() error: string | null;

  constructor(private httpClientService: HttpClientService,
              private userService: UserService,
              private router: Router) {

  }

  ngOnInit() {
    this.httpClientService
      .getUsers()
      .subscribe(response => this.handleSuccessfulResponse(response),
        error => {
        if (error.status === 403) {
          this.error = 'Forbidden! Only for admin!';
        }
        });
  }

  handleSuccessfulResponse(response) {
    this.users = response;
  }

  moreInfoRedirect(user: User) {
    this.userService.setUser(user);
    this.router.navigate(['/all-users/user']);
  }

}
