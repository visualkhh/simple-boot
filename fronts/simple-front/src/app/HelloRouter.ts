import {Router} from '@src/com/simple/boot/module/Router'
import {Sim} from '@src/com/simple/boot/decorators/SimDecorator'
import {HelloWord} from '@src/app/features/hello/hello-word'
import {Index} from '@src/app/features/index/index'
import {Sub} from '@src/app/layouts/sub/Sub'

@Sim()
export class HelloRouter extends Router {
    path = 'hello/'
    module = Sub
    '' = Index
    'hello/wow' = HelloWord
    'hello-world' = HelloWord
}
