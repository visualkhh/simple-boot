import {Router} from '@src/com/simple/boot/module/Router'
import {Sim} from '@src/com/simple/boot/decorators/SimDecorator'
import {HelloWord} from '@src/app/features/hello/hello-word'
import {Index} from '@src/app/features/index'
import {HelloRouter} from '@src/app/HelloRouter'
import {WowRouter} from '@src/app/WowRouter'
import {App} from '@src/app/layouts/App'

@Sim()
export class AppRouter extends Router {
    path = ''
    module = App
    childs = [
        HelloRouter,
        WowRouter
    ]

    '' = Index
    'hello/wow' = HelloWord
    'hello-world' = HelloWord
}
