import {Router} from '@src/com/simple/boot/module/Router'
import {Sim} from '@src/com/simple/boot/decorators/SimDecorator'
import {HelloWord} from '@src/app/features/hello/hello-word'
import {Index} from '@src/app/features/index/index'
import {HelloRouter} from '@src/app/HelloRouter'

@Sim()
export class AppRouter extends Router {
    constructor() {
        super('', [
            HelloRouter
        ])
        console.log('AppRouter constructor')
    }

    '' = Index;
    'hello/wow' = HelloWord;
    'hello-world' = HelloWord;
}
