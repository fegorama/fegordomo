import { NgModule, APP_INITIALIZER } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { DeviceComponent } from './components/device/device.component';
import { DevicesComponent } from './components/devices/devices.component';
import { DeviceItemDirective } from './components/device-item.directive';
import { AddDeviceComponent } from './components/add-device/add-device.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { EditDeviceComponent } from './components/edit-device/edit-device.component'
import { ConfigService } from './services/config.service';

function initializeApp(appConfig: ConfigService) {
  return () => appConfig.loadConfig();
}
@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    DeviceComponent,
    DevicesComponent,
    DeviceItemDirective,
    AddDeviceComponent,
    EditDeviceComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [
    {
      provide: APP_INITIALIZER,     
      useFactory: initializeApp,      
      deps: [ConfigService],      
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
