
import { HttpErrorResponse, HttpHandler, HttpHeaderResponse, HttpInterceptor, HttpProgressEvent, HttpRequest, HttpResponse, HttpSentEvent, HttpUserEvent } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, EMPTY, Observable, throwError as observableThrowError, throwError } from 'rxjs';
import { catchError, filter, finalize, switchMap, take } from 'rxjs/operators';
import { Token } from './token';
import { TokenService } from './token.service';

@Injectable()
export class HttpTokenInterceptorService implements HttpInterceptor {

	authToken$: BehaviorSubject<Token> = new BehaviorSubject<Token>(null);

	isRefreshingToken = false;

	constructor(
		private tokenService: TokenService) { }

	intercept(req: HttpRequest<any>, next: HttpHandler):
		Observable<HttpSentEvent | HttpHeaderResponse | HttpProgressEvent | HttpResponse<any> | HttpUserEvent<any>> {

			return next.handle(this.tokenService.addToken(req)).pipe(
			catchError(response => {
				if (response instanceof HttpErrorResponse) {
					if ((response.status === 401) && !this.isConnectionRequest(req)) {
						return this.retryAfterRefresh$(req, next);
					} else {
						console.log ('Invalid authentification credentials');
						return EMPTY;				
					}
				}
			}));
	}

	isConnectionRequest(req: HttpRequest<any>): boolean {
		
		if (!req.body) {
			return false;
		}
		
		let isConnection = false;
		req.body.params.updates.forEach(element => {
			if ((element.param === 'grant_type') && (element.value === 'password')) {
				isConnection = true;
			}
		});
		return isConnection;
	}

	retryAfterRefresh$(req: HttpRequest<any>, next: HttpHandler): 
		Observable<HttpSentEvent | HttpHeaderResponse | HttpProgressEvent | HttpResponse<any> | HttpUserEvent<any>> {

		if (!this.isRefreshingToken) {
			this.isRefreshingToken = true;
			
			console.log ('retryAfterRefresh(...)');
			this.authToken$.next(null);

			return this.tokenService.refreshToken$()
				.pipe(
					take(1),
					switchMap(token => {
						// store the new tokens
						this.tokenService.token = token;
						if (token) {
							console.log('access_token %s expires in %s', token.access_token, token.expires_in);
						}

						this.authToken$.next(token);
                        return next.handle(this.tokenService.addToken(req));
					})
					,catchError(response => {
						console.log ('Error', response);
						return EMPTY;
					}),	
					finalize(() => this.isRefreshingToken = false)
				);
		} else {
			return this.authToken$
			.pipe(
				filter(token => token != null)
				, take(1)
				, switchMap(token => {
					return next.handle(this.tokenService.addToken(req));
				})
			);
		}
	}

	logoutUser() {
		// Route to the login page (implementation up to you)
		// this.router.navigate(['/welcome'], {});
		return observableThrowError('');
	}
}
