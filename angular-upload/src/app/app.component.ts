import { HttpClient, HttpRequest } from '@angular/common/http';
import { Component } from '@angular/core';

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.css']
})
export class AppComponent {
	
	title = 'Testing to upload a file to a backends';

	file: File;

	public uri = 'http://localhost:8080/api/upload/do';

	constructor(private httpClient: HttpClient) {

	}

	public fileEvent($event) {
		this.file = $event.target.files[0];
	}	

	public upload() {
		console.log ('upload()');

		const formData: FormData = new FormData();
		formData.append('file', this.file, this.file.name);

		// create a HTTP-post request and pass the form
		// tell it to report the upload progress
		const req = new HttpRequest('POST', this.uri, formData,  {
			reportProgress: true
		});

		// send the HTTP-request and subscribe for progress-updates
		this.httpClient.request(req).subscribe({
			next: event => console.log (event),
			error: error => console.log (error)
		});

	}
}
