import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DeviceDTO } from 'src/app/models/deviceDTO';
import { DeviceService } from 'src/app/services/device.service';

@Component({
  selector: 'app-edit-device',
  templateUrl: './edit-device.component.html',
  styleUrls: ['./edit-device.component.css']
})
export class EditDeviceComponent implements OnInit {
  currentDevice:DeviceDTO = {
    id: 0,
    name: '',
    type: '',
    ip: '',
    enable: true
  }

  constructor(
    private deviceService:DeviceService, 
    private route: ActivatedRoute, 
    private router:Router) { }

  ngOnInit(): void {
    this.getDevice(this.route.snapshot.params.id);
  }

  getDevice(id: string): void {
    this.deviceService.getDevice(id)
      .subscribe(
        data => {
          this.currentDevice = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
  }

  onSubmit() {
    this.deviceService.updateDevice(this.currentDevice.id, this.currentDevice)
      .subscribe(
        r => {
          console.log(r);
          this.router.navigate(['/']);
        },
        e => {
          console.log(e);
        }
      );
  }

}
