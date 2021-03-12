import {Router} from '@src/com/simple/boot/module/Router'
import {Sim} from '@src/com/simple/boot/decorators/SimDecorator'
import {HelloWord} from '@src/app/features/hello/hello-word'
import {Index} from '@src/app/features/index/index'

@Sim()
export class WowRouter extends Router {
    constructor() {
        super('wow/')
        console.log('WowRouter constructor ', this.path)
    }

    '' = Index
    'hello/wow' = HelloWord
    'hello-world' = HelloWord
}
