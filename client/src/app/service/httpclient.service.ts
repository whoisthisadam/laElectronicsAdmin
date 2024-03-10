import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {UserCreateDto} from '../registration/registration.component';
import {environment} from '../../environments/environment';
import * as url from 'url';
import {ApplyForm} from '../dated-reports/dated-reports.component';
import {PaymentCreateDto} from '../payment/payment.component';

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

export class Manufacturer {
  constructor(
    public name: string,
    public productsNumber: number
  ) {
  }
}

export class Product {
  constructor(
    public id: number,
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

export class Discount {
  constructor(
    public name: string,
    public discountPercent: string,
    public subId: number
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
    return this.httpClient.get(environment.baseUrl + '/data/users/user?login='
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

  // completeOrder(order: OrderCreateDto) {
  //   return this.httpClient.post(environment.baseUrl + '/data/orders', order);
  // }

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

  getManufacturersByProducts() {
    return this.httpClient.get<Manufacturer[]>(environment.baseUrl + '/data/manufacturers', {});
  }

  getDatedOrders(applyForm: ApplyForm) {
    const fromDate = applyForm.fromYear + '-' + applyForm.fromMonth + '-' + applyForm.fromDay + ' 00:00:00';
    const toDate = applyForm.toYear + '-' + applyForm.toMonth + '-' + applyForm.toDay + ' 23:59:59';
    return this.httpClient.get<Order[]>(environment.baseUrl + '/data/orders/dated?from=' + fromDate + '&to=' + toDate);
  }

  getDatedProducts(applyForm: ApplyForm) {
    const fromDate = applyForm.fromYear + '-' + applyForm.fromMonth + '-' + applyForm.fromDay + ' 00:00:00';
    const toDate = applyForm.toYear + '-' + applyForm.toMonth + '-' + applyForm.toDay + ' 23:59:59';
    return this.httpClient.get<Product[]>(environment.baseUrl + '/data/products/dated?from=' + fromDate + '&to=' + toDate);
  }

  addSubscription(subId: number, userLogin: string, dto: PaymentCreateDto) {
    return this.httpClient.post(`${environment.baseUrl}/data/products/user?subID=${subId}&userLogin=${userLogin}`, dto);
  }

  getSubs(userName: string) {
    return this.httpClient.get<Product[]>(`${environment.baseUrl}/data/products/subscriptions?userName=${userName}`);
  }

  removeUserSub(subId: number) {
    return this.httpClient.delete(`${environment.baseUrl}/data/products/subscriptions/remove?subId=${subId}`);
  }

  addSubDiscount(discount: Discount) {
    return this.httpClient.post(`${environment.baseUrl}/data/discounts`, discount);
  }

}
