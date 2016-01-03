import {bootstrap} from 'angular2/platform/browser';
import {ROUTER_PROVIDERS} from 'angular2/router';
import {CfpApp} from './app/cfp';


//noinspection TypeScriptValidateTypes
bootstrap(CfpApp, [
    ROUTER_PROVIDERS
]);
