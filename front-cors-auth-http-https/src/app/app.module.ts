import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import {TokenService} from './token.service';
import {HttpTokenInterceptorService} from './http-token-interceptor.service';

@NgModule({
	declarations: [
		AppComponent
	],
	imports: [
		BrowserModule,
		HttpClientModule
	],
	providers: [
		TokenService, 
		HttpTokenInterceptorService,
		{
			provide: HTTP_INTERCEPTORS,
			useClass: HttpTokenInterceptorService,
			multi: true
		},
],
	bootstrap: [AppComponent]
})
export class AppModule { }
