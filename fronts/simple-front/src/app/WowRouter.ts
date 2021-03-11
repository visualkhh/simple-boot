import {Router} from '@src/com/simple/boot/module/Router'
import {Sim} from '@src/com/simple/boot/decorators/SimDecorator'
import {HelloWord} from '@src/app/features/hello/hello-word'
import {Index} from '@src/app/features/index/index'
import {AppRouter} from '@src/app/AppRouter'

@Sim()
export class HelloRouter extends Router {
    constructor() {
        super('wow/')
        console.log('helloRouter constructor ', this.root)
    }

    '' = Index;
    'hello/wow' = HelloWord;
    'hello-world' = HelloWord;
}
