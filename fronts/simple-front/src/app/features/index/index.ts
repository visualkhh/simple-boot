import {Sim} from '../../../com/simple/boot/decorators/SimDecorator'
import {Module} from '../../../com/simple/boot/module/Module'
import html from './index.html'
import {ViewService} from '@src/com/simple/boot/service/ViewService'
import {RandomUtils} from '@src/com/simple/boot/util/random/RandomUtils'

@Sim()
export class Index extends Module {
    public datas = [1, 2, 3, 4, 5, 6, 7];
    template = html;

    constructor(public v: ViewService) {
        super('index')
    }

    onInit() {
    }

    onChangedRendered() {
        this.v.eI('change')?.click().subscribe(it => {
            this.datas = [RandomUtils.random(1, 400), RandomUtils.random(1, 400), RandomUtils.random(1, 400)];
        })
    }
}
