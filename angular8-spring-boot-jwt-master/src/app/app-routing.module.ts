import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ProductsComponent } from './products/products.component';
// import { AddEmployeeComponent } from './add-product/add-product.component';
import { LoginComponent } from './login/login.component';
import { LogoutComponent } from './logout/logout.component';
import { AuthGaurdService } from './service/auth-gaurd.service';
import {ProfileComponent} from './profile/profile.component';
import {UpdateProfileComponent} from './update-profile/update-profile.component';
import {RegistrationComponent} from './registration/registration.component';
import {ShoppingCartComponent} from './shopping-cart/shopping-cart.component';
import {PaymentComponent} from './payment/payment.component';
import {AddProductComponent} from './add-product/add-product.component';
import {EditProductComponent} from './edit-product/edit-product.component';
import {AllUsersComponent} from './all-users/all-users.component';
import {MoreUserInfoComponent} from './more-user-info/more-user-info.component';

const routes: Routes = [
  { path: 'products', component: ProductsComponent, canActivate: [AuthGaurdService] },
  // { path: 'addemployee', component: AddEmployeeComponent,canActivate:[AuthGaurdService]},
  { path: 'login', component: LoginComponent },
  { path: 'logout', component: LogoutComponent, canActivate: [AuthGaurdService] },
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGaurdService] },
  { path: 'edit-profile', component: UpdateProfileComponent, canActivate: [AuthGaurdService] },
  { path: 'shopping-cart', component: ShoppingCartComponent, canActivate: [AuthGaurdService] },
  { path: 'sign-up', component: RegistrationComponent },
  { path: 'payment', component: PaymentComponent, canActivate: [AuthGaurdService] },
  { path: 'add-product', component: AddProductComponent, canActivate: [AuthGaurdService] },
  { path: 'edit-product', component: EditProductComponent, canActivate: [AuthGaurdService] },
  { path: 'all-users', component: AllUsersComponent, canActivate: [AuthGaurdService] },
  { path: 'all-users/user', component: MoreUserInfoComponent, canActivate: [AuthGaurdService] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
