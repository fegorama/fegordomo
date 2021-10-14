import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DeviceDTO } from '../models/deviceDTO';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';

@Injectable({
  providedIn: 'root'
})
export class DeviceService {

  url:string = 'http://localhost:8080/devices/';

  httpOptions = {
    headers: {
      'Content-Type': 'application/json'
    }
  };

  constructor(private http:HttpClient, private configService: ConfigService) { }

  // si no funciona reemplazar ConfigService.settings por this.url...

  getDevices():Observable<DeviceDTO[]> {
    return this.http.get<DeviceDTO[]>(ConfigService.settings.apiUrl + 'all');
  }

  getDevice(id:string):Observable<DeviceDTO> {
    return this.http.get<DeviceDTO>(ConfigService.settings.apiUrl + id);
  }

  addDevice(device:DeviceDTO):Observable<DeviceDTO> {
    return this.http.post<DeviceDTO>(ConfigService.settings.apiUrl + 'add', device, this.httpOptions);
  }

  deleteDevice(device:DeviceDTO):Observable<DeviceDTO> {
    return this.http.delete<DeviceDTO>(ConfigService.settings.apiUrl + device.id.toString());
  }

  updateDevice(id:number, device:DeviceDTO):Observable<DeviceDTO> {
    return this.http.put<DeviceDTO>(ConfigService.settings.apiUrl + id.toString(), device, this.httpOptions);
  }

}