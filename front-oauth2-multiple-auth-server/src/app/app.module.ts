import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import {TokenService} from './token.service';
import {HttpTokenInterceptorService} from './http-token-interceptor.service';
import {HttpRefreshTokenErrorInterceptorService} from './http-refresh-token-error-interceptor.service';

import { AppRoutingModule } from './app-routing.module';
import { MainComponent } from './main/main.component';
import { ConnectionComponent } from './connection/connection.component';

@NgModule({
	declarations: [
		AppComponent,
		MainComponent,
		ConnectionComponent
	],
	imports: [
		BrowserModule,
		HttpClientModule,
		AppRoutingModule
	],
	providers: [
		TokenService, 
		HttpTokenInterceptorService,
		{
			provide: HTTP_INTERCEPTORS,
			useClass: HttpTokenInterceptorService,
			multi: true
		},
		{
			provide: HTTP_INTERCEPTORS,
			useClass: HttpRefreshTokenErrorInterceptorService,
			multi: true
		},
],
	bootstrap: [AppComponent]
})
export class AppModule { }
