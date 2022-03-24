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

import { SocialLoginModule, SocialAuthServiceConfig } from 'angularx-social-login';
import { GoogleLoginProvider } from 'angularx-social-login';


@NgModule({
	declarations: [
		AppComponent,
		MainComponent,
		ConnectionComponent
	],
	imports: [
		BrowserModule,
		HttpClientModule,
		AppRoutingModule,
		SocialLoginModule
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
		{
			provide: 'SocialAuthServiceConfig',
			useValue: {
			  autoLogin: false,
			  providers: [
				{
				  id: GoogleLoginProvider.PROVIDER_ID,
				  provider: new GoogleLoginProvider('690807651852-sqjienqot7ui0pufj4ie4n320pss5ipc.apps.googleusercontent.com')
				}
			  ]
			} as SocialAuthServiceConfig,
		  }


	],
	bootstrap: [AppComponent]
})
export class AppModule { }
