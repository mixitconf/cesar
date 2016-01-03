import {Component} from 'angular2/core';
import {RouteConfig, ROUTER_DIRECTIVES} from 'angular2/router';
import {CesarMenu} from './menu/menu';
import {HomeComponent} from './home/home';
import {ProfileComponent} from './profile/profile';
import {TalksComponent} from './talks/talks';

@Component({
  selector: 'cfp-app',
  templateUrl: 'app/cfp.html',
  styleUrls: ['app/cfp.css'],
  directives: [ROUTER_DIRECTIVES, CesarMenu]
})
@RouteConfig([
  {path: '/home', name: 'Home', component: HomeComponent, useAsDefault: true},
  {path: '/profile', name: 'Profile', component: ProfileComponent},
  {path: '/talks', name: 'Talks', component: TalksComponent}
])
export class CfpApp {
}
