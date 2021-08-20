import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DeviceDTO } from '../models/deviceDTO';
import { HttpClient } from '@angular/common/http';

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

  constructor(private http:HttpClient) { }

  getDevices():Observable<DeviceDTO[]> {
    return this.http.get<DeviceDTO[]>(this.url + 'all');
  }

  addDevice(device:DeviceDTO):Observable<DeviceDTO> {
    return this.http.post<DeviceDTO>(this.url + 'add', device, this.httpOptions);
  }
}