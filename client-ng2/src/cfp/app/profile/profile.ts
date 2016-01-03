import {Component} from 'angular2/core';

@Component({
    selector: 'profile',
    templateUrl: 'app/profile/profile.html'
})
export class ProfileComponent {
    title: string = 'Profile Page';
    body:  string = 'This is the about home body';
}