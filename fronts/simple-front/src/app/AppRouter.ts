import {Router} from '@src/com/simple/boot/module/Router'
import {Sim} from '@src/com/simple/boot/decorators/SimDecorator'
import {HelloWord} from '@src/app/features/hello/hello-word'
import {Index} from '@src/app/features/index/index'
import {HelloRouter} from '@src/app/HelloRouter'
import {Module} from '@src/com/simple/boot/module/Module'
import {SimstanceManager} from '@src/com/simple/boot/simstance/SimstanceManager'
import {WowRouter} from '@src/app/WowRouter'

@Sim()
export class AppRouter extends Router {
    constructor(public simstance: SimstanceManager) {
        super('', new Module('app-empty'), [
            HelloRouter,
            WowRouter
        ])
        console.log('AppRouter constructor', simstance)
    }

    '' = Index
    'hello/wow' = HelloWord
    'hello-world' = HelloWord
}
