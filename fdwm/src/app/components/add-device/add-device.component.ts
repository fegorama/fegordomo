import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DeviceDTO } from 'src/app/models/deviceDTO';
import { DeviceService } from 'src/app/services/device.service';

@Component({
  selector: 'app-add-device',
  templateUrl: './add-device.component.html',
  styleUrls: ['./add-device.component.css']
})
export class AddDeviceComponent implements OnInit {

  name:string = '';
  type:string = '';
  ip:string = '';

  constructor(private deviceService:DeviceService, private router:Router) { }

  ngOnInit(): void {
  }

  onSubmit() {
    const device = new DeviceDTO();
    device.enable = true;
    device.name = this.name;
    device.type = this.type;
    device.ip = this.ip;

    this.deviceService.addDevice(device).subscribe(r => {
      this.router.navigate(['/']);
    });
  }
  
}