import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, tap } from 'rxjs/operators';

import { environment } from '../../environments/environment';
import { AppConfig } from '../interfaces/app-config.interface';
import { Observable, throwError } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class ConfigService {

    static settings: AppConfig;

    constructor(private http: HttpClient) { }

    loadConfig(): Promise<AppConfig> {
        const jsonFile = `assets/config.${environment.name}.json`;
        return this.http.get<AppConfig>(jsonFile).pipe(
            tap(resp => ConfigService.settings = resp),
            catchError(e => throwError(`Could not load file '${jsonFile}': ${JSON.stringify(e)}`))
        ).toPromise()
    }
}