import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Token } from '../token';
import { TokenService } from '../token.service';

@Component({
	selector: 'app-main',
	templateUrl: './main.component.html',
	styleUrls: ['./main.component.css']
})
export class MainComponent {

	title = 'Testing the connection to a backend server';

	statusOK: string;
	messageOK: string;

	statusKO: string;
	messageKO: string;

	statusPing: string;
	messagePing: string;

	statusPingSecure: string;
	messagePingSecure: string;

	constructor(public httpClient: HttpClient, public tokenService: TokenService) { }

	connectionOK() {
		let headers: HttpHeaders = new HttpHeaders();
		headers = headers.append('Content-Type', 'application/x-www-urlencoded');
		headers = headers.append('Authorization', 'Basic ' + btoa('fitzhi-trusted-client' + ':secret'));

		const params = new HttpParams()
			.set('username', 'admin')
			.set('password', 'parissg75')
			.set('grant_type', 'password');

		this.httpClient.post<Token>
			('http://localhost:8080/oauth/token', '', { headers: headers, params: params })
			.subscribe({
				next: (token: Token) => {
					this.tokenService.token = token;
					if (token) {
						console.log('access_token %s expires in %s', token.access_token, token.expires_in);
					}
					this.statusOK = 'OK'
					this.messageOK = token.refresh_token;

					this.tokenService.refreshToken$().subscribe({
						next: token => {
							this.tokenService.token = token;
							if (token) {
								console.log('access_token %s expires in %s', token.access_token, token.expires_in);
							}
						},
						error: error => console.log(error)
					})
				}
			});
	}

	connectionKO() {
		let headers: HttpHeaders = new HttpHeaders();
		headers = headers.append('Content-Type', 'application/x-www-urlencoded');
		headers = headers.append('Authorization', 'Basic ' + btoa('fitzhi-trusted-client' + ':secret'));

		const params = new HttpParams()
			.set('username', 'myTestUser')
			.set('password', 'myTestPassKO')
			.set('grant_type', 'password');

		this.httpClient.post<Token>
			('http://localhost:8080/oauth/token', { headers: headers, params: params })
			.subscribe({
				error: response => {
					this.statusKO = response.status;
					this.messageKO = response.message;
					this.tokenService.token = null;
				}
			});

	}


	ping() {
		this.httpClient.get('http://localhost:8080/api/ping', { responseType: 'text' as 'json' })
			.subscribe({
				next: (result: string) => this.messagePing = result,
				error: error => console.log('error', error)
			});

	}

	pingSecure() {
		this.httpClient.get('http://localhost:8080/api/staff/1', { responseType: 'text' as 'json' })
			.subscribe({
				next: (result: string) => this.messagePingSecure = result,
				error: response => {
					this.statusPingSecure = response.status;
					this.messagePingSecure = response.message;
				}
			});
	}

}
