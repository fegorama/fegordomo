import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DevicesComponent } from './components/devices/devices.component';
import { AddDeviceComponent } from './components/add-device/add-device.component';
import {EditDeviceComponent } from './components/edit-device/edit-device.component';

const routes: Routes = [
  {
    path: '',
    component: DevicesComponent
  },
  {
    path: 'add-device',
    component: AddDeviceComponent
  },
  {
    path: 'edit-device/:id',
    component: EditDeviceComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
