import {Sim} from '../../../com/simple/boot/decorators/SimDecorator'
import {Module} from '../../../com/simple/boot/module/Module'
import html from './index.html'
@Sim()
export class Index extends Module {
    public datas = [1, 2, 3, 4, 5, 6, 7];
    template = html;

    constructor() {
        super('index')
    }

    onInit() {
    }

    onChangedRendered() {
    }
}
