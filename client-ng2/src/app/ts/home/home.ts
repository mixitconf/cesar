/// <reference path="../../../../typings/angular2/angular2.d.ts" />
/// <reference path="./home-headband.ts" />
/// <reference path="./home-info.ts" />
/// <reference path="./home-photo.ts" />
/// <reference path="./home-speaker.ts" />
/// <reference path="./home-sponsor.ts" />
import {Component, View} from 'angular2/angular2';
import {CesarHomeHeadbandComponent} from './home-headband';
import {CesarHomeInfoComponent} from './home-info';
import {CesarHomePhotoComponent} from './home-photo';
import {CesarHomeSpeakerComponent} from './home-speaker';
import {CesarHomeSponsorComponent} from './home-sponsor';

@Component({
    selector: 'cesar-home'
})
@View({
    templateUrl:'views/home.html',
    directives: [CesarHomeHeadbandComponent, CesarHomeInfoComponent, CesarHomePhotoComponent, CesarHomeSpeakerComponent, CesarHomeSponsorComponent]
})
export class CesarHomeComponent {

}


