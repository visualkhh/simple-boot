import {Sim} from '@src/com/simple/boot/decorators/SimDecorator'
import {Module} from '@src/com/simple/boot/module/Module'
import html from './index.html'

@Sim()
export class Index extends Module {
    public numbers = [1, 2, 3, 4, 5, 6, 7];
    template = html;
}
