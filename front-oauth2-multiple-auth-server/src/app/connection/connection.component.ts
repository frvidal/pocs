import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-connection',
  templateUrl: './connection.component.html',
  styleUrls: ['./connection.component.css']
})
export class ConnectionComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  public onClick() {
    this.router.navigate(['/main']);
  }
}
