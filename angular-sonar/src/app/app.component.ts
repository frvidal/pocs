import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';

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

    this.initSonarServer('http://localhost:9000');
		const subscription = this.httpClient
			.get(this.sonarQubeServer + '/api/server/version', {responseType: 'text' as 'json' })
        .subscribe({
          next: (version: string) => console.log (version) });
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


}
