import { HttpClient } from '@angular/common/http';
import { CloneVisitor } from '@angular/compiler/src/i18n/i18n_ast';
import { AfterViewInit, Component } from '@angular/core';

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.css']
})
export class AppComponent implements AfterViewInit {

	title = 'Testing the connection to Fitzhi';

	constructor (public httpClient: HttpClient) {}
	
	ngAfterViewInit(): void {
		const subscription = this.httpClient
			.get('http://localhost:8080/api/admin/isVeryFirstConnection', { responseType: 'text' as 'json' } )
				.subscribe({
					next: (result: string) => console.log ('result', result),
					error: error => console.log ('error', error)
				});
	}

}
