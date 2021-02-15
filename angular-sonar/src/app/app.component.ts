import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { take } from 'rxjs/operators';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Angular and Sonar dialog';

  public sonarQubeServer = 'http://localhost:9000';


  constructor(private httpClient : HttpClient) {}

  public go() {
    this.initSonarServer(this.sonarQubeServer.replace('localhost:9000', 'localhost:4200'));
	this.loadSonarSupportedMetrics(this.sonarQubeServer.replace('localhost:9000', 'localhost:4200'));
  }

	/**
	 * Initialize the `SonarServer` object for future usage, and tests its avaibility by retrieving its version.
	 * @param urlSonar the URL of the Sonar server
	 */
	private initSonarServer(urlSonar: string) {
		const subscription = this.httpClient
			.get(urlSonar + '/api/server/version', { responseType: 'text' as 'json' })
				.subscribe({
					next: result => console.log (result),
					complete: () => setTimeout(() => { subscription.unsubscribe(); } , 0)
				});
	}


	public authenticate(urlSonar: string) {

		const params = new HttpParams()
			.set('login', 'admin')
			.set('password', 'parissg75');

		this.httpClient
			.post<boolean>(urlSonar + '/api/authentication/login',  '', { params: params, observe: 'response'})
			.pipe(take(1))
			.subscribe({
				next: r  => {
					console.log ('Authentication Ok');
				},
			})
	}

	/**
	 * Load the supported metrics of this Sonar server, which are supported by the application.
	 * @param urlSonar the URL of the Sonar server
	 */
	private loadSonarSupportedMetrics(urlSonar: string) {

		let headers: HttpHeaders = new HttpHeaders();

		// No authentication required
		// const authdata = 'Basic ' + btoa('admin:parissg75');
		// headers = headers.append('Authorization', authdata);

		this.httpClient.get(urlSonar + '/api/metrics/search?ps=500', {headers: headers}).subscribe({
			next: metrics => { console.log(metrics); }
		});
	}

}
