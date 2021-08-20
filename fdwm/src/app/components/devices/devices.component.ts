import { Component, OnInit } from '@angular/core';
import { DeviceDTO } from '../../models/deviceDTO';
import { DeviceService } from 'src/app/services/device.service';

@Component({
  selector: 'app-devices',
  templateUrl: './devices.component.html',
  styleUrls: ['./devices.component.css']
})
export class DevicesComponent implements OnInit {

  items: DeviceDTO[] = [];

  constructor(private deviceService:DeviceService) { }

  ngOnInit(): void {
//    this.items = [];
//    this.items = this.deviceService.getDevices();
    this.deviceService.getDevices().subscribe(data => {
      this.items = data;
    })
  }

  deleteDevice(device: DeviceDTO) {
    this.items = this.items.filter(x => x.id != device.id);
  }

  enableDevice(device: DeviceDTO) {
    device.enable = !device.enable;
  }
}
