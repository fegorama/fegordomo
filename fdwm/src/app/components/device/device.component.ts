import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { DeviceDTO } from 'src/app/models/deviceDTO';

@Component({
  selector: 'app-device',
  templateUrl: './device.component.html',
  styleUrls: ['./device.component.css']
})
export class DeviceComponent implements OnInit {

  @Input() device: DeviceDTO = new DeviceDTO();
  @Output() deleteDevice: EventEmitter<DeviceDTO> = new EventEmitter();
  @Output() enableDevice: EventEmitter<DeviceDTO> = new EventEmitter();

  constructor() { }

  ngOnInit(): void {
  }

  onDelete(device: DeviceDTO) {
    this.deleteDevice.emit(device);
  }

  onEnable(device: DeviceDTO) {
    this.enableDevice.emit(device);
  }
}
