import {Sim} from '@src/com/simple/boot/decorators/SimDecorator'
import {Module} from '@src/com/simple/boot/module/Module'
import {AjaxService} from '@src/com/simple/boot/service/AjaxService'
import html from './sub.html'
import {I18nService} from '@src/app/features/service/I18nService'
// import {fromEvent} from 'rxjs'
// import {LoopModule} from '@src/app/shareds/LoopModule'
import {SimstanceManager} from '@src/com/simple/boot/simstance/SimstanceManager'

@Sim()
export class Sub extends Module {
    constructor(public i18nService: I18nService, public ajaxService: AjaxService, public simstance: SimstanceManager) {
        super('app-suub-router-module', html)
        console.log('--->', simstance);
    }

    onInit() {
    }

    loadData() {
    }

    onChangedRendered() {
    }
}
