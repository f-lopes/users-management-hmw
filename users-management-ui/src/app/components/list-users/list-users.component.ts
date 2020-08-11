import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-list-users',
  templateUrl: './list-users.component.html',
  styleUrls: ['./list-users.component.css']
})
export class ListUsersComponent implements OnInit {

  displayedColumns: string[] = ['id', 'firstName', 'lastName', 'email', 'password'];
  @Input() users: any;

  constructor() { }

  ngOnInit(): void {
  }
}
