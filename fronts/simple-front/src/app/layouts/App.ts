import {Sim} from '../../com/simple/boot/decorators/SimDecorator'
import {Module} from '../../com/simple/boot/module/Module'
import {AjaxService} from '../../com/simple/boot/service/AjaxService'
import html from './app.html'
import {SimstanceManager} from '../../com/simple/boot/simstance/SimstanceManager'
import css from './app.css'
import cssFirst from './app-first.css'
// const feather = 'a';
// import feather from '../../../test.txt'
// import feather from 'feather-icons/dist/feather.min'
// import('feather-icons/dist/feather.min')
// const feather = require('feather-icons/dist/feather.min')

@Sim()
export class App extends Module {
    styleImports = [cssFirst, css]
    constructor(public ajaxService: AjaxService, public simstance: SimstanceManager) {
        super('app-router-module', html)
        // import('feather-icons/dist/feather.min.js').then(it => {
        //     alert('it====', it)
        // })
        // console.log('App Constructor-->', feather)
    }

    onInit() {
    }

    onChangedRendered() {
    }
}
