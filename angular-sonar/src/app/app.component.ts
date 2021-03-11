import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { of } from 'rxjs';
import { switchMap, take } from 'rxjs/operators';
import { Metric } from './metric';
import { Project } from './project';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Angular and Sonar dialog';

  public sonarQubeServer = 'http://localhost:9000';

  public user = 'admin';

  public password = 'admin';

  public version = '';

  public prefixSonarQube = '/sonarqube';

  public metrics = [];

  public projects = [];

  public connectionSettings = new Map<string, string>();

  constructor(private httpClient : HttpClient) {}

  public go() {

	console.log ('urlSonar', this.sonarQubeServer);

	this.initSonarServer(this.sonarQubeServer);
	this.authenticate(this.sonarQubeServer).subscribe({
		next: doneAndOk => {
			if (doneAndOk) {
				console.log ('Connected');
			}
		}
	});
  }

	/**
	 * Initialize the `SonarServer` object for future usage, and tests its avaibility by retrieving its version.
	 * @param urlSonar the URL of the Sonar server
	 */
	private initSonarServer(urlSonar: string) {
		console.log('initSonarServer("' + urlSonar + '")');
		const subscription = this.httpClient
			.get(urlSonar + '/api/server/version', { responseType: 'text' as 'json' })
				.subscribe({
					next: (result: string) => this.version = result,
					complete: () => setTimeout(() => { subscription.unsubscribe(); } , 0)
				});
	}


	public authenticate(urlSonar: string) {

		let params = new HttpParams()
			.set('login', this.user)
			.set('password', this.password);

		return this.httpClient
			.post<any>(urlSonar + '/api/authentication/login', '', {responseType: 'text' as 'json',  params: params})
			.pipe(
				take(1),
				switchMap(r => { 
					return of(true); 
				}
			)
		);
	}

	/**
	 * Load the supported metrics of this Sonar server, which are supported by the application.
	 */
	public loadSonarSupportedMetrics() {

		this.metrics = [];

		let headers: HttpHeaders = new HttpHeaders();

		// No authentication required
		// const authdata = 'Basic ' + btoa('admin:parissg75');
		// headers = headers.append('Authorization', authdata);

		this.httpClient.get(this.sonarQubeServer + '/api/metrics/search?ps=500', {headers: headers}).subscribe({
			next: (metrics: any) => { 
				metrics.metrics.forEach(m => {
					this.metrics.push (new Metric(m.key, m.name));
				});
			 }
		});
	}

	/**
	 * Load the projects declared on this Sonar server.
	 */
	 public loadSonarProjects() {

		this.projects = [];

		let headers: HttpHeaders = new HttpHeaders();

		// No authentication required
		const authdata = 'Basic ' + btoa(this.user + ':' + this.password);
		headers = headers.append('Authorization', authdata);

		this.httpClient.get(this.sonarQubeServer + '/api/components/search?ps=500&qualifiers=TRK', {headers: headers}).subscribe({
			next: (response: any) => { 
				response.components.forEach(c => {
					this.projects.push (new Project(c.key, c.name));
				});
			 }
		});
	}
}
