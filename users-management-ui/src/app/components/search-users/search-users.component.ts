import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from "@angular/forms";
import {UserService} from "../../services/user.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-search-users',
  templateUrl: './search-users.component.html',
  styleUrls: ['./search-users.component.css']
})
export class SearchUsersComponent implements OnInit {

  searchUsersForm: FormGroup;
  formSubmitted = false;
  users: any;

  constructor(private formBuilder: FormBuilder, private userService: UserService) {
  }

  ngOnInit(): void {
    this.searchUsersForm = this.formBuilder.group({
      firstName: [''],
      lastName: [''],
      email: [''],
      password: ['']
    });
    this.search("", "", "");
  }

  onSubmit() {
    this.search(this.searchUsersForm.get('firstName').value,
      this.searchUsersForm.get('lastName').value, this.searchUsersForm.get('email').value);
  }

  search(firstName, lastName, email) {
    this.userService.search(firstName, lastName, email).subscribe(
      data => {
        this.users = data['items'];
      },
      error => {
        console.log(error);
      }
    )
  }
}
