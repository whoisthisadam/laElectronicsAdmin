import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {UserCreateDto} from '../registration/registration.component';
import {OrderCreateDto} from '../shopping-cart/shopping-cart.component';
import {environment} from '../../environments/environment';
import * as url from 'url';

export class Order {
  constructor(
    public id: number,
    public userEmail: string,
    public userId: number,
    public total: number,
    public products: Product[]
  ) {
  }
}

export class Product {
  constructor(
    public id: number,
    public category: Category,
    public name: string,
    public price: number,
    public manufacturerName: string
  ) {
  }
}

export class User {
  constructor(
    public id: number,
    public firstName: string,
    public lastName: string,
    public mobilePhone: string,
    public email: string,
    public login: string,
    public roleName: string,
    public credentials: Credentials
  ) {
  }
}

export class Credentials {
  constructor(
    public login: string,
    public password: string
  ) {
  }
}

export class Category {
  constructor(
    public name: string
  ) {
  }
}

@Injectable({
  providedIn: 'root'
})
export class HttpClientService {
  constructor(private httpClient: HttpClient) {
  }


  getProducts() {
    return this.httpClient.get<Product[]>(environment.baseUrl + '/data/products');
  }

  public deleteProduct(id: number) {
    return this.httpClient.patch(
      environment.baseUrl + '/data/products/delete?ID=' + id.toString(), {}
    );
  }


  getProfile() {
    return this.httpClient.get(environment.baseUrl + '/data/users/user?email='
      + sessionStorage.getItem('username'));
  }

  updateProfile(user: User) {
    return this.httpClient.patch(
      environment.baseUrl + '/data/users/update?id='
      + sessionStorage.getItem('userId'),
      user);
  }

  deleteAccount() {
    return this.httpClient.patch(environment.baseUrl + '/data/users/delete?id='
      + sessionStorage.getItem('userId'), {}
    );
  }

  registerUser(user: UserCreateDto) {
    return this.httpClient.post(environment.baseUrl + '/registration', user);
  }

  completeOrder(order: OrderCreateDto) {
    return this.httpClient.post(environment.baseUrl + '/data/orders', order);
  }

  addProduct(product: Product) {
    return this.httpClient.post(environment.baseUrl + '/data/products', product);
  }

  editProduct(product: Product) {
    return this.httpClient.patch(environment.baseUrl + '/data/products/update?id=' + product.id, product);
  }

  getUsers() {
    return this.httpClient.get<User[]>(environment.baseUrl + '/data/users/all');
  }

  deleteUser(id: string) {
    return this.httpClient.patch(environment.baseUrl + '/data/users/delete?id='
      + id, {}
    );
  }

  getPendingOrders() {
    return this.httpClient.get<Order[]>(environment.baseUrl + '/data/orders/pending');
  }

  approveOrder(id: number, discount?: number) {
    let reqUrl = environment.baseUrl + '/data/orders/approve/' + id;
    if (discount != null) {
      reqUrl = reqUrl + '?discount=' + discount;
    }
    return this.httpClient.patch(reqUrl, {});
  }

}
