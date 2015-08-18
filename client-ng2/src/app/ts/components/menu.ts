/// <reference path="../../../../typings/angular2/angular2.d.ts" />

import {Component, View} from 'angular2/angular2';

@Component({
    selector: 'cesar-menu'
})
@View({
    templateUrl:'views/components/menu.html'
})
export class CesarMenuComponent {
    menus:any;

    constructor() {
        this.menus = [
            new CesarMenuItem('test')
        ]
    }
}

class CesarMenuItem {
    name:string;
    link:string;
    submenus:any;

    constructor(name:string) {
        this.name = name;
    }
}
