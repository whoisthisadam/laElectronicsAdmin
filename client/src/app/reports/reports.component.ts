import {Component, OnInit} from '@angular/core';
import {HttpClientService, Manufacturer, Order} from '../service/httpclient.service';
import {Timestamp} from 'rxjs';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {ApproveOrderComponent} from '../approve-order/approve-order.component';
import {DatedReportsComponent} from '../dated-reports/dated-reports.component';
import {OrderService} from '../service/order.service';

@Component({
  selector: 'app-reports',
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.css']
})
export class ReportsComponent implements OnInit {

  manufacturers: Manufacturer[];

  isManufacturers = false;

  displayedColumns: string[] = ['name', 'products'];

  constructor(private http: HttpClientService,
              private dialog: MatDialog,
              private orderService: OrderService) {
  }

  ngOnInit() {
    this.http.getManufacturersByProducts().subscribe(value => this.manufacturers = value);
  }

  showManufacturers() {
    this.isManufacturers = this.isManufacturers !== true;
  }

  openDialog = (type: string) => {
    this.orderService.setType(type);
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '1800px';
    dialogConfig.height = '756px';
    dialogConfig.backdropClass = 'popupBackdropClass';
    this.dialog.open(DatedReportsComponent, dialogConfig);
  };

}
