import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {User, UserService} from "../../services/user.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.css']
})
export class CreateUserComponent implements OnInit {

  createUserForm: FormGroup;
  formSubmitted = false;
  isLoading = false;

  constructor(private router: Router, private formBuilder: FormBuilder, private userService: UserService) { }

  ngOnInit(): void {
    this.createUserForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit() {
    this.formSubmitted = true;

    if (this.createUserForm.invalid) {
      return;
    }

    let userToCreate = new User(this.createUserForm.get('firstName').value, this.createUserForm.get('lastName').value,
        this.createUserForm.get('email').value, this.createUserForm.get('password').value);
    this.userService.create(userToCreate);
    this.isLoading = true;

    setTimeout(() => {
      this.router.navigate(['']);
    }, 1500)
  }

  onReset() {
    this.formSubmitted = false;
    this.createUserForm.reset();
  }
}
