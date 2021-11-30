import { Component, OnInit } from '@angular/core';
import { AnonymousSubject } from 'rxjs/internal/Subject';

declare var require: any;

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{

	title = 'POC of Asciidoc Editor';
 
	public html = "...";
	
	public adocText = '';

	private asciidoctor: any;

	ngOnInit(): void {
		this.asciidoctor = require('asciidoctor')();
	}

	public input($event: any) {
		this.html = this.asciidoctor.convert(this.adocText);
		console.log (this.html);
	}
}
