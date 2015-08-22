/// <reference path="../../../typings/angular2/angular2.d.ts" />
/// <reference path="menu/menu.ts" />

import {Component, View, bootstrap} from 'angular2/angular2';
import {CesarMenuComponent} from './menu/menu';
import {CesarHomeComponent} from './home/home';

// Annotation section
@Component({
    selector: 'cesar-app'
})
@View({
    templateUrl: 'views/app.html',
    directives: [CesarMenuComponent, CesarHomeComponent]
})
// Component controller
class CesarAppComponent {
    name: string;
    constructor() {
        //Hack to be able dynamic components of Material Design Lite
        componentHandler.upgradeAllRegistered();
        this.name = 'Alice';
    }
}

bootstrap(CesarAppComponent);