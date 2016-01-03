import {Component} from 'angular2/core';

@Component({
    selector: 'talks',
    templateUrl: 'app/talks/talks.html'
})
export class TalksComponent {
    title: string = 'Talk Page';
    body:  string = 'This is the about home body';
}