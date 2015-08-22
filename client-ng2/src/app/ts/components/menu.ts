/// <reference path="../../../../typings/angular2/angular2.d.ts" />

//TODO used coreDirectives
import {Component, View, NgIf, NgFor} from 'angular2/angular2';

@Component({
    selector: 'cesar-menu'
})
@View({
    templateUrl:'views/components/menu.html',
    directives:[NgIf, NgFor]
})
export class CesarMenuComponent {
    menus:Array<CesarMenuItem>;

    constructor() {
        this.menus = [
            new CesarMenuItem('actu').createSimpleItem('Actualités', '#'),
            new CesarMenuItem('prog').createItemWithSubItems('Programme', [
                new CesarMenuItem(undefined).createSimpleItem('Planning', '#'),
                new CesarMenuItem(undefined).createSimpleItem('Conférences, ateliers', '#'),
                new CesarMenuItem(undefined).createSimpleItem('Ligthning talks', '#'),
                new CesarMenuItem(undefined).createSimpleItem('Mix-Teen', '#'),
            ])
        ]
    }

}

class CesarMenuItem {
    id:string;
    name:string;
    link:string;
    submenus:Array<CesarMenuItem>;

    constructor(id:string){
        this.id = id;
    }

    createSimpleItem(name:string, link:string) {
        this.name = name;
        this.link = link;
        return this;
    }

    createItemWithSubItems(name:string, submenus:Array<CesarMenuItem>) {
        this.name = name;
        this.link = '#';
        this.submenus = submenus;
        return this;
    }
}
