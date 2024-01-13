import {Component, OnInit} from '@angular/core';
import {HttpClientService, Order} from '../service/httpclient.service';
import {Router} from '@angular/router';
import {OrderService} from '../service/order.service';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {ApproveOrderComponent} from '../approve-order/approve-order.component';

@Component({
  selector: 'app-pending-orders',
  templateUrl: './pending-orders.component.html',
  styleUrls: ['./pending-orders.component.css']
})
export class PendingOrdersComponent implements OnInit {

  orders: Order[];
  displayedColumns: string[] = ['email', 'total', 'products', 'approve'];
  isProduct: boolean;

  constructor(private httpClientService: HttpClientService,
              private orderService: OrderService,
              private dialog: MatDialog,
              private router: Router) {

  }

  ngOnInit() {
    this.httpClientService
      .getPendingOrders()
      .subscribe(response => {
        this.handleSuccessfulResponse(response);
      });
    this.orderService.isProduct.subscribe(value => this.isProduct = value);
  }

  handleSuccessfulResponse(response) {
    this.orders = response;
  }

  toProducts(order: Order) {
    this.isProduct = true;
    this.orderService.setProducts(order.products);
  }

  openDialog = (order: Order) => {
    this.orderService.setTotal(order.total);
    this.orderService.setId(order.id);
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '1800px';
    dialogConfig.height = '756px';
    dialogConfig.backdropClass = 'popupBackdropClass';
    this.dialog.open(ApproveOrderComponent, dialogConfig);
  };


}
