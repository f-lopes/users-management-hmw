import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";

const usersApiBaseUrl = 'http://api.localhost/v1/users/';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get(usersApiBaseUrl);
  }

  create(user: User) {
    this.http.post(usersApiBaseUrl, user).subscribe(
      (error) => console.log(error)
    );
  }

  search(firstName: string, lastName: string, email: string) {
    return this.http.get(usersApiBaseUrl + "/search" + "?firstName=" + firstName + "&lastName=" + lastName + "&email=" + email);
  }
}

export class User {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  password: string;

  constructor(firstName: string, lastName:string, email: string, password: string) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
  }
}
