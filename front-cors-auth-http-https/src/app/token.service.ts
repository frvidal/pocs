import { HttpClient, HttpHeaders, HttpParams, HttpRequest } from '@angular/common/http';
import { temporaryAllocator } from '@angular/compiler/src/render3/view/util';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Token } from './token';

@Injectable({
	providedIn: 'root'
})
export class TokenService {

	token: Token;

	constructor(private httpClient: HttpClient) { }

	refreshToken$(): Observable<Token> {

		let headers: HttpHeaders = new HttpHeaders();
		headers = headers.append('Content-Type', 'application/x-www-urlencoded');
		headers = headers.append('Authorization', 'Basic ' + btoa('test-trusted-client' + ':secret'));

		const params = new HttpParams()
			.set('refresh_token', this.token.refresh_token)
			.set('grant_type', 'refresh_token');

		return this.httpClient.post<Token>('http://localhost:8080/oauth/token', '',
			{ headers: headers, params: params });
	}

	addToken(req: HttpRequest<any>): HttpRequest<any> {
		if (req.params.get('grant_type')) {
			switch (req.params.get('grant_type')) {
				case 'refresh_token': 
				case 'password':
					return req;
			}
		}
		const temp = (this.token) ?
			req.clone({ setHeaders: { Authorization: 'Bearer ' + this.token.access_token } }) : req;
		return temp;
	}
}
