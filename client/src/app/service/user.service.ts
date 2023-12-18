import { Injectable } from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {User} from './httpclient.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private user = new BehaviorSubject<User>(undefined);

  selectedUser = this.user.asObservable();

  setUser(user: User) {
    this.user.next(user);
  }

  constructor() { }
}
