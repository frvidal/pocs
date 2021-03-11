import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EMPTY, Observable, of, throwError } from 'rxjs';
import { catchError, tap} from 'rxjs/operators';
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
		headers = headers.append('Authorization', 'Basic ' + btoa('fitzhi-trusted-client' + ':secret'));

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
		return (this.token) ?
			req.clone({ setHeaders: { Authorization: 'Bearer ' + this.token.access_token } }) : req;
	}
}
